package com.example.gradebook;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] grades = new int[50];   // 50 элементов, пока все равны 0
        Random random = new Random();

        for (int i = 0; i < grades.length; i++) {
            grades[i] = random.nextInt(5) + 1;   // nextInt(5) даёт 0..4, +1 → 1..5
            Log.i("info", "Student " + i + " = " + grades[i]);
        }
    }
}
