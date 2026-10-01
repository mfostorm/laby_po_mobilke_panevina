package com.example.militaryhospital;

import android.util.Log;

public class Soldier {
    private int health;   // скрыто от внешнего кода

    public int getHealth() {
        return health;
    }

    // Принимаем только значения от 0 до 100
    public void setHealth(int newHealth) {
        if (newHealth < 0 || newHealth > 100) {
            Log.w("Soldier", "Недопустимое здоровье: " + newHealth);
            return;
        }
        health = newHealth;
    }
}
