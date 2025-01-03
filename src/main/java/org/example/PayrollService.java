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

            System.out.println("\nUC 2: Retrieve Employee Payroll Data");
            payrollService.retrieveEmployeePayrollData();

            System.out.println("\nUC 3: Update Salary for Employee Terisa");
            payrollService.updateSalary("Terisa", 3000000.00);

            System.out.println("\nUC 4: Update Salary with PreparedStatement");
            payrollService.updateSalaryWithPreparedStatement("Terisa", 3000000.00);

            System.out.println("\nUC 5: Retrieve Employees by Date Range");
            payrollService.retrieveEmployeesByDateRange("2023-01-01", "2023-12-31");

            System.out.println("\nUC 6: Aggregate Analysis by Gender");
            payrollService.aggregateAnalysisByGender();


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
    // UC 2: Retrieve Employee Payroll Data
    public void retrieveEmployeePayrollData() throws SQLException {
        List<EmployeePayroll> employeePayrollList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM employee_payroll")) {

            while (resultSet.next()) {
                EmployeePayroll employee = new EmployeePayroll(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("salary"),
                        resultSet.getDate("start_date"));
                employeePayrollList.add(employee);
            }
        }
        employeePayrollList.forEach(System.out::println);
    }
    // UC 3: Update Salary for Employee Terisa
    public void updateSalary(String name, double salary) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            String updateQuery = "UPDATE employee_payroll SET salary = " + salary + " WHERE name = '" + name + "'";
            int rowsAffected = statement.executeUpdate(updateQuery);
            System.out.println("Rows Updated: " + rowsAffected);
        }
    }

    // UC 4: Update Salary with PreparedStatement
    public void updateSalaryWithPreparedStatement(String name, double salary) throws SQLException {
        String updateQuery = "UPDATE employee_payroll SET salary = ? WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setDouble(1, salary);
            preparedStatement.setString(2, name);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows Updated: " + rowsAffected);
        }
    }

    // UC 5: Retrieve Employees by Date Range
    public void retrieveEmployeesByDateRange(String startDate, String endDate) throws SQLException {
        String query = "SELECT * FROM employee_payroll WHERE start_date BETWEEN ? AND ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println(new EmployeePayroll(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("salary"),
                            resultSet.getDate("start_date")));
                }
            }
        }
    }


    // UC 6: Aggregate Analysis by Gender
    public void aggregateAnalysisByGender() throws SQLException {
        String query = "SELECT gender, SUM(salary) as TotalSalary, AVG(salary) as AvgSalary, " +
                "MIN(salary) as MinSalary, MAX(salary) as MaxSalary, COUNT(*) as Count " +
                "FROM employee_payroll GROUP BY gender";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double totalSalary = resultSet.getDouble("TotalSalary");
                double avgSalary = resultSet.getDouble("AvgSalary");
                double minSalary = resultSet.getDouble("MinSalary");
                double maxSalary = resultSet.getDouble("MaxSalary");
                int count = resultSet.getInt("Count");

                System.out.println("Gender: " + gender +
                        ", Total Salary: " + totalSalary +
                        ", Avg Salary: " + avgSalary +
                        ", Min Salary: " + minSalary +
                        ", Max Salary: " + maxSalary +
                        ", Count: " + count);
            }
        }
    }

}
