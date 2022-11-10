package com.example.uiexample.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.juiexample.common.TLog;

public class JMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jmain);
        TLog.i("", "");
    }
}