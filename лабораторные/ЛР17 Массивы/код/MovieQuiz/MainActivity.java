package com.example.moviequiz;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [i][0] — название, [i][1] — год выпуска
        String[][] movies = {
                {"The Matrix", "1999"},
                {"Inception", "2010"},
                {"Titanic", "1997"},
                {"Interstellar", "2014"},
                {"The Godfather", "1972"}
        };

        Random random = new Random();
        boolean[] asked = new boolean[movies.length];   // какие фильмы уже были

        for (int q = 0; q < 3; q++) {
            int index;
            do {
                index = random.nextInt(movies.length);   // случайная строка 0..4
            } while (asked[index]);                      // без повторов
            asked[index] = true;

            Log.i("info", "What is the release year of " + movies[index][0] + "?");
            Log.i("info", "Answer: " + movies[index][1]);
        }
    }
}
