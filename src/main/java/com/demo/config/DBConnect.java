package com.demo.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
    private static final String URL = "jdbc:mysql://localhost:3306/bt21?useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "12345";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Thêm hàm main để test kết nối
    public static void main(String[] args) {
        Connection conn = getConnection();

        if (conn != null) {
            System.out.println("Kết nối MySQL thành công!");
        } else {
            System.out.println("Kết nối MySQL thất bại!");
        }
    }
}
