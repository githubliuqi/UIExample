package com.example.tetris.model;

import android.content.Context;

import com.example.common.TLog;

public class TetrisGame {

    private static final String TAG = "TetrisGame";

    private final Context context;
    private Runnable activeBoxTask;
    private ActiveBoxThread activeBoxThread;

    private long gameInterval = 1000;
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
        activeBoxThread.setInterval(gameInterval);
        activeBoxThread.start();
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
        if (interval > 0) {
            this.gameInterval = interval;
        } else {
        }
        if (activeBoxThread != null) {
            activeBoxThread.setInterval(gameInterval);
        }
        return this;
    }

    private static final class ActiveBoxThread extends Thread {
        private volatile boolean bRunning = true;
        long interval = 0;
        Runnable task;

        public ActiveBoxThread setInterval(long interval) {
            this.interval = interval;
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
