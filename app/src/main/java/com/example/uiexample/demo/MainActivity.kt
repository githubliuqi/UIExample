package com.example.uiexample.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uiexample.common.KLog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KLog.i("test", "test")
    }
}