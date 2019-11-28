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
	    if (look.tag != Tag.EOF) move();
	} else error("syntax error");
    }

    public void start() {
	if(look.tag== '(' || look.tag==Tag.NUM){
		expr();
		match(Tag.EOF);
	} else 
		error("Error in Start");
	
    }

    private void expr() {
	if(look.tag== '(' || look.tag==Tag.NUM){
		term();
		exprp();
	}else
		error("Error in Expr");
    }

    private void exprp() {
	switch (look.tag) {
	case '+':
		match('+');
		term();
		exprp();
		break;
	case '-':
		match('-');
		term();
		exprp();
		break;
	case ')':
		break;
	case Tag.EOF:
		match(Tag.EOF);
		break;
	default:
		error("Error in Exprp");
	}
    }

    private void term() {
    if(look.tag== '(' || look.tag==Tag.NUM){
		fact();
		termp();
	} else 
		error("Error in Term");
    }

    private void termp() {
    switch(look.tag){
	case '*':
		match('*');
		fact();
		termp();
		break;
	case '/':
		match('/');
		fact();
		termp();
		break;
	case Tag.EOF:
		match(Tag.EOF);
		break;
	case '+':
	case '-':
	case ')':
		break;
	default:
			error("Error in Termp");
	}
	}

    private void fact() {
    switch(look.tag){
			case '(':
				match('(');
				expr();
				match(')');
				break;
			case Tag.NUM:
				match(Tag.NUM);
				break;
			default:
				error("Error in fact");
				
	}
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}