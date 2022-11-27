package com.example.xlog.api;

/**
 * Created by liuqi on 2019/12/31.
 *
 * 日志输出渠道：logcat、file、network、outputStream ...
 * 根据需求自行扩展。
 */

public interface ILogChannel {

    public ILogger getLog();

    public void log(int priority, String className, int line, String tag, String log);

    public void setLogFormat(ILogFormat logFormat);

    public ILogFormat getLogFormat();

    public void reset();
}
