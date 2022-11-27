package com.example.xlog.impl;

/**
 * 日志库内部的日志信息，外部不要使用
 */
public final class InnerLog {

    private static final Boolean LOG_NEABLE = true;

    private InnerLog(){}

    protected static void sysout(String log){
        if (LOG_NEABLE){
            System.out.println(log);
        }
    }
}
