package com.kuang.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

// 操作数据库的公共类
public class BaseDao {

    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    // 静态代码块，类加载时就运行
    static {
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");

        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }

    // 获取数据库的连接
    public static Connection getConnection(){
        Connection connection = null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    // 编写查询公共类
    public static ResultSet excute(Connection conn, PreparedStatement preparedStatement, String sql, Object[] args, ResultSet rs) throws SQLException {
        preparedStatement = conn.prepareStatement(sql);

        for(int i = 0; i < args.length; i++){
            preparedStatement.setObject(i+1, args[i]);
        }

        rs = preparedStatement.executeQuery();
        return rs;
    }

    // 编写增删改公共类
    public static int excute(Connection conn, PreparedStatement preparedStatement, String sql, Object[] args) throws SQLException {
        preparedStatement = conn.prepareStatement(sql);

        for(int i = 0; i < args.length; i++){
            preparedStatement.setObject(i+1, args[i]);
        }

        int i = preparedStatement.executeUpdate();
        return i;
    }

    // 关闭连接
    public static boolean destory(Connection conn, PreparedStatement preparedStatement, ResultSet rs){
        boolean flag = true;

        if(rs != null){
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if(preparedStatement != null){
            try {
                preparedStatement.close();
                preparedStatement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if(conn != null){
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }

        return flag;
    }

}
