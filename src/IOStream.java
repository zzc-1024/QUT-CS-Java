import java.io.*;

public class IOStream {
    static byte[] map = new byte[10];
    public static void main(String[] args) {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File("").getCanonicalPath() + "/a.txt");
        } catch (FileNotFoundException e) {
            System.out.println("不存在要加密的文件");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("系统权限不足");
            System.exit(1);
        }
        try {
            outputStream = new FileOutputStream(new File("").getCanonicalPath() + "/b.txt");
        } catch (IOException e) {
            System.out.println("系统权限不足");
            System.exit(1);
        }
        map[0] = '1';
        map[1] = '0';
        for (int i = 2; i < 10; i++) {
            map[i] = (byte) ('2' + 9 - i);
        }
        try {
            byte b;
            while ( (b = (byte) inputStream.read()) != -1) {
                if (b >= '0' && b <= '9') {
                    b = map[b - '0'];
                } else if (b >= 'A' && b < 'Z' || b >= 'a' && b < 'z') {
                    b++;
                } else if (b == 'Z' || b == 'z') {
                    b -= 25;
                }
                outputStream.write(b);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }   //main
}
