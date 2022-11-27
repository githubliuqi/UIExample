package com.example.xlog.api;

/**
 * Created by liuqi on 2019/12/31.
 */

public interface ILogFormat {

    String formatLog(int priority, String className, int line, String tag, String log);
}
