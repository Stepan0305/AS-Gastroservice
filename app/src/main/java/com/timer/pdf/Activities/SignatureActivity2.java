package com.timer.pdf.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sun.mail.util.MailConnectException;
import com.timer.pdf.Models.DataKeeper;
import com.timer.pdf.Models.DataKeeperKeeper;
import com.timer.pdf.Models.EmailSender;
import com.timer.pdf.Models.Generator;
import com.timer.pdf.Models.PaintView;
import com.timer.pdf.R;

import java.io.IOException;

public class SignatureActivity2 extends AppCompatActivity {
    LinearLayout paintBox;
    PaintView paintView;
    ImageButton btnClear, btnConfirm, btnBack, btnPhoto;
    ProgressDialog pd;
    boolean ok = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature2);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        paintBox = findViewById(R.id.paintBox2);
        paintView = new PaintView(this);
        paintView.setBackgroundColor(Color.BLACK);
        paintBox.addView(paintView);
        btnClear = findViewById(R.id.btnClear2);
        btnConfirm = findViewById(R.id.btnConfirm2);
        btnBack = findViewById(R.id.btnBack2);
        btnPhoto = findViewById(R.id.btnPhoto);
        if (DataKeeperKeeper.keeper.getOurSignature() != null) {
            new SendTask().execute(DataKeeperKeeper.keeper);
        }
        try {
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.logo_big);
            Generator.createFile(
                    b,
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/logo.png"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onTap2(View v) {
        if (v.getId() == btnClear.getId()) {
            paintView.path.reset();
            paintView.invalidate();
        } else if (v.getId() == btnConfirm.getId()) {
            if (DataKeeperKeeper.keeper.getOurSignature() == null) {
                DataKeeperKeeper.keeper.setOurSignature(paintView.viewToBitmap(paintView));
            }

            new SendTask().execute(DataKeeperKeeper.keeper);

        } else if (v.getId() == btnBack.getId()) {
            finish();
        } else if (v.getId() == btnPhoto.getId()) {
            startActivity(new Intent(this, AddPhotosActivity.class));
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

            } catch (MailConnectException mex) {
                mex.printStackTrace();
                ok = false;
//                AlertDialog.Builder alert = new AlertDialog.Builder(SignatureActivity2.this);
//                alert.setTitle("Error!");
//                alert.setMessage("Keine Internetverbindung. Datei im internen Speicher gespeichert.");
//                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(SignatureActivity2.this, "text", Toast.LENGTH_LONG).show();
//                    }
//                });
//                alert.create().show();
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
            if (ok) {
                Toast.makeText(SignatureActivity2.this, "Сообщение отправлено", Toast.LENGTH_LONG).show();
                Intent i = new Intent(SignatureActivity2.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(SignatureActivity2.this);
                alert.setTitle("Error!");
                alert.setMessage("Internet Verbindung fehlt. Datei befindet sich im Download Ordner.");
                alert.setCancelable(false);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(SignatureActivity2.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                alert.setNegativeButton("Neu starten", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recreate();
                    }
                });
                alert.create().show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}