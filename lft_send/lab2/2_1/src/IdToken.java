public class IdToken extends Token {
    String name = "";

    public IdToken(int t, String name) {
        super(t);
        this.name = name;
    }

    @Override
    public String toString() {
        return "<" + tag + "," + name + ">";
    }
}
