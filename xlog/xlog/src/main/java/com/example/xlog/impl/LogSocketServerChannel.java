package com.example.xlog.impl;

import com.example.xlog.network.SocketServer;

/**
 * Created by liuqi on 2020/6/15.
 * App端是作为Socket服务端发出数据，另外一端/多端作为客户端接收日志
 * 内部专用的Socket渠道，外部不要直接使用了，最终都是发往同一个SocketServer。
 * 外部只需要调用：EsLogManager.getInstance().startLogServer()，日志库自动将所有EsLog的
 */

public final class LogSocketServerChannel extends LogChannelBase {

    private static final SocketServer socketServer = SocketServer.getInstance();


    public LogSocketServerChannel() {
        LogFormatStd logFormatStd = new LogFormatStd();
        logFormatStd.setEnableTime(true).setEnableTag(true).setEnableClassName(true).setEnableLevel(true).setEnableLine(true).setEnableThreadInfo(true);
        setLogFormat(logFormatStd);
    }

    @Override
    public void log(int priority, String className, int line, String tag, String log) {
        String log2 = getLogFormat().formatLog(priority, className, line, tag, log);
        socketServer.post(getLog().getLogId(), log2);
    }
}
