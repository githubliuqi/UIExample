package com.example.tetris;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.juiexample.event.OnEventListener;

public class TetrisDialog extends Dialog implements OnEventListener {

    private final TetrisGame game;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    public TetrisDialog(Context context) {
        super(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        TetrisLayout tetrisLayout = new TetrisLayout(context);
        tetrisLayout.setOnEventListener(this::onEvent);
        setContentView(tetrisLayout);
        game = new TetrisGame(context);
        game.setActiveBoxTask(new Runnable() {
            @Override
            public void run() {
                uiHandler.post(() -> tetrisLayout.moveActiveBox());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onEvent(String key, Bundle bundle) {
        if (GameDef.GAME_EVENT_START.equals(key)) {
            game.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        game.stop();
    }
}
