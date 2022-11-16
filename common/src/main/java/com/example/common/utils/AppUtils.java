package com.example.common.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.TypedValue;

import java.lang.reflect.InvocationTargetException;

public class AppUtils {

    private static Application application;

    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return TextUtils.isEmpty(applicationName) ? "" : applicationName;
    }

    public static String getPackageName() {
        return getApplicationByReflect().getPackageName();
    }

    public static Application getApplicationByReflect() {
        if (application != null) {
            return application;
        }
        try {
            @SuppressLint("PrivateApi") Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            application = (Application) app;
            if (app == null) {
                throw new NullPointerException("You should init first.");
            }
            return application;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("You should init first.");
    }

    public static Resources getResources() {
        return getApplicationByReflect().getResources();
    }

    public static int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static Bitmap decodeResource(int id) {
        TypedValue value = new TypedValue();
        getResources().openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(getResources(), id, opts);
    }
}
