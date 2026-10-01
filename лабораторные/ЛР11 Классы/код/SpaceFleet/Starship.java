package com.example.spacefleet;

import android.util.Log;

public class Starship {
    int shield;        // уровень щита
    String shipClass;  // класс корабля
    String captain;    // имя капитана (задание 4)

    void fireLasers() {
        Log.i("Info:", shipClass + " открывает огонь лазерами");
    }

    // Задание 4
    void launchTorpedo() {
        Log.i("Info:", "Капитан " + captain + " с корабля " + shipClass
                + " запускает торпеду!");
    }
}
