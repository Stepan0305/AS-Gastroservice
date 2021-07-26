package com.timer.pdf.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ClipData;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.timer.pdf.Models.DataKeeperKeeper;
import com.timer.pdf.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AddPhotosActivity extends AppCompatActivity {
    ImageButton btnBack, btnSave, btnCamera, btnGallery, btnClear;
    LinearLayout box;
    ArrayList<File> pathList;
    private static final int CODE_GALLERY = 0;
    private static final int CODE_CAMERA = 1;
    String currentPhotoPath;
    long currentSum = 0;
    TextView txtCurrentSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnBack = findViewById(R.id.btnBackPhotos);
        btnSave = findViewById(R.id.btnSavePhotos);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        btnClear = findViewById(R.id.btnClearPhotos);
        box = findViewById(R.id.txtBox);
        txtCurrentSum = findViewById(R.id.currentSize);
        pathList = new ArrayList<>();
    }

    public void onPhotosClick(View v) {
        if (v.getId() == btnBack.getId()) {
            finish();
        } else if (v.getId() == btnSave.getId()) {
            DataKeeperKeeper.pathList.addAll(pathList);
            finish();
        } else if (v.getId() == btnGallery.getId()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Bild auswählen"), CODE_GALLERY);
        } else if (v.getId() == btnCamera.getId()) {
            try {
                File file = File.createTempFile(getFileName(), ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                currentPhotoPath = file.getAbsolutePath();
                Uri uri = FileProvider.getUriForFile(this, "com.timer.pdf.fileprovider", file);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CODE_CAMERA);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.getId() == btnClear.getId()){
            currentSum = 0;
            pathList = new ArrayList<>();
            updateCurrentSizeTV();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_GALLERY) {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri uri = clipData.getItemAt(i).getUri();
                        if (currentSum < 25000000) {
                            try {
                                Bitmap bitmap = null;
                                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                                File file = new File(getFile(i));
                                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, os);
                                os.close();
                                pathList.add(file);
                                currentSum += file.length();
                                updateCurrentSizeTV();
                                addTextView(file.getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else Toast.makeText(this, "Die Dateigröße ist zu groß!", Toast.LENGTH_LONG).show();
                    }
                } else{
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = null;
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        File file = new File(getFile(0));
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, os);
                        os.close();
                        pathList.add(file);
                        currentSum += file.length();
                        updateCurrentSizeTV();
                        addTextView(file.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == CODE_CAMERA) {
                if (currentSum < 25000000) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                        File file = new File(getFile(0));
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, os);
                        os.close();
                        pathList.add(file);
                        Log.d("size", file.length() + "");
                        addTextView(file.getName());
                        currentSum += file.length();
                        updateCurrentSizeTV();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else Toast.makeText(this, "Die Dateigröße ist zu groß!", Toast.LENGTH_LONG).show();

            }
        }
    }

    private String getFile(int i) {
        i++;
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File directory;
        directory = wrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(directory, getFileName() + "(" + i + ")" + ".jpg");
        return file.getPath();
    }

    private String getFileName() {
        return new SimpleDateFormat("dd-MM-yyyy HH.mm.ss", Locale.getDefault()).format(new Date());
    }

    private void addTextView(String s) {
        TextView tv = new TextView(AddPhotosActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 10);
        tv.setLayoutParams(params);
        tv.setBackgroundColor(getColor(R.color.white));
        tv.setText(s);
        tv.setTextColor(getColor(R.color.black));
        tv.setTextSize(18);
        box.addView(tv);
    }
    private void updateCurrentSizeTV(){
        float megabytes = currentSum / 1024f / 1024f;
        txtCurrentSum.setText("Momentane Größe: " + megabytes + " /24 megabytes");
        if (megabytes == 0){
            txtCurrentSum.setTextColor(getColor(R.color.black));
            box.removeAllViews();
        }
        else if (megabytes < 10){
            txtCurrentSum.setTextColor(getColor(R.color.green));
        } else if (megabytes < 20){
            txtCurrentSum.setTextColor(getColor(R.color.dark_yellow));
        } else txtCurrentSum.setTextColor(getColor(R.color.red));
    }
}