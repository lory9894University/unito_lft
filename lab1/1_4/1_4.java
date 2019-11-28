import javax.swing.*;

public class LFTMatrix {
    public static boolean scan(String s)
    {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (Character.isDigit(ch)) {
                        if (ch%2==1)
                            state=1;
                        else
                            state=2;
                    }else
                        state=-1;
                    break;
                case 1://dispari
                    if (Character.isDigit(ch)) {
                        if (ch%2==1)
                            state=1;
                        else
                            state=2;
                    }else if(ch==' '){
                        state=3;
                    }else if(ch>='L' && ch<='Z')
                        state=5;
                    else
                        state=-1;
                    break;
                case 2://pari
                    if (Character.isDigit(ch)) {
                        if (ch%2==1)
                            state=1;
                        else
                            state=2;
                    }else if(ch==' '){
                        state=4;
                    }else if(ch>='A' && ch<='K')
                        state=5;
                    else
                        state=-1;
                    break;
                case 3://space dispari
                    if(ch==' '){
                        state=3;
                    }else if(ch>='L' && ch<='Z')
                        state=5;
                    else
                        state=-1;
                    break;
                case 4://space pari
                    if(ch==' '){
                        state=4;
                    }else if(ch>='A' && ch<='K')
                        state=5;
                    else
                        state=-1;
                    break;
                case 5:
                    if (Character.isAlphabetic(ch)){
                        state=5;
                    }else if(ch == ' ')
                        state=6;
                    else
                        state=-1;
                    break;
                case 6:
                    if (ch>='A' && ch<='Z')
                        state=5;
                    else
                        state=-1;
                    break;
            }
        }
        return state == 5;
    }
    public static void main(String[] args) {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
