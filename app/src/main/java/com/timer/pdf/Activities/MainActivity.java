package com.timer.pdf.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.timer.pdf.R;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView txtHours, txtMinutes, txtSeconds;
    Button btnSet, btnReset;
    FloatingActionButton btnNext;
    boolean timerStarted = false;
    Timer timer;
    TimerTask timerTask;
    double time = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        txtHours = findViewById(R.id.txtHours);
        txtMinutes = findViewById(R.id.txtMinutes);
        txtSeconds = findViewById(R.id.txtSeconds);
        btnSet = findViewById(R.id.btnSet);
        btnReset = findViewById(R.id.btnReset);
        btnNext = findViewById(R.id.btnNext);
        timer = new Timer();
    }

    public void onClick(View v) {
        if (v.getId() == btnSet.getId()) {
            if (!timerStarted) {
                btnSet.setBackgroundColor(getResources().getColor(R.color.yellow));
                btnSet.setText("Pause");
                startTimer();
            } else {
                btnSet.setBackgroundColor(getResources().getColor(R.color.green));
                btnSet.setText("Start");
                timerTask.cancel();
            }
            timerStarted = !timerStarted;
        } else if (v.getId() == btnReset.getId()) {
            time = 0.0;
            updateTimer();
        } else if (v.getId() == btnNext.getId()) {
            timer.cancel();
            Intent intent = new Intent(MainActivity.this, BlankActivity.class);
            intent.putExtra("time", time);
            startActivity(intent);
        }
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time += 1;
                        updateTimer();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private void updateTimer() {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = (rounded % 86400) / 3600;

        if ((seconds < 10)) {
            txtSeconds.setText("0" + seconds);
        } else {
            txtSeconds.setText(seconds + "");
        }
        if ((minutes < 10)) {
            txtMinutes.setText("0" + minutes + ":");
        } else {
            txtMinutes.setText(minutes + ":");
        }
        if ((hours < 10)) {
            txtHours.setText("0" + hours + ":");
        } else {
            txtHours.setText(hours + ":");
        }
    }
}