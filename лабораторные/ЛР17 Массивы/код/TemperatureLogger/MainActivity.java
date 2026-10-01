package com.example.temperaturelogger;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Массив на 7 дней, сразу инициализируем значениями
        int[] temperatures = {22, 24, 21, 23, 25, 22, 24};

        Log.i("info", "Weekly temperatures:");
        int sum = 0;
        for (int i = 0; i < temperatures.length; i++) {
            Log.i("info", "Day " + i + " = " + temperatures[i]);
            sum += temperatures[i];
        }

        // Приводим к double, чтобы деление не было целочисленным
        double average = (double) sum / temperatures.length;
        Log.i("info", "Average temperature = " + average);
    }
}
