package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollService {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        try {
            PayrollService payrollService = new PayrollService();

            System.out.println("UC 1: Database Connection Test");
            payrollService.testDatabaseConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UC 1: Test Database Connection
    public void testDatabaseConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            System.out.println("Connection Established Successfully.");
            DriverManager.drivers().forEach(driver -> System.out.println("Driver: " + driver));
        }

    }
}
