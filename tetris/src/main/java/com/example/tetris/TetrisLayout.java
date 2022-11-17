package com.example.tetris;

import android.content.Context;
import android.util.Size;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.juiexample.common.IconButton;
import com.example.juiexample.event.OnEventListener;

public class TetrisLayout extends LinearLayout {

    private Context context;
    private OnEventListener onEventListener;
    private TetrisMainView tetrisMainView;

    public TetrisLayout(Context context) {
        super(context);
        this.context = context;
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.VERTICAL);
        addView(createStartButton());
        tetrisMainView = new TetrisMainView(context, new Size(40, 40), new Size(14,30));
        tetrisMainView.update();
        LinearLayout.LayoutParams mainParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(tetrisMainView, mainParams);
    }

    private ImageButton createStartButton() {
        IconButton iconButton = new IconButton(context) {
            @Override
            public void onEventCallback() {
                if (onEventListener != null) {
                    onEventListener.onEvent(GameDef.GAME_EVENT_START, null);
                }
            }
        };
        return iconButton;
    }

    public TetrisLayout setOnEventListener(OnEventListener listener) {
        onEventListener = listener;
        return this;
    }

    public TetrisLayout moveActiveBox() {
        tetrisMainView.moveActiveBox();
        return this;
    }
}
