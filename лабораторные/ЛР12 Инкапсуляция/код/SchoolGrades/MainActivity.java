package com.example.schoolgrades;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Student john = new Student();
        john.setGrade(3);

        Teacher teacher = new Teacher();
        teacher.improveGrade(john);

        Log.i("Info:", "Новая оценка Джона: " + john.getGrade());
    }
}
