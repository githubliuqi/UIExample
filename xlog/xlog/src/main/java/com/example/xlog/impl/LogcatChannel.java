package com.example.xlog.impl;

import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuqi on 2019/12/31.
 *
 */
public class LogcatChannel extends LogChannelBase {

    protected ExecutorService signleThreadExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void log(final int priority, final String className, final int line, final String tag, final String log) {
        signleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String log0 = getLogFormat().formatLog(priority, className, line, tag, log);
                if (TextUtils.isEmpty(tag)) {
                    System.out.println(log0);
                } else {
                    Log.println(priority, tag, log0);
                }
            }
        });
    }
}
