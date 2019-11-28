public class es1_11 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i);

            if (ch != '/' && ch != '*' && ch!='a')
                state = -1;

                switch (state) {
                    case 0:
                        if (ch=='/')
                            state=1;
                        else state=0;
                        break;
                    case 1:
                        if (ch=='*')
                            state=2;
                        else if (ch=='/')
                            state=1;
                        else state=0;
                        break;
                    case 2:
                        if (ch == '*')
                            state=3;
                        else state=2;
                        break;
                    case 3:
                        if (ch=='/')
                            state=4;
                        else if(ch=='*')
                            state=3;
                        else state=2;
                    case 4:
                        if (ch=='/')
                            state=1;
                        state=4;
                        break;
                }
            i++;
        }
        return state == 4 || state==0 || state ==1;
    }
    public static void main(String[] args) {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}