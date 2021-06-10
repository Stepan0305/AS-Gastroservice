package com.timer.pdf.Activities;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.timer.pdf.R;

public class BlankActivity extends AppCompatActivity {
    Button addPart;
    LinearLayout box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_acrivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        int time = (int) Math.round(getIntent().getDoubleExtra("time", 0));
        int seconds = ((time % 86400) % 3600) % 60;
        int minutes = ((time % 86400) % 3600) / 60;
        int hours = (time % 86400) / 3600;
        String s = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        toolBarLayout.setTitle("Время: " + s);

        addPart = findViewById(R.id.addPart);
        box = findViewById(R.id.box);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onClick(View v) {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        EditText name = new EditText(this);
        name.setLayoutParams(new LinearLayout.LayoutParams((int)(width * 0.4), LinearLayout.LayoutParams.WRAP_CONTENT));
        name.setTextSize(16);
        name.setHint("Название");
        layout.addView(name);
        EditText number = new EditText(this);
        number.setLayoutParams(new LinearLayout.LayoutParams((int)(width * 0.3), LinearLayout.LayoutParams.WRAP_CONTENT));
        number.setTextSize(16);
        number.setHint("Номер");
        layout.addView(number);
        EditText count = new EditText(this);
        count.setLayoutParams(new LinearLayout.LayoutParams((int)(width * 0.3), LinearLayout.LayoutParams.WRAP_CONTENT));
        count.setTextSize(16);
        count.setHint("Количество");
        layout.addView(count);
        box.addView(layout);
    }
}