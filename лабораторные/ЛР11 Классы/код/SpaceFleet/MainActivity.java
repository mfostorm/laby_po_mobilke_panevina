package com.example.spacefleet;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Задание 1. Три корабля
        Starship enterprise = new Starship();
        enterprise.shipClass = "Крейсер";
        enterprise.shield = 200;

        Starship serpent = new Starship();
        serpent.shipClass = "Перехватчик";
        serpent.shield = 80;

        Starship odyssey = new Starship();
        odyssey.shipClass = "Линкор";
        odyssey.shield = 300;

        Log.i("Info:", "Щит корабля " + enterprise.shipClass + ": " + enterprise.shield);
        Log.i("Info:", "Щит корабля " + serpent.shipClass + ": " + serpent.shield);
        Log.i("Info:", "Щит корабля " + odyssey.shipClass + ": " + odyssey.shield);

        enterprise.fireLasers();
        serpent.fireLasers();
        odyssey.fireLasers();

        // Задание 4. Четвёртый корабль и капитаны
        Starship defiant = new Starship();
        defiant.shipClass = "Фрегат";
        defiant.shield = 180;

        enterprise.captain = "Пикард";
        serpent.captain = "Снейк";
        odyssey.captain = "Галер";
        defiant.captain = "Сиско";

        Log.i("Info:", "Капитан корабля " + enterprise.shipClass + ": " + enterprise.captain);
        Log.i("Info:", "Капитан корабля " + serpent.shipClass + ": " + serpent.captain);
        Log.i("Info:", "Капитан корабля " + odyssey.shipClass + ": " + odyssey.captain);
        Log.i("Info:", "Капитан корабля " + defiant.shipClass + ": " + defiant.captain);

        enterprise.launchTorpedo();
        serpent.launchTorpedo();
        odyssey.launchTorpedo();
        defiant.launchTorpedo();
    }
}
