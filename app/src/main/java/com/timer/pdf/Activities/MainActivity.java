package com.timer.pdf.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.timer.pdf.Models.DataKeeper;
import com.timer.pdf.Models.DataKeeperKeeper;
import com.timer.pdf.Models.Generator;
import com.timer.pdf.Models.PaintView;
import com.timer.pdf.R;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView txtHours, txtMinutes, txtSeconds;
    Button btnSet, btnReset;
    FloatingActionButton btnNext, btnSettings;
    boolean timerStarted = false;
    Timer timer;
    TimerTask timerTask;
    double time = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtHours = findViewById(R.id.txtHours);
        txtMinutes = findViewById(R.id.txtMinutes);
        txtSeconds = findViewById(R.id.txtSeconds);
        btnSet = findViewById(R.id.btnSet);
        btnReset = findViewById(R.id.btnReset);
        btnNext = findViewById(R.id.btnNext);
        btnSettings = findViewById(R.id.btnSettings);
        timer = new Timer();
        if (!hasPermission()){
            requestPermission();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        DataKeeperKeeper.keeper = new DataKeeper();
        DataKeeperKeeper.currentFile = null;
        if (!PreferenceManager.getDefaultSharedPreferences(this).getString("ourData", "no").equals("no")) {
            DataKeeperKeeper.keeper.setOurData(PreferenceManager.getDefaultSharedPreferences(this).getString("ourData", "no"));
        }
        if (!PreferenceManager.getDefaultSharedPreferences(this).getString("ourEmail", "no").equals("no")) {
            DataKeeperKeeper.keeper.setOurEmail(PreferenceManager.getDefaultSharedPreferences(this).getString("ourEmail", "no"));
        }
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
            if (timerTask != null) {
                timerTask.cancel();
            }
            Intent intent = new Intent(MainActivity.this, BlankActivity.class);
            intent.putExtra("time", time);
            startActivity(intent);
        } else if (v.getId() == btnSettings.getId()) {
            Intent i = new Intent(MainActivity.this, ChangeDataActivity.class);
            startActivity(i);
        }
    }

    private void startTimer() {
        //вложенность, чтоб работал при выходе из приложения
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
        //формулы из интернета
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

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            int result2 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
            return Environment.isExternalStorageManager() && result2 == PackageManager.PERMISSION_GRANTED;
        } else {
            int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int result2 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                    && result2 == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA}, 1);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, 1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}