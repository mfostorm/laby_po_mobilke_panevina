package com.example.vehicles;

import android.util.Log;

public class Car extends Vehicle {

    public Car() {
        super(180, "Sedan");   // вызов конструктора Vehicle — первой строкой
        Log.i("Info:", "Car constructor");
    }

    @Override
    public void move() {
        Log.i("Info:", "Car is driving on the road");
    }
}
