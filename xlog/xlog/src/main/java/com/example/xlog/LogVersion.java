package com.example.xlog;

/**
 * Created by liuqi on 2020/3/29.
 */

public final class LogVersion {

    /**
     * 日志库版本号
     */
    private static final String LOG_VERSION = "1.0.8";

    /**
     * 最新更新日期
     */
    private static final String LAST_UPDATE_DATE = "2020-06-16";


    public static String getLogVersion(){
        return LOG_VERSION;
    }

    public static String getLastUpdateDate() {
        return LAST_UPDATE_DATE;
    }
}
