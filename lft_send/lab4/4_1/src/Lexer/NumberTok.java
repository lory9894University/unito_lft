package Lexer;

public class NumberTok extends Token {
    int number = 0;

    public NumberTok(int t, String number) {
        super(t);
        this.number = Integer.parseInt(number);
    }

    public int getNumber(){
    	return number;
    }

    @Override
    public String toString() {
        return "<" + tag + "," + number + ">";
    }
}
