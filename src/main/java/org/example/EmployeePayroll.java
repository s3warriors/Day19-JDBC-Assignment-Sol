package org.example;

import java.sql.Date;

class EmployeePayroll {
    int id;
    String name;
    double salary;
    Date startDate;

    public EmployeePayroll(int id, String name, double salary, Date startDate) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "EmployeePayroll{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", startDate=" + startDate +
                '}';
    }
}
