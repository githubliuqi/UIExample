package com.example.tetris.view;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.juiexample.event.EventHandler;
import com.example.juiexample.utils.ViewUtils;
import com.example.tetris.GameDef;

public class KeyLayout extends LinearLayout {

    public KeyLayout(Context context) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        Button btnLeft = createButton();
        btnLeft.setText("⬅️");
        btnLeft.setOnClickListener(v -> EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_KEY_LEFT, null));
        addView(btnLeft, params);

        Button btnUp = createButton();
        btnUp.setText("⬆️");
        btnUp.setOnClickListener(v -> EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_KEY_UP, null));
        addView(btnUp, params);

        Button btnStart = createButton();
        btnStart.setText("▶️");
        btnStart.setTag(Boolean.FALSE);
        btnStart.setOnClickListener(v -> {
            Boolean state = (Boolean) btnStart.getTag();
            String eventKey = state ? GameDef.GAME_EVENT_KEY_STOP : GameDef.GAME_EVENT_KEY_START;
            EventHandler.getInstance().sendEvent(eventKey, null);
            state = !state;
            btnStart.setText(state ? "⏸️️" : "▶️");
            btnStart.setTag(state);

        });
        addView(btnStart, params);

        Button btnDown = createButton();
        btnDown.setText("⬇️");
        btnDown.setOnClickListener(v -> EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_KEY_DOWN, null));
        addView(btnDown, params);

        Button btnRight = createButton();
        btnRight.setText("➡️");
        btnRight.setOnClickListener(v -> EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_KEY_RIGHT, null));
        addView(btnRight, params);
    }

    private Button createButton() {
        Button button = ViewUtils.createButton(getContext());
        button.setBackground(null);
        button.setTextSize(26);
        return button;
    }
}
