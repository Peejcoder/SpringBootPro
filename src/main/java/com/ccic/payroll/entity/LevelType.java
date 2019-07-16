package com.ccic.payroll.entity;

public enum LevelType {
    lv1("一星级",1),
    lv2("二星级",2),
    lv3("三星级",3),
    lv4("四星级",4),
    lv5("五星级",5);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private LevelType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (LevelType c : LevelType.values()) {
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

