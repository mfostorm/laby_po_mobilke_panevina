package com.example.magicacademy;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Задание 2. Три волшебника
        Wizard harry = new Wizard();
        harry.house = "Гриффиндор";
        harry.mana = 120;

        Wizard draco = new Wizard();
        draco.house = "Слизерин";
        draco.mana = 90;

        Wizard luna = new Wizard();
        luna.house = "Когтевран";
        luna.mana = 110;

        Log.i("Info:", "Мана Гарри: " + harry.mana);
        Log.i("Info:", "Мана Драко: " + draco.mana);
        Log.i("Info:", "Мана Полумны: " + luna.mana);

        harry.castSpell();
        draco.castSpell();
        luna.castSpell();

        // Задание 5. Четвёртый волшебник и палочки
        Wizard hermione = new Wizard();
        hermione.house = "Гриффиндор";
        hermione.mana = 130;

        harry.wand = "Остролист";
        draco.wand = "Боярышник";
        luna.wand = "Тополь";
        hermione.wand = "Виноградная лоза";

        Log.i("Info:", "Палочка Гарри: " + harry.wand);
        Log.i("Info:", "Палочка Драко: " + draco.wand);
        Log.i("Info:", "Палочка Полумны: " + luna.wand);
        Log.i("Info:", "Палочка Гермионы: " + hermione.wand);

        harry.castUltimateSpell();
        draco.castUltimateSpell();
        luna.castUltimateSpell();
        hermione.castUltimateSpell();
    }
}
