package com.example.tetris.model;

import android.content.Context;

import com.example.common.TLog;

public class TetrisGame {

    private static final String TAG = "TetrisGame";

    private final Context context;
    private Runnable activeBoxTask;
    private ActiveBoxThread activeBoxThread;

    private int score = 0;

    public TetrisGame(Context context) {
        this.context = context;
    }

    public TetrisGame setActiveBoxTask(Runnable task) {
        activeBoxTask = task;
        return this;
    }

    public TetrisGame start() {
        startActiveBox();
        return this;
    }

    public TetrisGame stop() {
        if (activeBoxThread != null) {
            activeBoxThread.stopThread();
        }
        return this;
    }

    public TetrisGame startActiveBox() {
        if (activeBoxTask == null) {
            TLog.e(TAG, "activeBoxTask is null");
            return this;
        }
        activeBoxThread = new ActiveBoxThread();
        activeBoxThread.setTask(activeBoxTask);
        activeBoxThread.start();
        return this;
    }

    public TetrisGame pauseActiveBox() {
        if (activeBoxThread == null) {
            return this;
        }
        return this;
    }

    public int getScore() {
        return score;
    }

    public TetrisGame setScore(int score) {
        this.score = score;
        return this;
    }

    public TetrisGame setInterval(long interval) {
        if (activeBoxThread == null) {
            TLog.e(TAG, "activeBoxThread is not start");
            return this;
        }
        activeBoxThread.setInterval(interval);
        return this;
    }

    private static final class ActiveBoxThread extends Thread {
        private static final int MIN_INTERVAL_MS = 200;
        private static final int MAX_INTERVAL_MS = 1200;
        private static final int MIDDLE_INTERVAL_MS =  (MIN_INTERVAL_MS + MAX_INTERVAL_MS) / 2;
        private volatile boolean bRunning = true;
        long interval = MIDDLE_INTERVAL_MS;
        Runnable task;

        public ActiveBoxThread setInterval(long interval) {
            if (interval >= MIN_INTERVAL_MS && interval <= MAX_INTERVAL_MS) {
                this.interval = interval;
            } else {
                TLog.e(TAG, String.format("interval = %s, not in [%s, %s]", interval, MIN_INTERVAL_MS, MAX_INTERVAL_MS));
            }
            return this;
        }

        public ActiveBoxThread setTask(Runnable task) {
            this.task = task;
            return this;
        }

        public ActiveBoxThread stopThread() {
            bRunning = false;
            return this;
        }

        @Override
        public void run() {
            while (bRunning) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (task != null) {
                    task.run();
                }
            }
        }
    }
}
