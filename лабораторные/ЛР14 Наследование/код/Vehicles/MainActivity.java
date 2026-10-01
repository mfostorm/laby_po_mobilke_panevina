package com.example.vehicles;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Создание объектов
        Car myCar = new Car();
        Airplane myPlane = new Airplane();

        // Присвоение имён (используем поле из суперкласса)
        myCar.modelName = "Toyota Camry";
        myPlane.modelName = "Airbus A320";

        // Вывод характеристик (используем геттеры из суперкласса)
        Log.i("Car Speed:", "Max: " + myCar.getMaxSpeed());
        Log.i("Plane Speed:", "Max: " + myPlane.getMaxSpeed());

        // Демонстрация работы методов
        myCar.accelerate(50);
        Log.i("Car Speed:", "Current: " + myCar.getCurrentSpeed());

        myPlane.accelerate(300);
        Log.i("Plane Speed:", "Current: " + myPlane.getCurrentSpeed());

        // Демонстрация полиморфизма (разная работа метода move())
        myCar.move();
        myPlane.move();

        // Торможение
        myCar.brake();
        Log.i("Car Speed:", "After brake: " + myCar.getCurrentSpeed());

        // Проверка счётчика
        Log.i("Total Vehicles:", "" + Vehicle.getNumVehicles());
    }
}
