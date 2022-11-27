package com.example.xlog.impl;

import com.example.xlog.api.ILogChannel;

/**
 * Created by liuqi on 2019/12/31.
 *
 * 默认的Logger只有1个输出渠道：Logcat
 */

public final class LoggerStd extends LoggerBase {

    private static final LogChannelFactory logChannelFactory = LogChannelFactory.getInstance();


    public LoggerStd(){
        init();
    }

    private void init(){

        setEnableLog(true);

        setLogFormat(new LogFormatStd());

        ILogChannel logcatChannel = logChannelFactory.createLogcatChannel();
        addChannel(logcatChannel);
    }

}