package com.example.methods;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Info:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Задание 1. Методы с возвращаемыми значениями
        Log.i(TAG, "2 в степени 10 = " + calculatePower(2, 10));
        Log.i(TAG, "3 в степени 4 = " + calculatePower(3, 4));
        Log.i(TAG, "7 в степени 0 = " + calculatePower(7, 0));
        Log.i(TAG, getGreeting("Анна"));

        // Задание 2. Перегрузка метода
        logProduct("Смартфон");
        logProduct("Смартфон", 30000);
        logProduct("Смартфон", 30000, true);
    }

    // Возводит base в степень exponent: умножаем base сам на себя exponent раз
    private int calculatePower(int base, int exponent) {
        int result = 1;
        for (int i = 0; i < exponent; i++) {
            result = result * base;
        }
        return result;
    }

    // Формирует приветствие вида "Привет, Имя!"
    private String getGreeting(String name) {
        return "Привет, " + name + "!";
    }

    // Версия 1: только название
    private void logProduct(String name) {
        Log.i(TAG, "Товар: " + name);
    }

    // Версия 2: название и цена
    private void logProduct(String name, int price) {
        Log.i(TAG, "Товар: " + name + ", Цена: " + price + " руб.");
    }

    // Версия 3: название, цена и наличие на складе
    private void logProduct(String name, int price, boolean inStock) {
        String availability;
        if (inStock) {
            availability = "да";
        } else {
            availability = "нет";
        }
        Log.i(TAG, "Товар: " + name + ", Цена: " + price + " руб., В наличии: "
                + availability);
    }
}
