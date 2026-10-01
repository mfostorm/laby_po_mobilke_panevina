package com.example.vehicles;

import android.util.Log;

public abstract class Vehicle {

    private int maxSpeed;
    private int currentSpeed;
    public String modelName;
    private static int numVehicles;

    public Vehicle(int maxSpeed, String modelName) {
        numVehicles++;
        this.maxSpeed = maxSpeed;
        this.modelName = modelName;
        Log.i("Info:", "Vehicle constructor: " + modelName);
    }

    // Каждый наследник обязан описать, как он двигается
    public abstract void move();

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = Math.max(0, Math.min(currentSpeed, maxSpeed));
    }

    // Разгон, но не выше максимальной скорости
    public void accelerate(int delta) {
        currentSpeed = currentSpeed + delta;
        if (currentSpeed > maxSpeed) {
            currentSpeed = maxSpeed;
        }
    }

    public void brake() {
        currentSpeed = 0;
    }

    public static int getNumVehicles() {
        return numVehicles;
    }
}
