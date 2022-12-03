package com.example.bugs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.common.TLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BugsActivity extends Activity {

    private static final String TAG = "BugsActivity";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            TLog.d(TAG, "handleMessage");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        boolean isAnonymousInnerClass = intent.getBooleanExtra("isAnonymousInnerClass", false);
        String viewClass = intent.getStringExtra("viewClass");
        try {
            Class clazz = Class.forName(viewClass);
            Constructor constructor = clazz.getConstructor(Context.class);
            constructor.setAccessible(true);
            View view = (View) constructor.newInstance(this);
            if (isAnonymousInnerClass) {
               Method method = clazz.getDeclaredMethod("setAnonymousInnerObject", Object.class);
               if (method != null) {
                   method.invoke(view, handler);
               }
            }
            setContentView(view);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
