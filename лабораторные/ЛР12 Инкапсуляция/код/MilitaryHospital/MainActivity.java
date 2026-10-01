package com.example.militaryhospital;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Soldier rambo = new Soldier();
        rambo.setHealth(40);

        Hospital hospital = new Hospital();
        hospital.healSoldier(rambo);

        Log.i("Info:", "Здоровье Рэмбо после лечения: " + rambo.getHealth());
    }
}
