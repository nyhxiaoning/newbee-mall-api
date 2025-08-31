package ltd.newbee.demo;

import java.sql.*;

public class userDemoSql {

    // 生命本类的全局的变量
    private static Connection conn;
    private static Statement stmt;

    // 数据库连接加载驱动
    static {
        try {
            System.out.println("开始加载数据库驱动...");
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("数据库驱动加载成功...");
            System.out.println("开始连接数据库...");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javatest", "root", "nyh123");
            // 创建Statement对象：创建一个SQL语句执行对象
            stmt = conn.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // 查询用户
    public static void queryAllUser() throws  SQLException {
        String sql = "select * from user";
        System.out.println("开始查询所有用户...");
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println("用户ID：" + rs.getInt("id"));
            System.out.println("用户名：" + rs.getString("name"));
            System.out.println("用户age：" + rs.getString("age"));
        }

    }

    public static void queryUserById(int id) throws SQLException {
        String sql = "select * from user where id = " + id;
        System.out.println("开始查询用户...");
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println("用户ID：" + rs.getInt("id"));
            System.out.println("用户名：" + rs.getString("name"));
            System.out.println("用户age：" + rs.getString("age"));
        }
    }

    public static void queryUserByName(String name) throws SQLException {
        String sql = "select * from user where name = '" + name + "'";
        System.out.println("开始查询用户...");
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println("用户ID：" + rs.getInt("id"));
            System.out.println("用户名：" + rs.getString("username"));
            System.out.println("用户密码：" + rs.getString("password"));
        }
    }


    public static void main(String[] args) {
        //
        try {
            queryAllUser();
            queryUserById(1);
            queryUserByName("张三");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
