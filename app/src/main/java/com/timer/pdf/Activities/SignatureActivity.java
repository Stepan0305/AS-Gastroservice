package com.timer.pdf.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.timer.pdf.Models.DataKeeperKeeper;
import com.timer.pdf.Models.PaintView;
import com.timer.pdf.R;

public class SignatureActivity extends AppCompatActivity {
LinearLayout paintBox;
PaintView paintView;
ImageButton btnClear, btnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        paintBox = findViewById(R.id.paintBox);
        paintView = new PaintView(this);
        paintView.setBackgroundColor(Color.BLACK);
        paintBox.addView(paintView);
        btnClear = findViewById(R.id.btnClear);
        btnConfirm = findViewById(R.id.btnConfirm);
    }

    public void onTap(View v){
        if (v.getId() == btnClear.getId()){
            paintView.path.reset();
            paintView.invalidate();
        } else if (v.getId() == btnConfirm.getId()){
            DataKeeperKeeper.keeper.setClientSignature(paintView.viewToBitmap(paintView));
            Intent i = new Intent(SignatureActivity.this, SignatureActivity2.class);
            startActivity(i);
            finish();
        }
    }
}