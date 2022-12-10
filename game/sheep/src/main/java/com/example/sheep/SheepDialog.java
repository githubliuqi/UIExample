package com.example.sheep;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.example.juiexample.common.dialog.CustomAlertDialog;
import com.example.juiexample.common.dialog.CustomDialog;
import com.example.juiexample.event.EventHandler;
import com.example.juiexample.event.OnEventListener;
import com.example.juiexample.utils.ViewUtils;
import com.example.sheep.model.GameDef;
import com.example.sheep.settings.SettingsDialog;
import com.example.sheep.view.GameLayout;

public class SheepDialog extends CustomDialog implements OnEventListener {

    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private final SettingsDialog settingsDialog;
    private final GameLayout gameLayout;
    private int score = 0;

    public SheepDialog(@NonNull Context context) {
        super(context);
        setTitle("🐑");
        setRightTitle("设置");
        settingsDialog = new SettingsDialog(context);
        setRightListener(view -> settingsDialog.show());
        gameLayout = new GameLayout(context);
        gameLayout.setOnEventListener(this::onEvent);
        gameLayout.setVisibility(View.INVISIBLE);

        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.addView(gameLayout);
        Button startBtn = ViewUtils.createButton(context);
        startBtn.setText("开始游戏");
        startBtn.setOnClickListener(v -> {
            gameLayout.setVisibility(View.VISIBLE);
            startBtn.setVisibility(View.GONE);
            setRightVisibility(View.INVISIBLE);
        });
        FrameLayout.LayoutParams startParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        startParams.gravity = Gravity.CENTER;
        frameLayout.addView(startBtn, startParams);
        setView(frameLayout);

        EventHandler.getInstance()
                .addOnEventListener(GameDef.GAME_EVENT_OVER, this)
                .addOnEventListener(GameDef.GAME_EVENT_LEVEL_UPDATE, this)
                .addOnEventListener(GameDef.GAME_EVENT_SCORE_UPDATE, this);
    }

    @Override
    public void onEvent(String key, Bundle bundle) {
        if (GameDef.GAME_EVENT_OVER.equals(key)) {
            gameLayout.setGameOver(true);
            boolean pass = bundle.getBoolean("pass");
            CustomAlertDialog alertDialog = new CustomAlertDialog(context) {
                @Override
                public void onClickOk() {
                    SheepDialog.this.dismiss();
                }
            };
            alertDialog.setMessage(pass ? "恭喜过关！" : "很遗憾，游戏失败！");
            alertDialog.show();
        } else if (GameDef.GAME_EVENT_SCORE_UPDATE.equals(key)) {
            gameLayout.setScore(++score);
        } else if (GameDef.GAME_EVENT_LEVEL_UPDATE.equals(key)) {
            int level = bundle.getInt("level");
            gameLayout.setLevel(level);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventHandler.getInstance().removeAll();
    }
}
