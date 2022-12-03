package com.example.snaker.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Size;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.common.utils.AppUtils;
import com.example.juiexample.event.OnEventListener;
import com.example.juiexample.utils.ViewUtils;
import com.example.snaker.model.GameDef;

public class GameLayout extends LinearLayout {

    private final Context context;
    private OnEventListener onEventListener;
    private final GameMainView gameMainView;

    private TextView scoreTextView;

    public GameLayout(Context context) {
        super(context);
        this.context = context;
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFF13223F);

        scoreTextView = createScoreView();
        setScore(0);
        addView(scoreTextView);

        gameMainView = new GameMainView(context);

        LayoutParams mainParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(gameMainView, mainParams);

        KeyLayout keyLayout = new KeyLayout(context);
        LayoutParams keyParams = new LayoutParams(AppUtils.dip2px(250), LayoutParams.WRAP_CONTENT);
        keyParams.topMargin = AppUtils.dip2px(20);
        addView(keyLayout, keyParams);
    }

    public GameLayout setOnEventListener(OnEventListener listener) {
        onEventListener = listener;
        return this;
    }

    public GameLayout setScore(int score) {
        scoreTextView.setText(String.format("Score:"+score));
        return this;
    }

    public GameLayout moveSnaker() {
        gameMainView.moveSnaker();
        return this;
    }

    public GameLayout setMoveDirection(GameDef.MoveType moveType) {
        gameMainView.setMoveDirection(moveType);
        return this;
    }

    private TextView createScoreView() {
        TextView textView = ViewUtils.createTextView(context);
        textView.setTextSize(20);
        textView.setTextColor(Color.RED);
        return textView;
    }
}
