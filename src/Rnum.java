import java.util.Scanner;

public class Rnum {

    private int num;    //分子
    private int den;    //分母

    Rnum() {
        this.num = 1;
        this.den = 1;
    }

    Rnum(int num) {
        this.num = num;
        this.den = 1;
    }

    Rnum(int num, int den) {
        this.num = num;
        if (den == 0) {
            throw new ArithmeticException();
        }
        this.den = den;
        this.reduce();
    }

    public void setVal(int num, int den) {
        if (den == 0) {
            throw new ArithmeticException();
        }
        this.num = num;
        this.den = den;
        this.reduce();
    }

    public void setNum(int num) {
        this.setVal(num, this.den);
    }

    public int getNum() {
        return this.num;
    }

    public void setDen(int den) {
        this.setVal(this.num, den);
    }

    public int getDen() { return this.den; }

    public Rnum add(Rnum _val) {
        return new Rnum(this.getNum() * _val.getDen() + _val.getNum() * this.getDen(), this.getDen() * _val.getDen());
    }

    public Rnum add(int _val) {
        return this.add(new Rnum(_val));
    }

    public Rnum sub(Rnum _val) {
        return this.add(new Rnum(-_val.getNum(), _val.getDen()));
    }

    public Rnum sub(int _val) {
        return this.sub(new Rnum(_val));
    }

    public Rnum mul(Rnum _val) {
        return new Rnum(this.getNum() * _val.getNum(), this.getDen() * _val.getDen());
    }

    public Rnum mul(int _val) {
        return new Rnum(this.getNum() * _val, this.getDen());
    }

    public Rnum div(Rnum _val) {
        return this.mul(new Rnum(_val.getDen(), _val.getNum()));
    }

    public Rnum div(int _val) {
        return this.div(new Rnum(_val));
    }

    private int gcd(int _a, int _b) {
        return _b != 0? gcd(_b,_a % _b): _a;
    }

    private void reduce() {
        if (this.getNum() == 0) {
            this.den = 1;
            return;
        }
        if (this.getDen() < 0) {
            this.den = -this.den;
            this.num = -this.num;
        }
        int _tmp = gcd(Math.abs(this.getNum()), this.getDen());
        this.num /= _tmp;
        this.den /= _tmp;
    }

    @Override
    public String toString() {
        return "" + this.getNum() + (this.getDen() == 1? "": "/" + this.getDen());
    }

    public static Rnum getR() {
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean isFloat = false;
        input = scanner.nextLine();
        input = input.replace(" ", "");
        if (input.charAt(0) == '/' || input.charAt(input.length() - 1) == '/') {
           System.out.println("除号位置错误");
           return null;
        }
        for (int i = 0; i < input.length(); i++) {
            if (i == 0 && input.charAt(0) == '-') {
                if (input.length() == 1) {
                    System.out.println("负号后面缺少表达式");
                    return null;
                } else if (input.charAt(1) - '0' < 0 || input.charAt(1) -'0' > 9) {
                    if (input.charAt(1) == '/') {
                       System.out.println("非法运算符组合");
                    } else {
                        System.out.println("输入存在非法字符");
                    }
                    return null;
                }
                i++;
            } else if (input.charAt(i) == '/') {
                if (isFloat) {
                    System.out.println("多次输入除号");
                    return null;
                }
                isFloat = true;
                if (input.charAt(i + 1) == '-') {
                    i++;
                    if (i + 1 == input.length()) {
                        System.out.println("负号后面缺少表达式");
                        return null;
                    }
                }
            } else if ((int)input.charAt(i) - '0' < 0 || (int)input.charAt(i) - '0' > 9) {
                System.out.println("输入存在非法字符");
                return null;
            }
        }

        int num = 0;
        int den = 0;

        int it = 0;

        if (!isFloat)
            return new Rnum(Integer.parseInt(input));
        else {
            int sign = 1;
            if (input.charAt(it) == '-') {
                it++;
                sign = -1;
            }
            while (it < input.length()) {
                if (input.charAt(it) == '/') {
                    it++;
                    sign = 1;
                    if (input.charAt(it) == '-') {
                        sign = -1;
                        it++;
                    }
                    while (it < input.length()) {
                        den *= 10;
                        den += sign * ((int) input.charAt(it) - '0');
                        it++;
                    }
                    break;
                }
                num *= 10;
                num += sign * ((int) input.charAt(it) - '0');
                it++;
            }
        }
        return new Rnum(num, den);
    }

    public static void main(String[] args) {
        Rnum rnum1;
        Rnum rnum2;
        do {
            System.out.println("请输入第一个操作数");
            try {
                rnum1 = getR();
            } catch (ArithmeticException e) {
                rnum1 = null;
                System.out.println("分母不能为0");
            }
        } while (rnum1 == null);
        System.out.println("输入结果为" + rnum1);
        do {
            System.out.println("请输入第二个操作数");
            try {
                rnum2 = getR();
            } catch (ArithmeticException e) {
                rnum2 = null;
                System.out.println("分母不能为0");
            }
        } while (rnum2 == null);
        System.out.println("输入结果为" + rnum2);

        System.out.println("加法结果为：" + rnum1.add(rnum2));
        System.out.println("减法结果为：" + rnum1.sub(rnum2));
        System.out.println("乘法结果为：" + rnum1.mul(rnum2));
        try {
            System.out.println("除法结果为：" + rnum1.div(rnum2));
        } catch (ArithmeticException e) {
            System.out.println("除法结果为：NaN");
        }
    }

}
