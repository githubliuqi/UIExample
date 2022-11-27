package com.example.xlog.util;


import com.example.xlog.api.ILogger;

/**
 * 线程崩溃信息处理器
 * 日志库在打印日志时，设置给线程，用于捕获线程崩溃信息
 */
public class ThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    /**
     * EsLog日志器，用于记录线程崩溃信息
     */
    private ILogger logger;

    /**
     * 原有的UncaughtExceptionHandler
     */
    private Thread.UncaughtExceptionHandler parentHandler;


    public ThreadUncaughtExceptionHandler(ILogger logger, Thread.UncaughtExceptionHandler parentHandler) {
        this.logger = logger;
        this.parentHandler = parentHandler;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (null != parentHandler) {
            parentHandler.uncaughtException(t, e);
        }
        logger.error("************* uncaughtException *************");
        logger.error(t);
        logger.error(e);
        logger.error("************* uncaughtException *************");
    }
}
