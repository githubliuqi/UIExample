package com.example.common;

import com.example.xlog.LogConfig;
import com.example.xlog.LogManager;
import com.example.xlog.XLog;
import com.example.xlog.api.ILogger;
import com.example.xlog.impl.LogFormatStd;

import java.util.Arrays;

public class TLog {

    static {
        ILogger logger = LogManager.getInstance().getLogger("XLog");
        LogFormatStd logFormat = (LogFormatStd) logger.getLogFormat();
        logFormat.setEnableTime(false);

        LogConfig logConfig = LogConfig.getInstance();
        logConfig.addLogEntryClass(TLog.class);
    }

    public static final void i(String tag, String log) {
        XLog.i(tag, log);
    }

    public static final void d(String tag, String log) {
        XLog.d(tag, log);
    }

    public static final void w(String tag, String log) {
        XLog.w(tag, log);
    }

    public static final void e(String tag, String log) {
        XLog.e(tag, log);
    }

    public static final String toString(Object[][] array) {
        if (array == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(Arrays.toString(array[i]));
            sb.append(",\n");
        }
        return sb.toString();
    }
}
