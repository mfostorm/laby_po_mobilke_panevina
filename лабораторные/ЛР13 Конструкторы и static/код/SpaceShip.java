package com.example.spacebattle;

import android.util.Log;

public class SpaceShip {

    private static int totalShips;   // общий счётчик для всех кораблей
    private int hullStrength;        // прочность корпуса — скрыта
    public String shipName;          // имя корабля — доступно извне

    // Конструктор: вызывается при каждом new SpaceShip()
    public SpaceShip() {
        totalShips++;
        setHullStrength(100);
    }

    // Статический метод — вызывается через имя класса
    public static int getTotalShips() {
        return totalShips;
    }

    // Приватный сеттер: менять прочность может только сам класс
    private void setHullStrength(int hullStrength) {
        this.hullStrength = hullStrength;   // this.поле = параметр
    }

    public int getHullStrength() {
        return this.hullStrength;
    }

    public void takeDamage() {
        if (hullStrength <= 0) {
            // Защита от повторного уничтожения (иначе счётчик уменьшится дважды)
            Log.i("Info:", "Корабль '" + shipName + "' уже уничтожен");
            return;
        }
        hullStrength -= 35;
        Log.i("Info:", "Попадание! ");
        if (hullStrength <= 0) {
            destroyShip();
        }
    }

    private void destroyShip() {
        totalShips--;
        Log.i("Info:", "Корабль '" + this.shipName + "' уничтожен!");
    }
}
