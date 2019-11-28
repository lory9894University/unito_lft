public class es1_7 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;


        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch!='a' && ch !='b')
                        state = -1;
                    if (ch=='a')
                        state=3;
                    else state=1;
                    break;
                case 1:
                    if (ch!='a' && ch !='b')
                        state = -1;
                    if (ch=='a')
                        state=3;
                    else state=2;
                    break;
                case 2:
                    if (ch!='a' && ch !='b')
                        state = -1;
                    if (ch=='a')
                        state=3;
                    else state=-1;
                    break;
		case 3:
		    if (ch!='a' && ch !='b')
                        state = -1;
		    break;
            }
        }
        return state == 3;
    }
    public static void main(String[] args) {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }

}