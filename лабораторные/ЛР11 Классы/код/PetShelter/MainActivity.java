package com.example.petshelter;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pet barsik = new Pet();
        barsik.species = "Кот";
        barsik.age = 24;

        Pet rex = new Pet();
        rex.species = "Собака";
        rex.age = 36;

        Pet kesha = new Pet();
        kesha.species = "Попугай";
        kesha.age = 12;

        Log.i("Info:", "Возраст Барсика: " + barsik.age + " мес.");
        Log.i("Info:", "Возраст Рекса: " + rex.age + " мес.");
        Log.i("Info:", "Возраст Кеши: " + kesha.age + " мес.");

        barsik.makeSound();
        rex.makeSound();
        kesha.makeSound();
    }
}
