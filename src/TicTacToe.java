import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    static char[][] loc = new char[3][3];
    static int remains = 9;

    final static char player = '*';
    static final char robot  = 'O';

    static Scanner scanner = new Scanner(System.in);

    static void draw() {
        System.out.println("\\y---->");
        System.out.println("x\\0 1 2");
        System.out.println("|0"+loc[0][0]+"|"+loc[0][1]+"|"+loc[0][2]);
        System.out.println("| -+-+-");
        System.out.println("|1"+loc[1][0]+"|"+loc[1][1]+"|"+loc[1][2]);
        System.out.println("| -+-+-");
        System.out.println("V2"+loc[2][0]+"|"+loc[2][1]+"|"+loc[2][2]);
    }

    static char judge() {
        for (int i = 0 ;i < 3;i++){
            if(loc[i][0] == loc[i][1] && loc[i][1] == loc[i][2]){
                return loc[i][0];
            }
        }
        for (int i = 0 ;i < 3;i++) {
            if(loc[0][i] == loc[1][i] && loc[1][i] == loc[2][i]){
                return loc[0][i];
            }
        }
        if (loc[0][0] == loc[1][1] && loc[1][1] == loc[2][2])
            return loc[1][1];
        if (loc[0][2] == loc[1][1] && loc[1][1] == loc[2][0])
            return loc[1][1];
        return 0;
    }

    public static void main(String[] args) {
        boolean isPlayer = false;
        int tmp;
        char flag;
        Random random = new Random();

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3 ; j++)
                loc[i][j] = ' ';

        while (remains > 0) {
            tmp = remains;
            if (!isPlayer) {
                for (int i = 0 ;i < 9; i++) {
                    if (loc[i / 3][i % 3] == ' ') {
                        if (random.nextInt(tmp) == 0){
                            loc[i / 3][i % 3] = robot;
                            break;
                        }else {
                            tmp--;
                        }
                    }
                }
            }else {
                int x , y;
                do {
                    do {
                        System.out.println("请输入x坐标");
                        x = scanner.nextInt();
                    } while (x < 0 || x > 2);
                    do {
                        System.out.println("请输入y坐标");
                        y = scanner.nextInt();
                    } while (y < 0 || y > 2);
                }while (loc[x][y] != ' ');
                loc[x][y] = player;
            }
            if (!isPlayer)
                draw();
            flag = judge();
            if (flag == robot) {
                System.out.println("电脑获胜");
                return;
            }
            else if (flag == player) {
                if (isPlayer)
                    draw();
                System.out.println("玩家获胜");
                return;
            }
            remains--;
            isPlayer = !isPlayer;
        }
        System.out.println("和棋");
    }
}
