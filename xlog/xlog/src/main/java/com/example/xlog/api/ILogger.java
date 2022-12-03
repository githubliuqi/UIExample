package com.example.xlog.api;

import android.util.Log;

import java.util.Set;
import java.util.logging.Level;

/**
 * Created by liuqi on 2019/12/31.
 * <p>
 * 每个日志器可以添加多个日志输出渠道，每一条日志记录都会发给日志器的所有日志渠道
 */

public interface ILogger {

    /**
     * log-level
     */
    int LEVEL_VERBOSE = Log.VERBOSE;
    int LEVEL_DEBUG = Log.DEBUG;
    int LEVEL_INFO = Log.INFO;
    int LEVEL_WARN = Log.WARN;
    int LEVEL_ERROR = Log.ERROR;
    int LEVEL_ASSERT = Log.ASSERT;

    /**
     * set enable to output log , if false do nothing
     *
     * @param enable
     */
    void setEnableLog(boolean enable);

    boolean getEnableLog();

    void setLogId(String logId);

    String getLogId();

    /**
     * output log
     *
     * @param msg log info
     */
    void log(int priority, String tag, String msg);

    void log(int priority, String file, int line, String tag, String msg);

    void warn(String tag, String msg);

    void info(String tag, String msg);

    void debug(String tag, String msg);

    void error(String tag, String msg);

    void asset(String tag, String msg);

    void verbose(String tag, String msg);

    void warn(String msg);

    void info(String msg);

    void debug(String msg);

    void error(String msg);

    void asset(String msg);

    void verbose(String msg);


    void warn(Object tag, Object msg);

    void info(Object tag, Object msg);

    void debug(Object tag, Object msg);

    void error(Object tag, Object msg);

    void asset(Object tag, Object msg);

    void verbose(Object tag, Object msg);


    void warn(Object msg);

    void info(Object msg);

    void debug(Object msg);

    void error(Object msg);

    void asset(Object msg);

    void verbose(Object msg);

    /**
     * add a channel on this ILogger to output log
     */
    void addChannel(ILogChannel channel);

    /**
     * remove a channel on this ILogger to output log
     */
    void removeChannel(ILogChannel channel);

    /**
     * get all channels on this ILogger to output log
     */
    public Set<ILogChannel> getChannels();

    /**
     * set ILogFormat to format the log for all channels of this ILogger
     *
     * @param logFormat
     */
    void setLogFormat(ILogFormat logFormat);

    ILogFormat getLogFormat();

    /**
     * set ILogger level (under the level , will not output)
     *
     * @param level
     */
    void setLogLevel(int level);

    public int getLogLevel();

    /**
     * reset all channels .
     * for FileChannel : clear the logFile
     */
    void reset();

    /**
     * Level 转成字符串
     *
     * @param level
     * @return
     */
    static String levelToString(int level) {
        switch (level) {
            case LEVEL_VERBOSE:
                return "V";
            case LEVEL_DEBUG:
                return "D";
            case LEVEL_INFO:
                return "I";
            case LEVEL_WARN:
                return "W";
            case LEVEL_ERROR:
                return "E";
            case LEVEL_ASSERT:
                return "A";
        }
        return "NULL";
    }
}