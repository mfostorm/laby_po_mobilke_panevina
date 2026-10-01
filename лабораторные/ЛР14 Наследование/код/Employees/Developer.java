package com.example.employees;

import android.util.Log;

public class Developer extends Employee {

    private int overtimeHours;
    private int overtimeRate;

    public Developer(String name, int baseSalary, int overtimeHours, int overtimeRate) {
        super(name, baseSalary);
        this.overtimeHours = overtimeHours;
        this.overtimeRate = overtimeRate;
        Log.i("Info:", "Developer constructor");
    }

    @Override
    public int calculateSalary() {
        return getBaseSalary() + overtimeHours * overtimeRate;
    }

    @Override
    public void work() {
        Log.i("Info:", "Developer is writing code");
    }
}
