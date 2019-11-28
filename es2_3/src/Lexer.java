import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.print(tok + " ");
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads the next character and saves it in peek
     *
     * @param br buffer
     **/
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }
    private boolean comments(BufferedReader br){
        readch(br);
        if (peek == '/'){
            peek = ' ';
            do {
                readch(br);
                if (peek == '\n') {
                    System.out.println();
                    line++;
                }
            }
            while (peek != (char)-1 && peek != '\n');
            readch(br);
        }else if (peek == '*'){
            boolean asterics=false;
            peek = ' ';
            do {
                asterics= peek == '*';
                readch(br);
            }
            while (!(asterics && peek=='/'));
            readch(br);
        }else {
            return false;
        }
        return true;
    }
    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            readch(br);
            if (peek == '\n') {
                System.out.println();
                line++;
            }
            if(peek == '/'){
                if (!comments(br)) return Token.div;
            }
        }

        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case ';':
                peek = ' ';
                return Token.semicolon;
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    return Token.assign;
                }
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : " + peek);
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : " + peek);
                    return null;
                }
            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    peek = ' ';
                    return Word.lt;
                }
            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    peek = ' ';
                    return Word.gt;
                }

            case (char) -1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek)) {
                    StringBuffer id = new StringBuffer();
                    while (Character.isLetter(peek) || peek == '_') {
                        id.append(peek);
                        readch(br);
                    }
                    if (id.toString().compareTo("cond") == 0)
                        return Word.cond;
                    if (id.toString().compareTo("when") == 0)
                        return Word.when;
                    if (id.toString().compareTo("then") == 0)
                        return Word.then;
                    if (id.toString().compareTo("else") == 0)
                        return Word.elsetok;
                    if (id.toString().compareTo("while") == 0)
                        return Word.whiletok;
                    if (id.toString().compareTo("seq") == 0)
                        return Word.seq;
                    if (id.toString().compareTo("print") == 0)
                        return Word.print;
                    if (id.toString().compareTo("read") == 0)
                        return Word.read;
                    if (id.toString().compareTo("do") == 0)
                        return Word.dotok;

                    Pattern p = Pattern.compile("^_.*");
                    if (p.matcher(id.toString()).matches()) {
                        System.err.println("Erroneous identifier: "
                                + id.toString());
                    }

                    return new IdToken(Tag.ID, id.toString());

                } else if (Character.isDigit(peek)) { //todo: this can't recognize anything but integer
                    StringBuffer number = new StringBuffer();
                    while (Character.isDigit(peek)) {
                        number.append(peek);
                        readch(br);
                    }
                    return new NumberTok(Tag.NUM, number.toString());
                } else {
                    System.err.println("Erroneous character: "
                            + peek);
                    return null;
                }
        }
    }

}
