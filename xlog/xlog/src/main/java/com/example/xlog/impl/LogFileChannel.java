package com.example.xlog.impl;

import com.example.xlog.util.FileOpt;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuqi on 2019/12/31.
 */

public class LogFileChannel extends LogChannelBase {

    protected String logPath;

    protected final Object lock = new Object();

    protected ExecutorService signleThreadExecutor = Executors.newSingleThreadExecutor();

    protected FileOpt fileOpt = null;

    /**
     * 生成文件时，首先写入的内容
     */
    protected String firstInfo;

    public LogFileChannel(String logPath, String firstInfo) {
        this.logPath = logPath;
        this.firstInfo = firstInfo;
    }

    public LogFileChannel(File logFile, String firstInfo) {
        this(logFile.getAbsolutePath(), firstInfo);
    }

    public LogFileChannel(File logFile) {
        this(logFile.getAbsolutePath(), "");
    }

    @Override
    public void log(final int priority, final String className, final int line, final String tag, final String log) {
        signleThreadExecutor.execute(new Runnable() {

            @Override
            public void run() {
                String log0 = getLogFormat().formatLog(priority, className, line, tag, log);
                synchronized (lock) {
                    if (null == fileOpt){
                        fileOpt = new FileOpt(getLogPath());
                    }
                    fileOpt.write(fileOpt.addNewLine(log0));
                }
            }
        });
    }

    public String getLogPath(){
       return logPath;
    }

    public File getLogFile(){
        return new File(getLogPath());
    }

    public void clearLogFile(){
        if (null == logPath){
            return ;
        }
        synchronized (lock){
            if (null != fileOpt){
                fileOpt.clear();
            }else {
                new File(logPath).delete();
            }
        }
    }

    @Override
    public void reset() {
        clearLogFile();
    }

    @Override
    public String toString() {

        try {
            JSONObject jsonObject = new JSONObject(super.toString());
            jsonObject.put("logFilePath", getLogFile().getAbsolutePath());
            return jsonObject.toString(4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}