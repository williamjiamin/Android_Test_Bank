package com.lexueoude.lxodtestbank;

public class Category {
    public static final int JAVA = 1;
    public static final int ANDROID = 2;
    public static final int PYTHON = 3;

    private int id;
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
