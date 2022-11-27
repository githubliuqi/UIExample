package com.example.xlog.util;

import com.example.xlog.LogManager;
import com.example.xlog.api.ILogger;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by liuqi on 2019/8/22.
 * 线程池服务
 */
public class ThreadService {

    private static final String TAG = ThreadService.class.getSimpleName();

    public static final ILogger log = LogManager.getInstance().getLogger(LogUtil.DEFAULT_LOG_ID);

    private final ThreadFactory threadFactory =  new ThreadFactory() {
        final private AtomicInteger id = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(String.format("Thread-%d", id.addAndGet(1)));
            ThreadUncaughtExceptionHandler handler = new ThreadUncaughtExceptionHandler(log, null);
            thread.setUncaughtExceptionHandler(handler);
            return thread;
        }
    };

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 1, TimeUnit.MINUTES, new LinkedBlockingQueue(1000) , threadFactory);


    private static final class ThreadServiceHolder {
        private static final ThreadService threadService = new ThreadService();
    }

    public static final ThreadService getInstance(){
        return ThreadServiceHolder.threadService;
    }

    public void execute(Runnable task){
        executor.execute(task);
    }

}
