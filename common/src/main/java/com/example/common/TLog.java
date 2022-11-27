package com.example.common;

import android.util.Log;

import java.util.Arrays;

public class TLog {
    public static final void i(String tag, String log) {
        Log.i(tag, log);
    }

    public static final void d(String tag, String log) {
        Log.d(tag, log);
    }

    public static final void w(String tag, String log) {
        Log.w(tag, log);
    }

    public static final void e(String tag, String log) {
        Log.e(tag, log);
    }

    public static final String toString(Object[][] array) {
        if (array == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(Arrays.toString(array[i]));
            sb.append(",\n");
        }
        return sb.toString();
    }
}
