package com.example.xlog.impl;

import com.example.xlog.api.ILogChannel;

import java.io.File;

/**
 * Created by liuqi on 2019/12/31.
 */

public class LogChannelFactory {

    private static final LogChannelFactory factory = new LogChannelFactory();

    private LogChannelFactory(){}

    public static final LogChannelFactory getInstance(){
        return factory;
    }

    public ILogChannel createLogcatChannel(){
        return new LogcatChannel();
    }

    public ILogChannel createFileChannel(File logFile){
        LogFileChannel fileChannel = new LogFileChannel(logFile);
        return fileChannel;
    }

    public ILogChannel createRollFileChannel(File logFile){
        LogFileChannel fileChannel = new LogRollFileChannel(logFile);
        return fileChannel;
    }

    public ILogChannel createFileChannel(File logFile, String firstInfo){
        LogFileChannel fileChannel = new LogFileChannel(logFile, firstInfo);
        return fileChannel;
    }

    public ILogChannel createRollFileChannel(File logFile, String firstInfo){
        LogFileChannel fileChannel = new LogRollFileChannel(logFile, firstInfo);
        return fileChannel;
    }
}