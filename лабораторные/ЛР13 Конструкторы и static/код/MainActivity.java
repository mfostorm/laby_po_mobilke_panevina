package com.example.spacebattle;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Создание кораблей
        SpaceShip shipOne = new SpaceShip();
        SpaceShip shipTwo = new SpaceShip();

        // 2. Статический метод
        Log.i("Info:", "Всего кораблей создано: " + SpaceShip.getTotalShips());

        // 3. Имена через public-поле
        shipOne.shipName = "Восток";
        shipTwo.shipName = "Союз";

        // 4. Геттер
        Log.i("Info:", shipOne.shipName + ": прочность " + shipOne.getHullStrength());
        Log.i("Info:", shipTwo.shipName + ": прочность " + shipTwo.getHullStrength());

        // 5. Несанкционированный доступ — не компилируется:
        // shipOne.hullStrength = 999;
        //   → error: hullStrength has private access in SpaceShip
        // shipTwo.setHullStrength(200);
        //   → error: setHullStrength(int) has private access in SpaceShip

        // 6. Симуляция боя
        shipOne.takeDamage();
        logHull(shipOne, shipTwo);

        shipTwo.takeDamage();
        shipTwo.takeDamage();
        shipTwo.takeDamage();
        logHull(shipOne, shipTwo);

        shipTwo.takeDamage();
        logHull(shipOne, shipTwo);

        // 7. Финальная проверка счётчика
        Log.i("Info:", "Всего кораблей осталось: " + SpaceShip.getTotalShips());
    }

    private void logHull(SpaceShip a, SpaceShip b) {
        Log.i("Info:", a.shipName + ": " + a.getHullStrength()
                + ", " + b.shipName + ": " + b.getHullStrength());
    }
}
