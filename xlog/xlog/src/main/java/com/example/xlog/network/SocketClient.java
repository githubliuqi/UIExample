package com.example.xlog.network;

import android.text.TextUtils;

import com.example.xlog.util.InnerLog;
import com.example.xlog.util.ThreadService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by liuqi on 2020/6/13.
 */

public final class SocketClient {

    private Socket socket = null;

    private static final int CACHE_SIZE = 1000;

    private final List<String> messages = new ArrayList<>(CACHE_SIZE);


    private final AtomicBoolean stopedRef = new AtomicBoolean(false);
    
    private final AtomicBoolean autoRestartedRef = new AtomicBoolean(false);

    private String serverIp = "";

    private int serverPort = 0;


    public SocketClient(){

    }

    public void start(final String ip, final int port) {
        ThreadService.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                startClient(ip, port);
            }
        });
    }

    private void startClient(String ip, int port) {
        serverIp = ip;
        serverPort = port;
        InnerLog.sysout("startClient: ");
        boolean connect = true;
        while (connect) {
            if (stopedRef.get()) {
                return;
            }
            sleep(5000);
            try {
                InnerLog.sysout("正在尝试连接服务器...");
                synchronized (this) {
                    if (null != socket) {
                        return;
                    }
                    socket = new Socket(ip, port);
                    connect = false;
                }
                InnerLog.sysout("启动客户端:"+socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            OutputStream outputStream = socket.getOutputStream();
            final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            ThreadService.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            while(!stopedRef.get()){
                                if (messages.isEmpty()) {
                                    sleep(1000);
                                    continue;
                                }
                                String data = null;
                                synchronized (messages){
                                    if (!messages.isEmpty()) {
                                        data = messages.remove(0);
                                    }
                                }
                                if (TextUtils.isEmpty(data)){
                                    continue;
                                }
                                try {
                                    bw.write(data+"\n");
                                    bw.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }
                            try {
                                bw.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            stop();
                        }
                    }
            );
        }catch (IOException e){
            e.printStackTrace();
            stop();
        }
    }

    /**
     * 提交数据
     * @param loggerId 日志器ID
     * @param message
     */
    public void post(String loggerId, String message) {
        synchronized (messages){
            if (messages.size() >= CACHE_SIZE) {
                messages.remove(0);
            }
            messages.add(loggerId+"@# "+message);
        }
        if (null == socket && 0 != serverPort && !"0.0.0.0".equals(serverIp)) {
            autoRestart();
        }
    }

    private void autoRestart() {
        if (autoRestartedRef.get() || null != socket) {
            return;
        }
        autoRestartedRef.set(true);
        ThreadService.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                startClient(serverIp, serverPort);
                autoRestartedRef.set(false);
            }
        });

    }

    public void stop(){
        stopedRef.set(true);
        synchronized (messages) {
            messages.clear();
        }
        synchronized (this) {
            if (null != socket) {
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        stopedRef.set(false);
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
