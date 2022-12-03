package com.example.snaker;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.example.juiexample.common.dialog.CustomDialog;
import com.example.juiexample.event.EventHandler;
import com.example.juiexample.event.OnEventListener;
import com.example.juiexample.utils.ToastUtils;
import com.example.snaker.model.GameDef;
import com.example.snaker.model.SnakerGame;
import com.example.snaker.settings.SettingsDialog;
import com.example.snaker.view.GameLayout;

public class RetroSnakerDialog extends CustomDialog implements OnEventListener {

    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private final SettingsDialog settingsDialog;
    private final GameLayout gameLayout;
    private final SnakerGame game;

    public RetroSnakerDialog(@NonNull Context context) {
        super(context);
        setTitle("贪吃蛇");
        setRightTitle("设置");
        settingsDialog = new SettingsDialog(context);
        setRightListener(view -> settingsDialog.show());
        gameLayout = new GameLayout(context);
        gameLayout.setOnEventListener(this::onEvent);
        setView(gameLayout);

        game = new SnakerGame(context);
        game.setInterval(600);
        game.setActiveBoxTask(new Runnable() {
            @Override
            public void run() {
                uiHandler.post(() -> gameLayout.moveSnaker());
            }
        });

        EventHandler.getInstance()
                .addOnEventListener(GameDef.GAME_EVENT_OVER, this)
                .addOnEventListener(GameDef.GAME_EVENT_KEY_UP, this)
                .addOnEventListener(GameDef.GAME_EVENT_KEY_DOWN, this)
                .addOnEventListener(GameDef.GAME_EVENT_KEY_LEFT, this)
                .addOnEventListener(GameDef.GAME_EVENT_KEY_RIGHT, this)
                .addOnEventListener(GameDef.GAME_EVENT_SCORE_UPDATE, this)
                .addOnEventListener(GameDef.GAME_EVENT_SPEED_UPDATE, this)
                .addOnEventListener(GameDef.GAME_EVENT_KEY_START, this)
                .addOnEventListener(GameDef.GAME_EVENT_KEY_STOP, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        } else if (GameDef.GAME_EVENT_KEY_UP.equals(key)) {
            gameLayout.setMoveDirection(GameDef.MoveType.TYPE_MOVE_UP);
        } else if (GameDef.GAME_EVENT_KEY_DOWN.equals(key)) {
            gameLayout.setMoveDirection(GameDef.MoveType.TYPE_MOVE_DOWN);
        } else if (GameDef.GAME_EVENT_KEY_LEFT.equals(key)) {
            gameLayout.setMoveDirection(GameDef.MoveType.TYPE_MOVE_LEFT);
        } else if (GameDef.GAME_EVENT_KEY_RIGHT.equals(key)) {
            gameLayout.setMoveDirection(GameDef.MoveType.TYPE_MOVE_RIGHT);
        } else if (GameDef.GAME_EVENT_SCORE_UPDATE.equals(key)) {
            int score = game.getScore();
            score++;
            game.setScore(score);
            gameLayout.setScore(score);
        } else if (GameDef.GAME_EVENT_SPEED_UPDATE.equals(key)) {
            int interval = bundle.getInt("refresh_interval");
            game.setInterval(interval);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        game.stop();
        EventHandler.getInstance().removeAll();
    }
}
