import java.util.Scanner;

public class Money {
    static char[] num = {'零','壹','贰','叁','肆','伍','陆','柒','捌','玖','拾'};
    static char ne1 = '拾',ne2 = '百',ne3 = '千',ne4 = '万',ne8 = '亿';
    /*
    测试数据
    10.05
    1234567890
    100101
    0.12
    0.02
    1001
     */
    public static void main(String[] args) {
        String money,tmp,end,ans;
        tmp = "";
        end = "";
        ans = "元";
        Scanner scanner = new Scanner(System.in);
        int len;

        money = scanner.nextLine();
        len = money.length();
        if(len > 2)
        {
            tmp = money.substring(len - 3, len);
            for(int i = 0 ;i < 3;i++)
                if(tmp.charAt(i)=='.')
                {
                    if(i == 0)
                    {
                        money = money.substring(0,len - 3);
                        if((byte)tmp.charAt(1) - (byte)'0' != 0)
                        {
                            end += num[(byte)tmp.charAt(1) - (byte)'0'];
                            end += "角";
                        }
                        end += num[(byte)tmp.charAt(2) - (byte)'0'];
                        end += "分";
                        break;
                    }
                    if(i == 1)
                    {
                        money = money.substring(0,len - 2);
                        end += num[(byte)tmp.charAt(2) - (byte)'0'];
                        end += "角";
                        break;
                    }
                    if(i == 2)
                    {
                        System.out.println("小数点错误");
                        return;
                    }
                }
        }
        len = money.length();
        int bit = 0;

        while (money.length() != 0)
        {
            String looper = "";

            if (money.length() > 4){
                tmp = money.substring(money.length()-4,money.length());
                money = money.substring(0,money.length() - 4);
            }else {
                tmp = money;
                money = "";
            }

            if (tmp.charAt(tmp.length() - 1) != '0'){
                looper = "" + num[(byte)tmp.charAt(tmp.length() - 1) - (byte) '0'] + looper;
            }else {
                looper = num[0] + looper;
            }
            bit++;

            if (tmp.length() > 1){
                if (tmp.charAt(tmp.length() - 2) != '0') {
                    if (looper.charAt(0) == num[0])
                        looper = "";
                    looper = "" + num[(byte)tmp.charAt(tmp.length() - 2) - (byte) '0'] + ne1 + looper;
                }else if (looper.charAt(0) != num[0]) {
                    looper = num[0] + looper;
                }
                bit++;
            }

            if (tmp.length() > 2)   {
                if (tmp.charAt(tmp.length() - 3) != '0') {
                    if (looper.charAt(0) == num[0] && looper.length() == 1)
                        looper = "";
                    looper = "" + num[(byte)tmp.charAt(tmp.length() - 3) - (byte) '0'] + ne2 + looper;
                }else if (looper.charAt(0) != num[0]) {
                    looper = num[0] + looper;
                }
                bit++;
            }

            if (tmp.length() > 3)   {
                if (tmp.charAt(0) != '0'){
                    if (looper.charAt(0) == num[0] && looper.length() == 1)
                        looper = "";
                    looper = "" + num[(byte)tmp.charAt(0) - (byte) '0'] + ne3 + looper;
                }else if (looper.charAt(0) != num[0]) {
                    looper = num[0] + looper;
                }
                bit++;
            }

            if(looper.length() > 1 || looper.charAt(0) != num[0])
               if (bit - tmp.length() == 4)
                    looper += ne4;
                else if (bit - tmp.length() == 8)
                    looper += ne8;

            if (ans.charAt(0) != num[0] || looper.charAt(looper.length() - 1) != num[0])
                ans = looper + ans;

        }

        if(bit%4 == 2)
            if(ans.charAt(0) == num[1])
                ans = ans.substring(1,ans.length());
        if(end.length() == 0)
            ans += "整";
        else
            ans += end;
        System.out.println(ans);
    }
}
