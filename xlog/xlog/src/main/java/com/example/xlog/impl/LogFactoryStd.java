package com.example.xlog.impl;

import com.example.xlog.api.ILogger;
import com.example.xlog.api.ILogFactory;

/**
 * Created by liuqi on 2020/1/2.
 */

public class LogFactoryStd implements ILogFactory {

    @Override
    public ILogger createEsLog() {
        return new LoggerStd();
    }
}
