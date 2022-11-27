package com.example.xlog.impl;

import android.text.TextUtils;

import com.example.xlog.LogConfig;
import com.example.xlog.api.ILogger;
import com.example.xlog.api.ILogChannel;
import com.example.xlog.api.ILogFormat;
import com.example.xlog.util.ObjectUtil;
import com.example.xlog.util.ThreadUncaughtExceptionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liuqi on 2019/12/31.
 */

public class LoggerBase implements ILogger {

    private static final String TAG = LoggerBase.class.getSimpleName();

    private boolean enableLog = false;

    private int logLevel = ILogger.LEVEL_VERBOSE;

    private String logId = "";

    private ILogFormat logFormat = new LogFormatStd();

    private final Set<ILogChannel> channelSet = new HashSet<>();

    private static final LogConfig logConfig = LogConfig.getInstance();

    @Override
    public void setEnableLog(boolean enable) {
        this.enableLog = enable;
    }

    @Override
    public boolean getEnableLog() {
        return enableLog;
    }

    @Override
    public void setLogId(String logId){
        this.logId = logId;
    }

    @Override
    public String getLogId(){
        return logId;
    }

    @Override
    public void log(int priority, String tag, String msg) {
        log(priority, null, 0, tag, msg);
    }

    @Override
    public void log(int priority, String file, int line, String tag, String msg) {
        if (!enableLog || priority < logLevel){
            return;
        }
        Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
        if (null == handler || !(handler instanceof ThreadUncaughtExceptionHandler)) {
            ThreadUncaughtExceptionHandler handler1 = new ThreadUncaughtExceptionHandler(this, handler);
            Thread.setDefaultUncaughtExceptionHandler(handler1);
        }
        synchronized (channelSet){
            String className = file;
            int line0 = line;
            if (TextUtils.isEmpty(file)){
                String[] array = getClassAndLine();
                className = array[0];
                line0  = Integer.parseInt(array[1]);
            }
            for (ILogChannel channel : channelSet){
                channel.log(priority, className, line0, tag, msg);
            }
        }
    }

    private static String[] getClassAndLine(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        if (null == stackTraceElements || stackTraceElements.length == 0){
            return new String[]{"","0"};
        }
        Set<String> set = logConfig.getLogEntryClasses();
        StackTraceElement preElement = stackTraceElements[0];
        for (int i = stackTraceElements.length-1; i >= 0; i--){
            StackTraceElement element = stackTraceElements[i];
            String className = element.getClassName();
            if (set.contains(className)){
                return new String[]{preElement.getClassName(), ""+preElement.getLineNumber()};
            }
            preElement = element;
        }
        return new String[]{"","0"};
    }

    @Override
    public void warn(String tag, String msg) {
        log(LEVEL_WARN, tag, msg);
    }

    @Override
    public void info(String tag, String msg) {
        log(LEVEL_INFO, tag, msg);
    }

    @Override
    public void debug(String tag, String msg) {
        log(LEVEL_DEBUG, tag, msg);
    }

    @Override
    public void error(String tag, String msg) {
        log(LEVEL_ERROR, tag, msg);
    }

    @Override
    public void asset(String tag, String msg) {
        log(LEVEL_ASSERT, tag, msg);
    }

    @Override
    public void verbose(String tag, String msg) {
        log(LEVEL_VERBOSE, tag, msg);
    }

    @Override
    public void warn(String msg) {
        warn(TAG, msg);
    }

    @Override
    public void info(String msg) {
        info(TAG, msg);
    }

    @Override
    public void debug(String msg) {
        debug(TAG, msg);
    }

    @Override
    public void error(String msg) {
        error(TAG, msg);
    }

    @Override
    public void asset(String msg) {
        asset(TAG, msg);
    }

    @Override
    public void verbose(String msg) {
        verbose(TAG, msg);
    }

    @Override
    public void warn(Object tag, Object msg) {
        warn(objectToString(tag), objectToString(msg));
    }

    @Override
    public void info(Object tag, Object msg) {
        info(objectToString(tag), objectToString(msg));
    }

    @Override
    public void debug(Object tag, Object msg) {
        debug(objectToString(tag), objectToString(msg));
    }

    @Override
    public void error(Object tag, Object msg) {
        error(objectToString(tag), objectToString(msg));
    }

    @Override
    public void asset(Object tag, Object msg) {
        asset(objectToString(tag), objectToString(msg));
    }

    @Override
    public void verbose(Object tag, Object msg) {
        verbose(objectToString(tag), objectToString(msg));
    }

    @Override
    public void warn(Object msg) {
        warn(TAG, msg);
    }

    @Override
    public void info(Object msg) {
        info(TAG, msg);
    }

    @Override
    public void debug(Object msg) {
        debug(TAG, msg);
    }

    @Override
    public void error(Object msg) {
        error(TAG, msg);
    }

    @Override
    public void asset(Object msg) {
        asset(TAG, msg);
    }

    @Override
    public void verbose(Object msg) {
        verbose(TAG, msg);
    }

    @Override
    public void addChannel(ILogChannel channel) {
        if (null == channel){
            return;
        }
        synchronized (channelSet){
            if (!channelSet.contains(channel)){
                channelSet.add(channel);
                ((LogChannelBase)channel).setLog(this);
//                InnerLog.sysout("addChannel:\n"+channel);
            }
        }
    }

    @Override
    public void removeChannel(ILogChannel channel) {
        if (null != channel){
            synchronized (channelSet){
                channelSet.remove(channel);
            }
        }
    }

    @Override
    public Set<ILogChannel> getChannels() {
        return channelSet;
    }

    @Override
    public void setLogFormat(ILogFormat logFormat) {
        if (null == logFormat){
            return;
        }
        this.logFormat = logFormat;
        synchronized (channelSet){
            for(ILogChannel channel : channelSet){
                channel.setLogFormat(logFormat);
            }
        }
    }

    @Override
    public ILogFormat getLogFormat() {
        return logFormat;
    }

    @Override
    public void setLogLevel(int level) {
        logLevel = level;
    }

    @Override
    public int getLogLevel() {
        return logLevel;
    }

    @Override
    public void reset() {
        synchronized (channelSet){
            for (ILogChannel channel : channelSet){
                channel.reset();
            }
        }
    }

    @Override
    public String toString() {

        JSONObject jsonObject = new JSONObject();
        JSONObject logObject = new JSONObject();

        try {
            jsonObject.put("LogId", getLogId());
            jsonObject.put("LogLevel", getLogLevel());
            jsonObject.put("LogEnable", getEnableLog());
            JSONArray jsonArray = new JSONArray();
            for (ILogChannel channel : getChannels()){
                JSONObject object = new JSONObject(channel.toString());
                jsonArray.put(object);
            }
            jsonObject.put("LogChannel-Array", jsonArray);
            logObject.put("Logger", jsonObject);
            return logObject.toString(4);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String objectToString(Object object){

       return ObjectUtil.objectToString(object);
    }

}