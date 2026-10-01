package com.example.petshelter;

import android.util.Log;

public class Pet {
    int age;         // возраст в месяцах
    String species;  // вид животного

    void makeSound() {
        Log.i("Info:", species + " издаёт звук");
    }
}
