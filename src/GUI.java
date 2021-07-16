import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class GUI extends JFrame {
    public static final String url = "jdbc:mysql://localhost:3306";
    public static Scanner scanner = new Scanner(System.in);
    public String user = "";
    public String password = "";
    public String[] Quest = new String[5];
    public String[][] option = new String[5][4];
    public int[] ans = new int[5];
    public static Connection conn;

    JTextField text = new JTextField();
    ButtonGroup[] groups = new ButtonGroup[5];

    static {
        try {//加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("没有找到数据库驱动");
            System.out.println("程序结束");
            System.exit(1);
        }
    }

    {
        String createTable = """
                CREATE TABLE `Question`.`question` (
                  `Question` VARCHAR(255) NOT NULL,
                  `A` VARCHAR(255) NULL,
                  `B` VARCHAR(255) NULL,
                  `C` VARCHAR(255) NULL,
                  `D` VARCHAR(255) NULL,
                  `ANS` INT UNSIGNED NULL,
                  PRIMARY KEY (`Question`));""";
        boolean remake = true;
        while (remake) {
            System.out.println("请输入用户名");
            user = scanner.nextLine();
            System.out.println("请输入密码");
            password = scanner.nextLine();
            try {
                conn = DriverManager.getConnection(url + "/Question", user, password);
                remake = false;
            } catch (SQLSyntaxErrorException e) {
                //e.printStackTrace();
                System.out.println("未建立数据库");
                try {
                    conn = DriverManager.getConnection(url, user, password);
                    Statement stmt = conn.createStatement();
                    stmt.execute("CREATE DATABASE Question");
                    stmt.execute(createTable);
                    System.out.println("建立数据库完成");
                    remake = false;
                } catch (SQLSyntaxErrorException throwable) {
                    System.out.println("连接失败");
                    System.out.println("程序结束");
                    System.exit(1);
                } catch (SQLException throwable) {
                    System.out.println("账号或密码错误");
                    throwable.printStackTrace();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
                System.out.println("账号或密码错误");
            }
        }
    }

    GUI() {
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(12,1));
        this.setBounds(100,100,800,600);
        var font = new Font("宋体", Font.PLAIN, 24);
        this.setFont(font);
        try {
            Statement stmt = conn.createStatement();
            int remain = 0;
            ResultSet rs   = stmt.executeQuery("select * from question;");
            while (rs.next()) {
                remain++;
            }
            rs = stmt.executeQuery("select * from question;");
            if (remain < 5) {
                System.out.println("题目数量不足");
                System.out.println(remain);
                System.exit(1);
            }
            int selected= 5;
            Random random = new Random();
            while (selected != 0) {
                rs.next();
                if (random.nextInt() % remain < selected) {
                    Quest[--selected] = rs.getString(1);
                    for (int i = 0; i < 4; i++) {
                        option[selected][i] = rs.getString(2 + i);
                    }
                    ans[selected] = rs.getInt(6);
                }
                remain--;
            }
        } catch (SQLException e) {
            System.out.println("程序异常");
            System.out.println("程序结束");
            System.out.println(e);
            System.exit(1);
        }

        var labels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            labels[i] = new JLabel(Quest[i]);
        }

        var panels = new JPanel[5];
        var radioButton = new JRadioButton[5][4];
        for (int i = 0; i < 5; i++) {
            groups[i] = new ButtonGroup();
            labels[i].setFont(font);
            this.add(labels[i]);
            panels[i] = new JPanel();
            panels[i].setLayout(new GridLayout(1, 4));
            for (int j = 0; j < 4; j++) {
                radioButton[i][j] = new JRadioButton(option[i][j]);
                groups[i].add(radioButton[i][j]);
                panels[i].add(radioButton[i][j]);
            }
            this.add(panels[i]);
        }
        var panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        var remake = new JButton("重新作答");
        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                for (var tmp : groups) {
                    tmp.clearSelection();
                }
                try {
                    Statement stmt = conn.createStatement();
                    int remain = 0;
                    ResultSet rs   = stmt.executeQuery("select * from question;");
                    while (rs.next()) {
                        remain++;
                    }
                    rs = stmt.executeQuery("select * from question;");
                    if (remain < 5) {
                        System.out.println("题目数量不足");
                        System.out.println(remain);
                        System.exit(1);
                    }
                    int selected= 5;
                    Random random = new Random();
                    while (selected != 0) {
                        rs.next();
                        if (random.nextInt() % remain < selected) {
                            Quest[--selected] = rs.getString(1);
                            for (int i = 0; i < 4; i++) {
                                option[selected][i] = rs.getString(2 + i);
                            }
                            ans[selected] = rs.getInt(6);
                        }
                        remain--;
                    }
                } catch (SQLException throwable) {
                    System.out.println("程序异常");
                    System.out.println("程序结束");
                    System.out.println(throwable.toString());
                    System.exit(1);
                }
                for (int i = 0; i < 5; i++) {
                    labels[i] = new JLabel(Quest[i]);
                }
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 4; j++) {
                        radioButton[i][j] = new JRadioButton(option[i][j]);
                    }
                }
            }
        };
        remake.addMouseListener(listener);
        var submit = new JButton("提交");
        listener = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int score = 0;
                for (int i = 0; i < 5; i++) {
                    if (radioButton[i][ans[i]].isSelected()) {
                        score++;
                    }
                }
                text.setText("得分为" + score);
            }
        };
        submit.addMouseListener(listener);
        panel.add(remake);
        panel.add(submit);
        this.add(panel);
        this.add(text);
    }

    public static void main(String[] args) {
        new GUI();
    }
}
