package com.timer.pdf.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.timer.pdf.Models.DataKeeperKeeper;
import com.timer.pdf.R;

public class ChangeDataActivity extends AppCompatActivity {
EditText data;
FloatingActionButton save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);
        data = findViewById(R.id.editData);
        save = findViewById(R.id.btnSave);
    }

    public void onSave(View v){
        String place = data.getText().toString();
        if (place.isEmpty()){
            Toast.makeText(this, "Füllen Sie das Feld aus!", Toast.LENGTH_LONG).show();
        } else {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("ourData", place).apply();
            finish();
        }
    }
}