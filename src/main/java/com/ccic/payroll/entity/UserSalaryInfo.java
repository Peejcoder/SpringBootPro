package com.ccic.payroll.entity;

public class UserSalaryInfo {
    private int id;
    private String workMonth;
    private String userName;
    private String realName;
    private int orgId;
    private double salary1;
    private double salary2;
    private double salary3;
    private double salary4;
    private double salary5;
    private double salary6;
    private double totalSalary;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkMonth() {
        return workMonth;
    }

    public void setWorkMonth(String workMonth) {
        this.workMonth = workMonth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public double getSalary1() {
        return salary1;
    }

    public void setSalary1(double salary1) {
        this.salary1 = salary1;
    }

    public double getSalary2() {
        return salary2;
    }

    public void setSalary2(double salary2) {
        this.salary2 = salary2;
    }

    public double getSalary3() {
        return salary3;
    }

    public void setSalary3(double salary3) {
        this.salary3 = salary3;
    }

    public double getSalary4() {
        return salary4;
    }

    public void setSalary4(double salary4) {
        this.salary4 = salary4;
    }

    public double getSalary5() {
        return salary5;
    }

    public void setSalary5(double salary5) {
        this.salary5 = salary5;
    }

    public double getSalary6() {
        return salary6;
    }

    public void setSalary6(double salary6) {
        this.salary6 = salary6;
    }

    public double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(double totalSalary) {
        this.totalSalary = totalSalary;
    }
}
