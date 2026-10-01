package com.example.javameetui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // переменная типа int для хранения значения
    private int value = 0;

    // Набор кнопок и TextView
    private Button btnAdd;
    private Button btnTake;
    private TextView txtValue;
    private Button btnGrow;
    private Button btnShrink;
    private Button btnReset;
    private Button btnHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получаем ссылки на все виджеты из разметки
        btnAdd = findViewById(R.id.btnAdd);
        btnTake = findViewById(R.id.btnTake);
        txtValue = findViewById(R.id.txtValue);
        btnGrow = findViewById(R.id.btnGrow);
        btnShrink = findViewById(R.id.btnShrink);
        btnReset = findViewById(R.id.btnReset);
        btnHide = findViewById(R.id.btnHide);

        // "Слушаем" нажатия всех кнопок: обработчик — сама MainActivity
        btnAdd.setOnClickListener(this);
        btnTake.setOnClickListener(this);
        txtValue.setOnClickListener(this);
        btnGrow.setOnClickListener(this);
        btnShrink.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnHide.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Локальная переменная для масштаба текста
        float size;
        int id = view.getId();

        // В новых версиях Android Gradle Plugin R.id.* не являются константами,
        // поэтому switch (view.getId()) не компилируется — используем if-else
        if (id == R.id.btnAdd) {
            value++;
            txtValue.setText("" + value);
        } else if (id == R.id.btnTake) {
            value--;
            txtValue.setText("" + value);
        } else if (id == R.id.btnReset) {
            value = 0;
            txtValue.setText("" + value);
        } else if (id == R.id.btnGrow) {
            size = txtValue.getTextScaleX();
            txtValue.setTextScaleX(size + 1);
        } else if (id == R.id.btnShrink) {
            size = txtValue.getTextScaleX();
            txtValue.setTextScaleX(size - 1);
        } else if (id == R.id.btnHide) {
            if (txtValue.getVisibility() == View.VISIBLE) {
                // Сейчас текст виден — скрываем
                txtValue.setVisibility(View.INVISIBLE);
                btnHide.setText("SHOW");
            } else {
                // Сейчас текст скрыт — показываем
                txtValue.setVisibility(View.VISIBLE);
                btnHide.setText("HIDE");
            }
        }
    }
}
