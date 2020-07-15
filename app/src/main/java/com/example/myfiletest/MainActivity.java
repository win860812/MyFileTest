package com.example.myfiletest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    private EditText AC, PS;
    private CheckBox rm;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private String UserAc, UserPs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //如果沒有取得權限，則要求權限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

        } else {
            init();
        }

    }

    @Override
    //取得權限的控制
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            finish();
        }
    }

    private void init() {
        sp = getSharedPreferences("USER", MODE_PRIVATE);
        editor = sp.edit();
        AC = findViewById(R.id.AC);
        PS = findViewById(R.id.PS);
        rm = findViewById(R.id.remember);
        InputACPS();
        File root = new File("/sdcard/Android/data/" + getPackageName());
        if (!root.exists()) {
            root.mkdir();
            Log.v("test","sucess");
        }
    }

    public void InputACPS() {
        String UserAccount = sp.getString("ac", "");
        String UserPassWord = sp.getString("ps", "");
        AC.setText(UserAccount);
        PS.setText(UserPassWord);

    }

    public void Login(View view) {
        UserAc = AC.getText().toString();
        UserPs = PS.getText().toString();

        if (rm.isChecked()) {
            editor.putString("ac", UserAc);
            editor.putString("ps", UserPs);
            editor.commit();
        }
        if (UserAc.isEmpty() || UserPs.isEmpty()) {
            Toast.makeText(this, "帳號密碼不得為空", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
    }

    public void SD(View view) {
        try {
            FileOutputStream fpout = openFileOutput("AC+PS.txt", MODE_PRIVATE); //將資料存入Android/data/APP專案面
            fpout.write(UserAc.getBytes());
            fpout.write("\n".getBytes());
            fpout.write(UserPs.getBytes());
            fpout.flush();
            fpout.close();
            Toast.makeText(this, "SaveSD Sucess", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getMessage();
        }

    }
}
