package com.example.xlog.impl;

import android.text.TextUtils;

import com.example.xlog.util.FileOpt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liuqi on 2019/12/31.
 *
 * 每天生成一个日志文件，名为规范是baseLogName+date+suffix
 */

public class LogRollFileChannel extends LogFileChannel {

    private static final ThreadLocal<SimpleDateFormat> threadLocalDate = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }
    };

    private String dayLogPath;

    private final String baseLogName;

    private final String logDirPath;

    private final String logFileSuffix;

    public LogRollFileChannel(String logPath, String firstInfo) {
        this(new File(logPath), firstInfo);
    }

    public LogRollFileChannel(String logPath) {
        this(new File(logPath), "");
    }

    public LogRollFileChannel(File logFile) {
        this(logFile, "");
    }

    public LogRollFileChannel(File logFile, String firstInfo) {
        super(logFile, firstInfo);
        logDirPath = logFile.getParent();
        baseLogName = FileOpt.getFileName(logFile);
        logFileSuffix = FileOpt.getFileSuffix(logFile);
    }

    @Override
    public void log(final int priority, final String className, final int line, final String tag, final String log) {

        signleThreadExecutor.execute(new Runnable() {

            @Override
            public void run() {
                synchronized (lock) {
                    String log0 = getLogFormat().formatLog(priority, className, line, tag, log);
                    if (null == fileOpt){
                        fileOpt = new FileOpt(getLogPath(), firstInfo);
                    }else {
                        String date = threadLocalDate.get().format(new Date());
                        if (!fileOpt.getFile().getName().contains(date)){
                            fileOpt = new FileOpt(getLogPath(), firstInfo);
                        }
                    }
                    fileOpt.write(fileOpt.addNewLine(log0));
                }
            }
        });
    }

    @Override
    public String getLogPath() {
        String date = threadLocalDate.get().format(new Date());
        if (TextUtils.isEmpty(dayLogPath) || !dayLogPath.contains(date)){
            String suffix = TextUtils.isEmpty(logFileSuffix) ? ".txt" : logFileSuffix;
            dayLogPath = String.format("%s/%s%s%s", logDirPath, baseLogName, date, suffix);
        }
        return dayLogPath;
    }

    @Override
    public void clearLogFile() {
        File file = new File(logDirPath);
        File[] files = file.listFiles();
        if (null == files){
            return;
        }
        synchronized (lock){
            for (int i=0; i< files.length; i++){
                if (files[i].getName().startsWith(baseLogName)){
                    files[i].delete();
                    InnerLog.sysout("删除文件："+files[i]);
                }
            }
        }
    }
}