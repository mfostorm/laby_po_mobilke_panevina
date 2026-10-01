package com.example.widgets;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Все ссылки на виджеты — final
        final ImageView imageView = findViewById(R.id.imageView);
        final CheckBox checkBoxAlpha = findViewById(R.id.checkBoxAlpha);
        final CheckBox checkBoxScale = findViewById(R.id.checkBoxScale);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final EditText editText = findViewById(R.id.editText);
        final Button button = findViewById(R.id.button);
        final TextView textView = findViewById(R.id.textView);
        final Switch switch1 = findViewById(R.id.switch1);

        // TextView изначально невидим
        textView.setVisibility(View.INVISIBLE);

        // 4. При старте ни одна радиокнопка не выбрана
        radioGroup.clearCheck();

        // CheckBox «Прозрачность»
        checkBoxAlpha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imageView.setAlpha(0.3f);
                } else {
                    imageView.setAlpha(1f);
                }
            }
        });

        // CheckBox «Увеличение»
        checkBoxScale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imageView.setScaleX(1.5f);
                    imageView.setScaleY(1.5f);
                } else {
                    imageView.setScaleX(1f);
                    imageView.setScaleY(1f);
                }
            }
        });

        // RadioGroup: 3. if-else вместо switch
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioRed) {
                    imageView.setColorFilter(Color.argb(150, 255, 0, 0));
                } else if (checkedId == R.id.radioBlue) {
                    imageView.setColorFilter(Color.argb(150, 0, 0, 255));
                } else {
                    imageView.clearColorFilter();   // checkedId == -1: ничего не выбрано
                }
            }
        });

        // Button: копируем текст и показываем TextView
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(editText.getText().toString());
                textView.setVisibility(View.VISIBLE);
                // 5. Синхронизируем свитч с видимостью текста
                switch1.setChecked(true);
            }
        });

        // Switch: показывает или скрывает TextView
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
