package com.example.xlog.util;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.xlog.BuildConfig;
import com.example.xlog.LogManager;
import com.example.xlog.LogVersion;
import com.example.xlog.api.ILogger;
import com.example.xlog.api.ILogChannel;
import com.example.xlog.impl.LogChannelFactory;
import com.example.xlog.impl.LogFormatStd;

import java.io.File;


/**
 * 日志工具类，集中控制日志的打印 后期考虑打印入file或者上传到服务器等策略
 *
 * @author liuqi
 * @version 1.1
 * @date 2020-01-25
 */
public class LogUtil {
    /**
     * 默认的日志Tag标签
     */
    public final static String DEFAULT_TAG = "Java-Log";

    public final static String DEFAULT_LOG_ID = "Logger-Default";

    /**
     * 默认跟随编译配置。App调用时可以再次自主设置是否开启日志（ILogger.setEnableLog）
     */
    public final static boolean LOGGABLE = BuildConfig.DEBUG;

    private static ILogger logger = LogManager.getInstance().getLogger(DEFAULT_LOG_ID);

    private final static String DEFAULT_DIR = Environment.getExternalStorageDirectory().toString()
            + File.separator + "nlog" + String.format("/%s", PhoneUtil.getProcessName());

    private static boolean hasInit = false;

    static {
       init();
    }

    public static final String getDefaultLogFileName(){
        String name = String.format("nlog(%s-%s-Android%s).txt", Build.BRAND, Build.BOARD, Build.VERSION.RELEASE);
        return name;
    }

    /**
     * 外部最好先调用下init方法
     */
    public static final void init(){
        /**
         * 初始化一个默认的EsLog，它包含2个输出渠道：LogCat和LogFile.
         * 有时候，在不同的场景下，需要独立的日志输出，则创建不同的EsLog(id区分)。
         */
        synchronized (LogUtil.class){
            if (hasInit){
                return;
            }
            hasInit = true;
        }
        System.out.println("************* NLog init *************");
        System.out.println("NLog-Version:"+ LogVersion.getLogVersion());
        registerLogEntryClass();
        LogFormatStd format = (LogFormatStd)logger.getLogFormat();
        format.setEnableStackTrace(false);
        format.setEnableThreadInfo(false);
        format.setEnableTag(true);
        logger.setEnableLog(LOGGABLE);
        String logPath = String.format("%s/%s", DEFAULT_DIR, getDefaultLogFileName());
        File logFile = new File(logPath);
        ILogChannel fileChannel = LogChannelFactory.getInstance().createRollFileChannel(logFile, PhoneUtil.getPhoneInfo());
        logger.addChannel(fileChannel);
        System.out.println("************* NLog init success!!! *************");
        logger.debug(logger);
        System.out.println(logger.toString());
    }

    /**
     * 注册一下日志的入口类，方便日志库内部自动查找调用日志的类信息。
     */
    private static void registerLogEntryClass(){
        // EsLogConfig默认会自动加上这2个类
//        LogConfig.getInstance().addLogEntryClass(LogUtil.class).addLogEntryClass(LoggerBase.class);
    }

    public static String getAppLogRoot(){
        return DEFAULT_DIR;
    }


    public static void log(int priority, String file, int line, String tag, String str) {
        log((Integer)priority, file, line, tag, str);
    }

    public static void log(Integer priority, String file, int line, String tag, String str) {
        if (!hasInit){
            init();
        }
        logger.log(priority, file, line, tag, str);
    }

    /**
     * sdk 反射调用过来
     *
     * @param logId 为空时表示默认日志器ID：ILogger-Dafault. sdk端不需要知道默认ID是啥。
     * @param priority 日志等级
     * @param file 文件名或java类名
     * @param line 行号
     * @param tag tag标记
     * @param str 日志内容
     */
    public static void log(String logId, Integer priority, String file, int line, String tag, String str) {
        if (DEFAULT_LOG_ID.equals(logId) || TextUtils.isEmpty(logId)){
            log(priority, file, line, tag, str);
            return;
        }
        LogManager.getInstance().getLogger(logId).log(priority, file, line, tag, str);
    }

    /**
     * sdk 反射调用过来
     *
     * @param logId 为空时表示默认日志器ID：ILogger-Dafault. sdk端不需要知道默认ID是啥。
     * @param priority 日志等级
     * @param file 文件名或java类名
     * @param line 行号
     * @param tag tag标记
     * @param str 日志内容
     */
    public static void log(String logId, int priority, String file, int line, String tag, String str) {
        log(logId, (Integer) priority, file, line, tag, str);
    }

    /**
     * sdk 反射调用过来
     *
     * @param logId 为空时表示默认日志器ID：ILogger-Dafault. sdk端不需要知道默认ID是啥。
     * @param priority 日志等级
     * @param tag tag标记
     * @param str 日志内容
     */
    public static void log(String logId, Integer priority, String tag, String str) {
        log(logId, priority, "", 0, tag, str);
    }

    public static void v(String tag, String str) {
        log(ILogger.LEVEL_VERBOSE, "", 0, tag, str);
    }

    public static void v(String str) {
        v(DEFAULT_TAG, str);
    }

    public static void d(String tag, String str) {
        log(ILogger.LEVEL_DEBUG, "", 0, tag, str);
    }

    public static void d(String str) {
        d(DEFAULT_TAG, str);
    }

    public static void i(String tag, String str) {
        log(ILogger.LEVEL_INFO, "", 0, tag, str);
    }

    public static void i(String str) {
        i(DEFAULT_TAG, str);
    }

    public static void w(String tag, String str) {
        log(ILogger.LEVEL_WARN, "", 0, tag, str);
    }

    public static void w(String str) {
        w(DEFAULT_TAG, str);
    }

    public static void e(String tag, String str) {
        log(ILogger.LEVEL_ERROR, "", 0, tag, str);
    }

    public static void e(String str) {
        e(DEFAULT_TAG, str);
    }

    public static void e(String tag, Throwable throwable) {
        log(ILogger.LEVEL_ERROR, "", 0, tag,  Log.getStackTraceString(throwable));
    }

    public static void e(String tag, String msg, Throwable throwable) {
        log(ILogger.LEVEL_ERROR, "", 0, tag, msg + '\n' + Log.getStackTraceString(throwable));
    }

    public static void d(Object object){
        d(ObjectUtil.objectToString(object));
    }

    public static void d(Object tag, Object object){
        d(ObjectUtil.objectToString(tag), ObjectUtil.objectToString(object));
    }


    /**
     * 将字节流用十六进制字符串的方式显示出来
     *
     * @param tag
     * @param data
     */
    public static void d(String tag, byte[] data) {
        d(tag, BytesUtil.bytesToHexString(data));
    }

    public static void d(String tag, String format, Object... params) {
        d(tag, String.format(format, params));
    }

    public static void e(String tag, String format, Object... params) {
        e(tag, String.format(format, params));
    }

    public static void i(String tag, String format, Object... params) {
        i(tag, String.format(format, params));
    }


}
