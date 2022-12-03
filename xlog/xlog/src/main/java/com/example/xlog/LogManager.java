package com.example.xlog;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.example.xlog.api.ILogger;
import com.example.xlog.api.ILogFactory;
import com.example.xlog.dialog.SettingsDialog;
import com.example.xlog.impl.LogSocketServerChannel;
import com.example.xlog.impl.LogFactoryStd;
import com.example.xlog.network.SocketServer;
import com.example.xlog.util.PhoneUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by liuqi on 2019/12/31.
 */

public final class LogManager {

    private final HashMap<String, ILogger> logMap = new HashMap<>();

    private final ILogFactory logFactory = new LogFactoryStd();

    private final AtomicBoolean startedServerRef = new AtomicBoolean(false);
    private final AtomicBoolean initedLogClientRef = new AtomicBoolean(false);

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    private static final LogManager logManager = new LogManager();

    private LogManager(){}

    public static LogManager getInstance(){
        return logManager;
    }

    public ILogger getLogger(String logId){
        return getLogger(logId, logFactory);
    }

    public synchronized ILogger getLogger(String logId, ILogFactory factory){
        ILogger logger = logMap.get(logId);
        if (null == logger){
            ILogFactory factory0 = null == factory ? logFactory : factory;
            logger = factory0.createEsLog();
            logger.setLogId(logId);
            logMap.put(logId, logger);
//            InnerLog.sysout("createEsLog:\n"+logger);
        }
        return logger;
    }

    public synchronized ILogger removeLogger(String logId){
        ILogger logger = logMap.get(logId);
        if (null != logger){
            return logMap.remove(logId);
        }
        return null;
    }

    /**
     * 启动日志SocketServer，所有日志器的日志都会向SocketServer输出。外部不需要给EsLog addChannel。
     * 为什么不是启动client？client启动时需要知道server的IP和Port，
     * 这需要支持UI操作，如不嫌麻烦，可以调用initLogClient。
     * @param port
     */
    public synchronized void startLogServer(int port) {
        if (startedServerRef.get()) {
            return;
        }
        startedServerRef.set(true);
        SocketServer socketServer = SocketServer.getInstance();
        socketServer.start(port);
        for(ILogger logger : logMap.values()) {
            LogSocketServerChannel socketChannel = new LogSocketServerChannel();
            logger.addChannel(socketChannel);
            socketServer.post(logger.getLogId(), PhoneUtil.getPhoneInfo());
        }
    }

    /**
     * 通过UI进行日志库配置
     * 目前配置socket-server的IP和Port，用于将App的日志发送到server（比如PC）
     * @param activity
     */
    public synchronized void initLogClient(final Activity activity) {
        if (initedLogClientRef.get()) {
            return;
        }
        initedLogClientRef.set(true);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                SettingsDialog dialog = new SettingsDialog(activity);
                dialog.setTitle("日志库配置");
                dialog.show();
            }
        });
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        JSONObject logObject = new JSONObject();

        try {
            JSONArray jsonArray = new JSONArray();
            for (ILogger logger : logMap.values()){
                JSONObject object = new JSONObject(logger.toString());
                jsonArray.put(object);
            }
            jsonObject.put("LoggerArray", jsonArray);
            logObject.put("LogManager", jsonObject);
            return logObject.toString(4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
