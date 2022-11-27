package com.example.xlog.impl;

import android.util.Log;

import com.example.xlog.api.ILogger;
import com.example.xlog.api.ILogFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liuqi on 2019/12/31.
 */

public class LogFormatStd implements ILogFormat {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return createDateFormat();
        }
    };

    private boolean enableThreadInfo = false;

    private boolean enableStackTrace = false;

    private boolean enableTime = true;

    private boolean enableLevel = true;

    private boolean enableTag = false;

    private boolean enableClassName = true;

    private boolean enableLine = true;

    public LogFormatStd setEnableThreadInfo(boolean enable){
        enableThreadInfo = enable;
        return this;
    }

    public LogFormatStd setEnableStackTrace(boolean enable){
        enableStackTrace = enable;
        return this;
    }

    public LogFormatStd setEnableTime(boolean enable){
        enableTime = enable;
        return this;
    }

    public LogFormatStd setEnableLevel(boolean enable){
        enableLevel = enable;
        return this;
    }

    public LogFormatStd setEnableTag(boolean enable){
        enableTag = enable;
        return this;
    }

    public LogFormatStd setEnableClassName(boolean enableClassName) {
        this.enableClassName = enableClassName;
        return this;
    }

    public LogFormatStd setEnableLine(boolean enableLine) {
        this.enableLine = enableLine;
        return this;
    }

    @Override
    public String formatLog(int priority, String className, int line, String tag, String log) {
        StringBuilder sb = new StringBuilder("");
        if (enableTime){
            sb.append("[").append(getCurTime(getDateFormat())).append("]");
        }
        if (enableLevel){
            sb.append("[").append(priorityToString(priority)).append("]");
        }
        if (enableThreadInfo){
            sb.append("[").append(getThreadInfo()).append("]");
        }
        if (enableClassName){
            sb.append("[").append(className).append("]");
        }
        if (enableLine){
            sb.append("[").append(line).append("]");
        }
        if (enableTag){
            sb.append("[").append(tag).append("]");
        }
        sb.append(log);
        if (enableStackTrace){
            sb.append("\n").append(getStackTrace());
        }
        return sb.toString();
    }

    private static SimpleDateFormat createDateFormat(){
        return new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
    }

    private static SimpleDateFormat getDateFormat(){
        SimpleDateFormat dateFormat = threadLocal.get();
        return dateFormat;
    }

    protected String getCurTime(DateFormat dateFormat) {
        long mill = System.currentTimeMillis();
        return getFormatTime(mill, dateFormat);
    }

    protected String getFormatTime(long time, DateFormat dateFormat){
        return dateFormat.format(new Date(time));
    }

    protected String getThreadInfo(){
        Thread thread = Thread.currentThread();
        String info = String.format("Thread-%s-%d", thread.getName(), thread.getId());
        return info;
    }

    protected String getStackTrace(){
        Throwable throwable = new Throwable();
        return Log.getStackTraceString(throwable);
    }

    protected String priorityToString(int priority) {
        switch (priority) {
            case ILogger.LEVEL_VERBOSE: return "V";
            case ILogger.LEVEL_DEBUG: return "D";
            case ILogger.LEVEL_INFO: return "I";
            case ILogger.LEVEL_WARN: return "W";
            case ILogger.LEVEL_ERROR: return "E";
            case ILogger.LEVEL_ASSERT: return "A";
            default: return "UNKNOWN";
        }
    }

}