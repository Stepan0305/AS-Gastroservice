package com.timer.pdf.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.timer.pdf.Models.DataKeeper;
import com.timer.pdf.Models.DataKeeperKeeper;
import com.timer.pdf.R;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class ChangeDataActivity extends AppCompatActivity {
EditText data, email;
FloatingActionButton save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = findViewById(R.id.editData);
        data.setText(DataKeeperKeeper.keeper.getOurData());
        email = findViewById(R.id.editEmail);
        email.setText(DataKeeperKeeper.keeper.getOurEmail());
        save = findViewById(R.id.btnSave);

    }

    public void onSave(View v){
        String place1 = data.getText().toString();
        String place2 = email.getText().toString();
        if (place1.isEmpty() || place2.isEmpty()){
            Toast.makeText(this, "Füllen Sie das Feld aus!", Toast.LENGTH_LONG).show();
        } else if (!isValidEmailAddress(place2)) {
            Toast.makeText(this, "Falsche E-Mail Adresse!", Toast.LENGTH_LONG).show();
        } else {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("ourData", place1).apply();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("ourEmail", place2).apply();
            finish();
        }
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}