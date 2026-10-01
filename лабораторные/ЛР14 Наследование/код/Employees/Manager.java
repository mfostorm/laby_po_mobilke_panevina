package com.example.employees;

import android.util.Log;

public class Manager extends Employee {

    private int bonus;

    public Manager(String name, int baseSalary, int bonus) {
        super(name, baseSalary);
        this.bonus = bonus;
        Log.i("Info:", "Manager constructor");
    }

    @Override
    public int calculateSalary() {
        // baseSalary приватное в Employee — берём через геттер
        return getBaseSalary() + bonus;
    }

    @Override
    public void work() {
        Log.i("Info:", "Manager is managing the team");
    }
}
