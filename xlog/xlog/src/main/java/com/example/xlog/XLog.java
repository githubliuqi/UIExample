package com.example.xlog;

import com.example.xlog.api.ILogger;

public class XLog {

    private static final ILogger LOGGER = LogManager.getInstance().getLogger("XLog");

    static {
        LOGGER.error(LOGGER);
    }

    public static final void i(String tag, String log) {
        LOGGER.info(tag, log);
    }

    public static final void d(String tag, String log) {
        LOGGER.debug(tag, log);
    }

    public static final void w(String tag, String log) {
        LOGGER.warn(tag, log);
    }

    public static final void e(String tag, String log) {
        LOGGER.error(tag, log);
    }
}
