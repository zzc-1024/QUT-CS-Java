import java.sql.*;
import java.util.Scanner;

public class JDBC {

    public static Scanner scanner = new Scanner(System.in);

        public static Connection conn;

    public static final String url = "jdbc:mysql://localhost:3306";
    public static String user;
    public static String password;

    public static boolean connectToSQL() {
        String createStudent = """
                    CREATE TABLE `jdbc`.`student` (
                      `Sno` CHAR(9) NOT NULL,
                      `Sname` CHAR(10) NOT NULL,
                      `Sex` CHAR(2) NOT NULL,
                      `age` INT NOT NULL,
                      `Class` CHAR(10) NULL,
                      PRIMARY KEY (`Sno`));""";
        try {
            conn = DriverManager.getConnection(url + "/JDBC", user, password);
            Statement stmt = conn.createStatement();
            try {
                stmt.execute(createStudent);
            } catch (SQLException e) {
                //如果已经建表了就不需要进行额外操作
            }
        } catch (SQLSyntaxErrorException e) {//防止未建立数据库
            System.out.println(e.toString());
            try {
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException exception) {
                System.out.println("账号或密码错误，拒绝访问");
                return false;
            }
            Statement stmt;
            try {
                stmt = conn.createStatement();
                stmt.execute("CREATE DATABASE JDBC");
                stmt.execute(createStudent);
            } catch (SQLException exception) {
                System.out.println("SQL发生错误");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("账号或密码错误，拒绝访问");
            return false;
        }
        return true;
    }

    public static int query() {
        String input;
        String menu = """
                ************************
                *    Function List     *
                *    1.Add Student     *
                *    2.Change Student  *
                *    3.Delete Student  *
                *    4.Find By Sex     *
                *    5.Find By Age     *
                *    6.Find By Dept    *
                *    0.Exit            *
                ************************
                请输入指令""";
        System.out.println(menu);
        input = scanner.nextLine();
        input = input.replace(" ", "");
        try {
            return Integer.parseInt(input);
        } catch (Exception e) {
            return -1;
        }
    }

    public static void add() {
        int n;
        PreparedStatement pstmt;
        try {
            System.out.println("请输入要插入的学生数量");
            n = Integer.parseInt(scanner.nextLine().strip());
            try {
                pstmt = conn.prepareStatement("INSERT INTO STUDENT VALUES(?,?,?,?,?)");
            } catch (SQLException exception) {
                System.out.println("数据库连接异常");
                return;
            }
            while (n-- != 0) {
                String s;
                System.out.println("请输入学号");
                pstmt.setString(1, scanner.nextLine().strip());
                System.out.println("请输入姓名");
                pstmt.setString(2, scanner.nextLine().strip());
                System.out.println("请输入性别");
                s = scanner.nextLine();
                if (s.contains("男") && !s.contains("女")) {
                    s = "男";
                } else if (!s.contains("男") && s.contains("女")) {
                    s = "女";
                } else {
                    System.out.println("性别错误");
                    throw new SQLException();
                }
                pstmt.setString(3, s);
                System.out.println("请输入年纪");
                pstmt.setInt(4, Integer.parseInt(scanner.nextLine().strip()));
                System.out.println("请输入班级");
                pstmt.setString(5, scanner.nextLine().strip());
                pstmt.execute();
                System.out.println("插入完成");
            }
        } catch (SQLException exception) {
            System.out.println("在录入信息时遇见异常");
        } catch (NumberFormatException e) {
            System.out.println("输入数据非法");
        }
    }

    public static void change() {
        Statement stmt;
        int n;
        try {
            try {
                stmt = conn.createStatement();
            } catch (SQLException exception) {
                System.out.println(exception.toString());
                System.out.println("数据库连接异常");
                return;
            }
            System.out.println("请输入被修改的学生的学号");
            String set = "UPDATE STUDENT SET ";
            String where = " WHERE Sno = '" + scanner.nextLine().strip() + "'";
            String s;
            System.out.println("请输入要更新的姓名(不更新请直接回车略过)");
            s = scanner.nextLine().strip();
            if (!s.isEmpty()) {
                stmt.execute(set + "Sname = " + "'" + s + "'" + where);
            }
            System.out.println("请输入要更新的性别(不更新请直接回车略过)");
            s = scanner.nextLine().strip();
            if (s.contains("男") && !s.contains("女")) {
                stmt.execute(set + "Sex = '男'" + where);
            } else if (!s.contains("男") && s.contains("女")) {
                stmt.execute(set + "Sex = '女'" + where);
            } else if (!s.isEmpty()) {
                throw new SQLException("未知性别");
            }
            System.out.println("请输入要更新的年龄(不更新请直接回车略过)");
            s = scanner.nextLine().strip();
            if (!s.isEmpty()) {
                n = Integer.parseInt(s);
                stmt.execute(set + "Age =" + n + where);
            }
            System.out.println("请输入要更新的班级(不更新请直接回车略过)");
            s = scanner.nextLine().strip();
            if (!s.isEmpty()) {
                stmt.execute(set + "Class = " + "'" + s + "'" + where);
            }
        } catch (SQLException e) {
            //System.out.println(e.toString());
            System.out.println("错误的修改,已被拒绝");
        } catch (NumberFormatException e) {
            System.out.println("输入的数据非法");
        }
    }

    public static void del() {
        try {
            System.out.println("请输入要被删除的学号");
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM STUDENT WHERE Sno = '" + scanner.nextLine().strip() + "'");
        } catch (SQLException e) {
            System.out.println("删除失败");
        }
    }

    public static void findSex() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs;
            String tmp = "";
            while (tmp.isEmpty()) {
                System.out.println("请输入要查询的性别");
                tmp = scanner.nextLine().strip();
            }
            if (tmp.contains("男") && !tmp.contains("女")) {
                rs = stmt.executeQuery("SELECT * FROM STUDENT WHERE Sex = '男'");
            } else if (!tmp.contains("男") && tmp.contains("女")) {
                rs = stmt.executeQuery("SELECT * FROM STUDENT WHERE Sex = '女'");
            } else {
                System.out.println("性别错误");
                throw new SQLException();
            }
            while (rs.next()) {
                System.out.println("学号:" + rs.getString(1));
                System.out.println("姓名:" + rs.getString(2));
                System.out.println("性别:" + rs.getString(3));
                System.out.println("年龄:" + rs.getString(4));
                System.out.println("班级:" + rs.getString(5) + '\n');
            }
        } catch (SQLException e) {
            //System.out.println(e.toString());
            System.out.println("查询失败");
        }
    }

    public static void findAge() {
        try {
            Statement stmt = conn.createStatement();
            int low, high;
            System.out.println("请输入年纪下界");
            low = Integer.parseInt(scanner.nextLine().strip());
            if (low < 0) {
                throw new NumberFormatException();
            }
            System.out.println("请输入年纪上界");
            high = Integer.parseInt(scanner.nextLine().strip());
            if (high < 0) {
                throw new NumberFormatException();
            }
            ResultSet rs = stmt.executeQuery("SELECT * FROM STUDENT WHERE Age >= " + low + " AND Age <= " + high);
            while (rs.next()) {
                System.out.println("学号:" + rs.getString(1));
                System.out.println("姓名:" + rs.getString(2));
                System.out.println("性别:" + rs.getString(3));
                System.out.println("年龄:" + rs.getString(4));
                System.out.println("班级:" + rs.getString(5) + '\n');
            }
        } catch (SQLException e) {
            System.out.println("数据库异常");
        } catch (NumberFormatException e) {
            System.out.println("输入的数据非法");
        }
    }

    public static void findClass() {
        try {
            Statement stmt = conn.createStatement();
            System.out.println("请输入要查询的班级");
            ResultSet rs = stmt.executeQuery("SELECT * FROM STUDENT WHERE Class = '" + scanner.nextLine().strip() + "'");
            while (rs.next()) {
                System.out.println("学号:" + rs.getString(1));
                System.out.println("姓名:" + rs.getString(2));
                System.out.println("性别:" + rs.getString(3));
                System.out.println("年龄:" + rs.getString(4));
                System.out.println("班级:" + rs.getString(5) + '\n');
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public static void mainLoop(int state) {
        switch (state) {
            case 1 -> add();
            case 2 -> change();
            case 3 -> del();
            case 4 -> findSex();
            case 5 -> findAge();
            case 6 -> findClass();
            default -> System.out.println("无效指令");
        }
    }

    public static void main(String[] args) {
        try {//加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("没有找到驱动");
            System.out.println("程序结束");
        }
        System.out.println("正在连接SQL");
        do {
            System.out.println("请输入用户名");
            user = scanner.nextLine();
            System.out.println("请输入密码");
            password = scanner.nextLine();
        } while (!connectToSQL());
        System.out.println("欢迎使用");
        //询问操作
        int state = query();
        while (state != 0) {
            mainLoop(state);
            System.out.println("按回车继续");
            scanner.nextLine();
            state = query();
        }
    }
}
