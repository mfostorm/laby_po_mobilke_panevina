package com.example.recursion;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Info:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int n = 5;

        Log.i(TAG, "countUp(" + n + "):");
        countUp(n);

        Log.i(TAG, "countDown(" + n + "):");
        countDown(n);

        Log.i(TAG, "Сумма чисел от 1 до " + n + " = " + sumNumbers(n));
    }

    // Выводит числа от 1 до n по возрастанию
    private void countUp(int n) {
        countUp(1, n);
    }

    // Вспомогательная версия: current — число, которое выводим сейчас
    private void countUp(int current, int n) {
        Log.i(TAG, "" + current);
        if (current >= n) {
            return;                 // базовый случай: дошли до N
        }
        countUp(current + 1, n);    // шаг рекурсии: следующее число
    }

    // Выводит числа от n до 1 по убыванию
    private void countDown(int n) {
        Log.i(TAG, "" + n);
        if (n <= 1) {
            return;                 // базовый случай: дошли до 1
        }
        countDown(n - 1);
    }

    // Сумма 1 + 2 + ... + n
    private int sumNumbers(int n) {
        if (n <= 1) {
            return n;               // базовый случай: sum(1) = 1
        }
        return n + sumNumbers(n - 1);
    }
}
