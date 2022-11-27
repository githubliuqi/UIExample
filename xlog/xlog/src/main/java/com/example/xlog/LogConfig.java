package com.example.xlog;

import android.text.TextUtils;

import com.example.xlog.impl.LoggerBase;
import com.example.xlog.util.LogUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liuqi on 2020/4/18.
 */

public class LogConfig {

    public static final String KEY_LOG_ENTRY_CLASSES = "log-entry-classes";
    public static final String KEY_LOG_SERVER_IP = "log-server-ip";
    public static final String KEY_LOG_SERVER_PORT = "log-server-port";

    private static String oldLogEntryClasses = "";

    private static final Set<String> logEntryClassesSet = new HashSet<>();

    private static final LogConfig instance = new LogConfig();
    

    private LogConfig(){
        logEntryClassesSet.add(LogUtil.class.getName());
        logEntryClassesSet.add(LoggerBase.class.getName());
    }


    public static final LogConfig getInstance(){
        return instance;
    }

    public synchronized final Set<String> getLogEntryClasses(){

        String values = System.getProperty(KEY_LOG_ENTRY_CLASSES);
        if (TextUtils.equals(values, oldLogEntryClasses)){
           return logEntryClassesSet;
        }else {
            logEntryClassesSet.addAll(toList(values));
        }
        return logEntryClassesSet;
    }

    private static final Set<String> toList(String logEntryClasses){
        Set<String> set = new HashSet<>();
        if (TextUtils.isEmpty(logEntryClasses)){
            return set;
        }
        String[] valArray = logEntryClasses.split(";");
        for (String s : valArray){
            if (!TextUtils.isEmpty(s) && !"null".equals(s)){
                set.add(s);
            }
        }
        return set;
    }

    public LogConfig addLogEntryClass(Class clazz){
        if (null == clazz){
            return this;
        }
        return addLogEntryClass(clazz.getName());
    }

    public LogConfig addLogEntryClass(String clazz){
        if (TextUtils.isEmpty(clazz)){
            return this;
        }
        final String key = KEY_LOG_ENTRY_CLASSES;
        String oldValue = System.getProperty(key);
        if (TextUtils.isEmpty(oldValue)){
            System.setProperty(key, clazz);
        }else{
            System.setProperty(key, String.format("%s;%s", oldValue, clazz));
        }
        return this;
    }

    public LogConfig setLogServer(String ip, int port) {
        System.setProperty(KEY_LOG_SERVER_IP, ip);
        System.setProperty(KEY_LOG_SERVER_PORT, String.valueOf(port));
        return this;
    }

    public String getKeyLogServerIp() {
        return System.getProperty(KEY_LOG_SERVER_IP, "0.0.0.0");
    }

    public int getKeyLogServerPort() {
        String portStr = System.getProperty(KEY_LOG_SERVER_PORT, "0");
        try {
            return Integer.parseInt(portStr);
        }catch (Exception e){

        }
        return 0;
    }

}
