package com.example.common;

import android.util.Log;

public class TLog {
    public static final void i(String tag, String log) {
        Log.i(tag, log);
    }

    public static final void d(String tag, String log) {
        Log.d(tag, log);
    }

    public static final void e(String tag, String log) {
        Log.e(tag, log);
    }
}
