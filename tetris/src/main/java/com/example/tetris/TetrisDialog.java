package com.example.tetris;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.juiexample.event.EventHandler;
import com.example.juiexample.event.OnEventListener;
import com.example.juiexample.utils.ToastUtils;
import com.example.tetris.view.TetrisGame;
import com.example.tetris.view.TetrisLayout;

public class TetrisDialog extends Dialog implements OnEventListener {

    private final Context context;
    private final TetrisGame game;
    private final TetrisLayout tetrisLayout;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    public TetrisDialog(Context context) {
        super(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        this.context = context;
        tetrisLayout = new TetrisLayout(context);
        tetrisLayout.setOnEventListener(this::onEvent);
        EventHandler.getInstance()
                .addOnEventListener(GameDef.GAME_EVENT_OVER, this)
                .addOnEventListener(GameDef.GAME_EVENT_SCORE_UPDATE, this)
                .addOnEventListener(GameDef.GAME_EVENT_KEY_START, this)
                .addOnEventListener(GameDef.GAME_EVENT_KEY_STOP, this);
        setContentView(tetrisLayout);
        game = new TetrisGame(context);
        game.setActiveBoxTask(new Runnable() {
            @Override
            public void run() {
                uiHandler.post(() -> tetrisLayout.moveActiveBox());
            }
        });

        tetrisLayout.setScore(game.getScore());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int[][] array = new int[][]{{1, 5, 0}, {3, 8, 0}, {5, 9, 2}};
        ArrayUtils.rotate90(array);
    }

    @Override
    public void onEvent(String key, Bundle bundle) {
        if (GameDef.GAME_EVENT_KEY_START.equals(key)) {
            game.start();
        } else if (GameDef.GAME_EVENT_OVER.equals(key)) {
            ToastUtils.showLong(context, "Game Over!!!");
            game.stop();
        } else if (GameDef.GAME_EVENT_KEY_STOP.equals(key)) {
            game.stop();
        } else if (GameDef.GAME_EVENT_SCORE_UPDATE.equals(key)) {
            int score = game.getScore();
            score++;
            game.setScore(score);
            tetrisLayout.setScore(score);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        game.stop();
    }
}
