package com.example.magicacademy;

import android.util.Log;

public class Wizard {
    int mana;      // запас магии
    String house;  // факультет
    String wand;   // волшебная палочка (задание 5)

    void castSpell() {
        Log.i("Info:", house + " колдует заклинание");
    }

    // Задание 5
    void castUltimateSpell() {
        Log.i("Info:", house + " использует палочку из " + wand
                + " и применяет Ультимативное заклинание!");
    }
}
