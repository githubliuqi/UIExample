package com.example.uiexample.common

import android.util.Log

object KLog {
    fun i(tag: String, log: String) {
        Log.i(tag, log)
    }
}