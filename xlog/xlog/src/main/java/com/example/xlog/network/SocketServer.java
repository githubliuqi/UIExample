package com.example.xlog.network;

import android.text.TextUtils;

import com.example.xlog.util.InnerLog;
import com.example.xlog.util.ThreadService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 启动一个SocketServer，允许不同地向server提交数据
 * server会不停检测客户端，并向客户端输出数据。
 */
public final class SocketServer {

    private boolean stop = false;

    private static final int CACHE_SIZE = 1000;

    private final List<String> messages = new ArrayList<>(CACHE_SIZE);

    private static final SocketServer socketServer = new SocketServer();

    private static final ThreadService threadService = ThreadService.getInstance();

    private final AtomicBoolean stopedRef = new AtomicBoolean(false);


    /**
     * 默认的服务端口
     */
    private static final int DEFAULT_SERVER_PORT = 7816;

    /**
     * 接收客户端等待间隔
     */
    private static final long DEFAULT_ACCEPT_CLIENT_INTERVAL = 1000;

    private SocketServer() {

    }

    public static final SocketServer getInstance() {
        return socketServer;
    }

    public boolean start(final int port, final long interval) {
        InnerLog.sysout(String.format("port=%d, interval=%d", port, interval));
        threadService.execute(new Runnable() {
            @Override
            public void run() {
                startServer(port, interval);
            }
        });
        return true;
    }

    public boolean start(int port) {
        return start(port, DEFAULT_ACCEPT_CLIENT_INTERVAL);
    }

    public boolean start() {
        return start(DEFAULT_SERVER_PORT, DEFAULT_ACCEPT_CLIENT_INTERVAL);
    }

    private void startServer(int port, long interval) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                if (stop) {
                    break;
                }
                sleep(interval);
                final Socket socket = serverSocket.accept();
                InnerLog.sysout("accept client:"+socket);
                threadService.execute(new Runnable() {
                    @Override
                    public void run() {
                        work(socket);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != serverSocket) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void work(final Socket socket) {

        threadService.execute(new Runnable() {
            @Override
            public void run() {
                while(!stopedRef.get()){
                    if (messages.isEmpty()) {
                        sleep(1000);
                        continue;
                    }
                    String data = null;
                    synchronized (messages){
                        data = messages.remove(0);
                    }
                    if (TextUtils.isEmpty(data)){
                        continue;
                    }
                    BufferedWriter bw = null;
                    try {
                        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        bw.write(data+"\n");
                        bw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
