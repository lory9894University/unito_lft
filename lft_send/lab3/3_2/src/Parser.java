import Lexer.*;

import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
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
            stat();
            match(Tag.EOF);
	        }
        else error("procedure prog");
    }

    public void statlist(){
        if (look.tag == '(') {
            stat();
            statlistp();
        }else{
            error("procedure statlist");
        }
    }

    public void statlistp(){
        switch (look.tag){
            case '(':
                stat();
                statlistp();
                break;
            case ')':
                break;
            default:
                error("procedire statlistp");
        }
    }

    public void stat(){
        if (look.tag == '('){
            match('(');
            statp();
            match(')');
        }else error("procedure stat");
    }

    public void statp(){
        switch (look.tag){
            case '=':
                match('=');
                match(Tag.ID);
                expr();
                break;
            case Tag.COND:
                match(Tag.COND);
                bexpr();
                stat();
                elseopt();
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                bexpr();
                stat();
                break;
            case Tag.DO:
                match(Tag.DO);
                statlist();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                exprlist();
                break;
            case Tag.READ:
                match(Tag.READ);
                match(Tag.ID);
                break;
            default:
                error("procedure statp");
        }
    }

    public void elseopt(){
        switch (look.tag){
            case '(':
                match('(');
            case Tag.ELSE:
                match(Tag.ELSE);
                stat();
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

    public void bexprp (){
        if (look.tag==Tag.RELOP){
            match(Tag.RELOP);
            expr();
            expr();
        }else error("procedure bexprp");
    }

    public void expr(){
        switch (look.tag){
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case Tag.ID:
                match(Tag.ID);
                break;
            case '(':
                match('(');
                exprp();
                match(')');//todo non sono sicuro che non sia ridondante questo controllo
                break;
            default:
                error("procedure expr");
        }
    }

    public void exprp(){
        switch (look.tag){
            case '+':
                match('+');
                exprlist();
                break;
            case '*':
                match('*');
                exprlist();
                break;
            case '-':
                match('-');
                expr();
                expr();
            case '/':
                match('/');
                expr();
                expr();
                break;
        }
    }

    public void exprlist(){
        switch (look.tag){
            case '(':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp();
                break;
            default:
                error("procedure exprlist");
        }
    }

    public void exprlistp(){
        switch (look.tag){
            case '(':
                match('(');
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp();
                break;
            case ')':
                break;
            default:
                error("procedure exprlistp");
        }

    }
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}