package Translator;

import java.io.*;
import Lexer.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else error("syntax error");
    }

    public void prog() {        
	    if (look.tag == '(') {
            int lnext_prog = code.newLabel();
            stat(lnext_prog);
            code.emitLabel(lnext_prog);
            match(Tag.EOF);
            try {
                code.toJasmin();
            } catch (IOException e) {
                System.out.println("IO error\n");
            }
        }else error("procedure prog");
    }

    public void statlist(int lnext){
        if (look.tag == '(') {
            stat(lnext);
            statlistp(lnext);
        }else{
            error("procedure statlist");
        }
    }

    public void statlistp(int lnext){
        switch (look.tag){
            case '(':
                stat(lnext);
                statlistp(lnext);
                break;
            case ')':
                break;
            default:
                error("procedire statlistp");
        }
    }

    public void stat(int lnext){
        if (look.tag == '('){
            match('(');
            statp(lnext);
            match(')');
        }else error("procedure stat");
    }

    public void statp(int lnext) {
        switch(look.tag) {
            case '=':
                match('=');
                int assign_id_addr=0;
                if (look.tag==Tag.ID) {
                    assign_id_addr = st.lookupAddress(((Word) look).lexeme);
                    if (assign_id_addr == -1) {
                        assign_id_addr = count;
                        st.insert(((Word) look).lexeme, count++);
                    }
                }else
                    error("expected identifier," + look + " is not one");
                match(Tag.ID);
                expr();
                code.emit(OpCode.istore,assign_id_addr);
                break;
            case Tag.COND:
                match(Tag.COND);
                bexpr();
                stat(lnext);
                elseopt(lnext);
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                bexpr();
                stat(lnext);
                break;
            case Tag.DO:
                match(Tag.DO);
                statlist(lnext);
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                exprlist(0);
                code.emit(OpCode.invokestatic,1);
                break;
            case Tag.READ:
                match(Tag.READ);
                if (look.tag==Tag.ID) {
                    int read_id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (read_id_addr==-1) {
                        read_id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }                    
                    match(Tag.ID);
                    code.emit(OpCode.invokestatic,0);
                    code.emit(OpCode.istore,read_id_addr);   
                }
                else
                    error("Error in grammar (stat) after read with " + look);
                break;
            default:
                error("procedure statp");
        }
     }

    public void exprlist(int printSumMul){
        switch (look.tag){
            case '(':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp(printSumMul);
                break;
            default:
                error("procedure exprlist");
        }
    }

    public void exprlistp(int printSumMul) { //print 0, sum 1, mul 2
        switch (look.tag) {
            case '(':
                match('(');
            case Tag.NUM:
            case Tag.ID:
                expr();
                switch (printSumMul){
                    case 0:
                        code.emit(OpCode.invokestatic,1);
                        break;
                    case 1:
                        code.emit(OpCode.iadd);
                        break;
                    case 2:
                        code.emit(OpCode.imul);
                        break;
                    default:
                        error("exprlist dont know wat to do");
                }
                exprlistp(printSumMul);
                break;
            case ')':
                break;
            default:
                error("procedure exprlistp");
        }
    }
    public void elseopt(int lnext){
        switch (look.tag){
            case '(':
                match('(');
            case Tag.ELSE:
                match(Tag.ELSE);
                stat(lnext);
                match(')');
                break;
            case ')':
                break;
            default:
                error("procedure elseopt");
        }

    }

    public void bexpr(){
        if (look.tag == '('){
            match('(');
            bexprp();
            match(')');
        }else error("procedure bexpr");
    }

    public void bexprp (int labelFalse){
        if (look.tag==Tag.RELOP){
            String relop = ((Word)look).lexeme;
            match(Tag.RELOP);
            expr();
            expr();
            if (relop.compareTo("<")==0)
                code.emit(OpCode.if_icmpge,labelFalse);
            else if (relop.compareTo("<=")==0)
                code.emit(OpCode.if_icmpgt,labelFalse);
            else if (relop.compareTo("==")==0)
                code.emit(OpCode.if_icmpne,labelFalse);
            else if (relop.compareTo(">")==0)
                code.emit(OpCode.if_icmple,labelFalse);
            else if (relop.compareTo(">=")==0)
                code.emit(OpCode.if_icmplt,labelFalse);
            else if (relop.compareTo("<>")==0)
                code.emit(OpCode.if_icmpeq,labelFalse);
            else error("shoudn't ever appear");
        }else error("procedure bexprp");
    }

    public void expr(){
        switch (look.tag){
            case Tag.NUM:
                code.emit(OpCode.ldc,((NumberTok)look).number);
                match(Tag.NUM);
                break;
            case Tag.ID:
                code.emit(OpCode.iload,st.lookupAddress(((Word)look).lexeme));
                match(Tag.ID);
                break;
            case '(':
                match('(');
                exprp();
                match(')');
                break;
            default:
                error("procedure expr");
        }
    }

    private void exprp() {
        switch(look.tag) {
            case '+':
                match('+');
                exprlist(1);
                break;
            case '*':
                match('*');
                exprlist(1);
                break;
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            default:
                error("procedure exprp");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator trans = new Translator(lex, br);
            trans.prog();
            System.out.println("everything OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}