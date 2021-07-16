import java.util.Random;
import java.util.Scanner;

public class guess {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int count,ans,tmp;
        count = 0;
        ans = random.nextInt(100)+1;
        System.out.println("请猜一个1到100之间的整数");
        do {
            tmp = scanner.nextInt();
            if(tmp<ans)
                System.out.println("小了");
            else if(tmp>ans)
                System.out.println("大了");
            else
                System.out.println("猜对了");
            count++;
        }while (tmp!=ans);
        System.out.println("共猜了"+count+"次");
    }
}
