public class es1_9 {
    public static boolean scan(String s,String myname) {
        myname=myname.toLowerCase();
        s=s.toLowerCase();
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i);
            final char mych = myname.charAt(i);
            if (ch<0x20 || ch >0x7E)
                state = -1;

            switch (state) {
                case 0:
                    if (ch==mych)
                        state=0;
                    else state=1;
                    break;
                case 1://one strike
                    if (ch==mych)
                        state=1;
                    else state=-1;
                    break;
            }
            i++;
        }
        return state == 0 || state == 1;
    }
    public static void main(String[] args) {
        System.out.println(scan(args[0],"Lorenzo") ? "OK" : "NOPE");
    }
}
