package org.bklab.autoentity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SuperDao {
    private static String url = "jdbc:mysql://47.104.71.124:3306/zyan?useSSL=false&useUnicode=true&character=utf-8";
    private static String user = "root";
    private static String pwd = "12345678";
    private static Connection con;

    private SuperDao() {
    }

    /**
     * 建立连接
     *
     * @return
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * 关闭连接
     */
    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
