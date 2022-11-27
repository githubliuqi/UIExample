package com.example.xlog.impl;

import com.example.xlog.LogConfig;
import com.example.xlog.network.SocketClient;
import com.example.xlog.util.PhoneUtil;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by liuqi on 2020/6/15.
 * App端是作为Socket客户端发出数据，另外一端作为服务端接收日志
 * 每一个EsLog都可以单独分配一个（或多个）LogSocketClientChannel
 */

public final class LogSocketClientChannel extends LogChannelBase {

    private static final SocketClient socketClient = new SocketClient();

    private static final LogConfig logConfig = LogConfig.getInstance();

    private final AtomicBoolean startedRef = new AtomicBoolean(false);

    private final String serverIp;

    private final int serverPort;


    public LogSocketClientChannel() {
        this(logConfig.getKeyLogServerIp(), logConfig.getKeyLogServerPort());
    }

    public LogSocketClientChannel(String ip, int port) {
        LogFormatStd logFormatStd = new LogFormatStd();
        logFormatStd.setEnableTime(true).setEnableTag(true).setEnableClassName(true).setEnableLevel(true).setEnableLine(true).setEnableThreadInfo(true);
        setLogFormat(logFormatStd);
        serverIp = ip;
        serverPort = port;
    }

    @Override
    public void log(int priority, String className, int line, String tag, String log) {
        String log2 = getLogFormat().formatLog(priority, className, line, tag, log);
        socketClient.post(getLog().getLogId(), log2);
        if (startedRef.get()) {
            return;
        }
        String ip = serverIp;
        int port = serverPort;
        // 原有的IP和Port非法，则使用配置的IP和Port
        if (0 == serverPort || "0.0.0.0".equals(serverIp)) {
            ip =  logConfig.getKeyLogServerIp();
            port = logConfig.getKeyLogServerPort();
        }
        if (0 != port && !"0.0.0.0".equals(ip)){
            startedRef.set(true);
            socketClient.start(ip, port);
            socketClient.post(getLog().getLogId(), PhoneUtil.getPhoneInfo());
        }
    }

    @Override
    public void reset() {
        socketClient.stop();
        startedRef.set(false);
        InnerLog.sysout("reset LogSocketClientChannel");
    }
}
