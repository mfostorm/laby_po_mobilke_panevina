package com.example.employees;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Создание объектов
        Manager myManager = new Manager("Alice", 50000, 10000);
        Developer myDev = new Developer("Bob", 40000, 10, 500);

        // Вывод зарплаты (используем переопределённый метод)
        Log.i("Salary:", myManager.getName() + ": $" + myManager.calculateSalary());
        Log.i("Salary:", myDev.getName() + ": $" + myDev.calculateSalary());

        // Демонстрация работы методов (переопределённый work())
        myManager.work();
        myDev.work();

        // Демонстрация работы общего метода из суперкласса
        Log.i("Total Employees:", "" + Employee.getTotalEmployees());

        // Геттер для имени
        Log.i("First Employee:", myManager.getName());
    }
}
