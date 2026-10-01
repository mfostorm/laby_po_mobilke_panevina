package com.example.employees;

import android.util.Log;

public abstract class Employee {

    private String name;
    private int baseSalary;
    private static int totalEmployees;

    public Employee(String name, int baseSalary) {
        totalEmployees++;
        this.name = name;
        this.baseSalary = baseSalary;
        Log.i("Info:", "Employee constructor: " + name);
    }

    // Формула зарплаты у каждой должности своя
    public abstract int calculateSalary();

    public String getName() {
        return name;
    }

    public int getBaseSalary() {
        return baseSalary;
    }

    public void work() {
        Log.i("Info:", "Employee is working");
    }

    public static int getTotalEmployees() {
        return totalEmployees;
    }
}
