package com.timer.pdf.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.timer.pdf.Models.Connector;
import com.timer.pdf.Models.DataKeeper;
import com.timer.pdf.Models.Part;
import com.timer.pdf.R;

import java.util.ArrayList;

public class BlankActivity extends AppCompatActivity {
    Button addPart;
    ViewGroup box;
    EditText clientData, workDone, clientName, clientEmail;
    int layoutId = 1;
    int nameId = 100;
    int numberId = 10000;
    int countId = 1000000;

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
        clientData = findViewById(R.id.clientData);
        clientName = findViewById(R.id.clientName);
        workDone = findViewById(R.id.workMade);
        clientEmail = findViewById(R.id.clientEmail);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Part> parts = new ArrayList<>();
                for (int i = 1; i < layoutId; i++) {
                    LinearLayout layout = box.findViewById(i);
                    EditText name = layout.findViewById(i * 100);
                    EditText number = layout.findViewById(i * 10000);
                    EditText count = layout.findViewById(i * 1000000);
                    Part part = new Part(name.getText() + "", number.getText() + "", count.getText() + "");
                    parts.add(part);
                }
                DataKeeper keeper = new DataKeeper(
                        clientData.getText().toString(),
                        workDone.getText().toString(),
                        clientName.getText().toString(),
                        clientEmail.getText().toString(), parts
                );
                Connector.send(keeper);
            }
        });
    }

    @SuppressLint("ResourceType")
    public void onClick(View v) {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        LinearLayout layout = new LinearLayout(this);
        layout.setId(layoutId);
        layoutId++;
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        EditText name = new EditText(this);
        name.setId(nameId);
        nameId += 100;
        Log.d("id", name.getId() + "");
        name.setLayoutParams(new LinearLayout.LayoutParams((int) (width * 0.4), LinearLayout.LayoutParams.WRAP_CONTENT));
        name.setTextSize(16);
        name.setHint("Название");
        layout.addView(name);
        EditText number = new EditText(this);
        number.setLayoutParams(new LinearLayout.LayoutParams((int) (width * 0.3), LinearLayout.LayoutParams.WRAP_CONTENT));
        number.setId(numberId);
        numberId += 10000;
        number.setTextSize(16);
        number.setHint("Номер");
        layout.addView(number);
        EditText count = new EditText(this);
        count.setLayoutParams(new LinearLayout.LayoutParams((int) (width * 0.3), LinearLayout.LayoutParams.WRAP_CONTENT));
        count.setId(countId);
        countId += 1000000;
        count.setTextSize(16);
        count.setHint("Количество");
        layout.addView(count);
        box.addView(layout);
    }
}