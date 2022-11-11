package com.example.juiexample.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {

    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static final void showLong(Context context, String text) {
        handler.post(() -> Toast.makeText(context, text, Toast.LENGTH_SHORT).show());
    }
}
