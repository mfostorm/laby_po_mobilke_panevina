package com.example.vehicles;

import android.util.Log;

public class Airplane extends Vehicle {

    public Airplane() {
        super(900, "Boeing 737");
        Log.i("Info:", "Airplane constructor");
    }

    @Override
    public void move() {
        Log.i("Info:", "Airplane is flying in the sky");
    }
}
