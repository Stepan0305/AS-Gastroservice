package com.timer.pdf.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.timer.pdf.Models.DataKeeper;
import com.timer.pdf.Models.DataKeeperKeeper;
import com.timer.pdf.Models.EmailSender;
import com.timer.pdf.Models.PaintView;
import com.timer.pdf.R;

public class SignatureActivity2 extends AppCompatActivity {
    LinearLayout paintBox;
    PaintView paintView;
    ImageButton btnClear, btnConfirm;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature2);
        paintBox = findViewById(R.id.paintBox2);
        paintView = new PaintView(this);
        paintView.setBackgroundColor(Color.BLACK);
        paintBox.addView(paintView);
        btnClear = findViewById(R.id.btnClear2);
        btnConfirm = findViewById(R.id.btnConfirm2);
    }

    public void onTap2(View v){
        if (v.getId() == btnClear.getId()){
            paintView.path.reset();
            paintView.invalidate();
        } else if (v.getId() == btnConfirm.getId()){
            DataKeeperKeeper.keeper.setOurSignature(paintView.viewToBitmap(paintView));
            if (ContextCompat.checkSelfPermission(SignatureActivity2.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                new SendTask().execute(DataKeeperKeeper.keeper);
            } else {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(SignatureActivity2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    ActivityCompat.requestPermissions(SignatureActivity2.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        }
    }

    private class SendTask extends AsyncTask<DataKeeper, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SignatureActivity2.this);
            pd.setMessage("Die Nachricht wird gesendet...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(DataKeeper... dataKeepers) {
            try {
                EmailSender sender = new EmailSender("pdf.sender.bot@gmail.com", "SenderBot",
                        dataKeepers[0].getEmail(), "PDF blank", dataKeepers[0]);
                sender.createEmailMessage();
                sender.sendEmail();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if ((pd != null) && pd.isShowing()) {
                    pd.dismiss();
                }
            } catch (final Exception e) {
            } finally {
                pd = null;
            }
            Toast.makeText(SignatureActivity2.this, "Сообщение отправлено", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}