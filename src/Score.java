import java.util.InputMismatchException;
import java.util.Scanner;

public class Score {
    public static void main(String[] args) {
        int n = 0;
        double[] arr = null;
        Scanner scanner = new Scanner(System.in);
        while (n <= 0)
        {
            System.out.println("请输入大于0的正整数");
            try {
                n = scanner.nextInt();
                if (n == 0) {
                    System.out.println("输入数据为0，请重新输入");
                    continue;
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();//吃掉无法输入的数据
                System.out.println("输入错误，请重新输入");
                n = 0;
                continue;
            }
            try {
                arr = new double[n];
            } catch (OutOfMemoryError error) {
                System.out.println("输入数据过大，请重新输入");
                n = 0;
            } catch (NegativeArraySizeException e) {
                System.out.println("输入数据为负数，请重新输入");
            }
        }
        System.out.println("请输入" + n + "个评分数据:");
        for (int i = 0; i < n; i++) {
            try {
                arr[i] = scanner.nextDouble();
            } catch (InputMismatchException e) {
                scanner.nextLine();//吃非法数据
                System.out.println("输入错误，请重新输入");
                i--;
            }
        }
        try {
            System.out.println("算术平均值为：" + new First().aver(arr));
            try {
                System.out.println("去极值平均值为：" + new Second().aver(arr));
            } catch (NegativeArraySizeException e) {
                System.out.println("去极值平均值至少要三组数据");
            }
        } catch (NullPointerException e) {
            System.out.println("这种错误不可能发生（认真）");
        }
    }
    interface Average {
        double aver(double[] arr);
    }
    static class First implements Average {//计算算数平均值
        @Override
        public double aver(double[] arr) {
            if (arr == null) {
                throw new NullPointerException();
            }
            double tot = 0;
            for (double i : arr) {
                tot += i;
            }
            return tot / arr.length;
        }
    }
    static class Second implements Average {//计算去极值平均值
        @Override
        public double aver(double[] arr) {
            if (arr == null) {
                throw new NullPointerException();
            }
            if (arr.length <= 2) {
                throw new NegativeArraySizeException();
            }
            double tot = 0, min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
            for (double i : arr) {
                tot += i;
                min = Double.min(i, min);
                max = Double.max(i, max);
            }
            return (tot - min - max) / (arr.length - 2);
        }
    }
}
