package com.ccic.payroll.entity;

public enum WorkType {

    day1("白三休一以上", 0),
    day2("白三休一以下", 1),
    mid1("中三休一以上",2),
    mid2("中白三休一以下",3),
    night("夜班",4);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private WorkType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (WorkType c : WorkType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

