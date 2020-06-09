package Lexer;

public class NumberTok extends Token {
    public int number = 0;

    public NumberTok(int t, String number) {
        super(t);
        this.number = Integer.parseInt(number);
    }

    @Override
    public String toString() {
        return "<" + tag + "," + number + ">";
    }
}
