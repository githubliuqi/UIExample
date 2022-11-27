package com.example.xlog.impl;

import com.example.xlog.api.ILogChannel;
import com.example.xlog.api.ILogger;
import com.example.xlog.api.ILogFormat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liuqi on 2019/12/31.
 */

public abstract class LogChannelBase implements ILogChannel {

    protected ILogger logger;

    private static final ILogFormat logFormat0 = new LogFormatStd();

    protected ILogFormat logFormat = null;

    @Override
    public ILogger getLog() {
        return logger;
    }

    protected void setLog(ILogger logger){
        this.logger = logger;
    }

    @Override
    public abstract void log(int priority, String className, int line, String tag, String log) ;

    @Override
    public void setLogFormat(ILogFormat logFormat) {
        this.logFormat = logFormat;
    }

    @Override
    public ILogFormat getLogFormat() {
        if (null != logFormat){
            return logFormat;
        }else if (null != logger.getLogFormat()){
            return logger.getLogFormat();
        }
        return logFormat0;
    }

    @Override
    public void reset(){}

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("class", getClass());
            jsonObject.put("Logger-logId", getLog().getLogId());
            return jsonObject.toString(4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}