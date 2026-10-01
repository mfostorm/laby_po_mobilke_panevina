package com.example.widgetexploration;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получаем ссылки на все виджеты.
        // final — потому что они используются внутри анонимных классов
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final EditText editText = findViewById(R.id.editText);
        final Button button = findViewById(R.id.button);
        final TextClock tClock = findViewById(R.id.textClock);
        final CheckBox cbTransparency = findViewById(R.id.checkBoxTransparency);
        final CheckBox cbTint = findViewById(R.id.checkBoxTint);
        final CheckBox cbReSize = findViewById(R.id.checkBoxReSize);
        final ImageView imageView = findViewById(R.id.imageView);
        Switch switch1 = findViewById(R.id.switch1);
        final TextView textView = findViewById(R.id.textView);

        // Прячем TextView при запуске приложения
        textView.setVisibility(View.INVISIBLE);

        // Чекбоксы
        cbTransparency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbTransparency.isChecked()) {
                    imageView.setAlpha(.1f);   // почти прозрачная
                } else {
                    imageView.setAlpha(1f);    // полностью видимая
                }
            }
        });

        cbTint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbTint.isChecked()) {
                    imageView.setColorFilter(Color.argb(150, 255, 0, 0)); // красный
                } else {
                    imageView.setColorFilter(Color.argb(0, 0, 0, 0));     // без цвета
                }
            }
        });

        cbReSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbReSize.isChecked()) {
                    imageView.setScaleX(2);
                    imageView.setScaleY(2);
                } else {
                    imageView.setScaleX(1);
                    imageView.setScaleY(1);
                }
            }
        });

        // Радиокнопки: сначала снимаем отметки, затем слушаем выбор
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (rb == null) {
                    return;   // срабатывает при clearCheck(), когда ничего не выбрано
                }
                int id = rb.getId();
                if (id == R.id.radioButtonLondon) {
                    tClock.setTimeZone("Europe/London");
                } else if (id == R.id.radioButtonBeijing) {
                    tClock.setTimeZone("Asia/Shanghai");
                } else if (id == R.id.radioButtonNewYork) {
                    tClock.setTimeZone("America/New_York");
                }
            }
        });

        // Кнопка Capture: переносим текст из EditText в TextView
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(editText.getText());
            }
        });

        // Switch: показать или скрыть TextView
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

        // WebView: загружаем сайт (нужно разрешение INTERNET в манифесте)
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("https://gamecodeschool.com");
    }
}
