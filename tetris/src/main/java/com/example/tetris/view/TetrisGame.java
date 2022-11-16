package com.example.tetris.view;

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

    private static final class ActiveBoxThread extends Thread {

        private volatile boolean bRunning = true;
        long interval = 500; // 回调间隔 1s
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
