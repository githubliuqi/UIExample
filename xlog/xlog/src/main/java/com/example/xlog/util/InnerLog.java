package com.example.xlog.util;

/**
 * 日志库内部的日志信息，外部不要使用
 */
public final class InnerLog {

    private static final Boolean LOG_NEABLE = true;

    private InnerLog(){}

    public static void sysout(String log){
        String caller = getCaller();
        if (LOG_NEABLE && caller.startsWith("com.excelsecu.log")){
            System.out.println(log);
        }
    }

    private static String getCaller() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length > 3) {
            return stackTraceElements[3].getClassName();
        }
        return "";
    }
}
