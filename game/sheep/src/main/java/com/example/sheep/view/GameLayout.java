package com.example.sheep.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Size;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.common.utils.AppUtils;
import com.example.juiexample.event.OnEventListener;
import com.example.juiexample.utils.ViewUtils;
import com.example.sheep.model.GameDef;

public class GameLayout extends LinearLayout {

    private final Context context;
    private OnEventListener onEventListener;
    private final GameMainView gameMainView;
    private final BucketView bucketView;

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

        int size = AppUtils.dip2px(45);
        Size gridSize = new Size(size, size);
        Size mainSize = new Size(size * 7, size * 7);

        gameMainView = new GameMainView(context, gridSize, mainSize) {
            @Override
            public void onRemoveView(GameGridItemView view) {
                bucketView.addItemView(view);
            }
        };

        LayoutParams mainParams = new LayoutParams(mainSize.getWidth(), mainSize.getHeight());
        addView(gameMainView, mainParams);

        bucketView = new BucketView(context, gridSize);
        addView(bucketView);
    }

    public GameLayout setOnEventListener(OnEventListener listener) {
        onEventListener = listener;
        return this;
    }

    public GameLayout setScore(int score) {
        scoreTextView.setText(String.format("Score:"+score));
        return this;
    }

    public GameLayout setGameOver(boolean gameOver) {
        gameMainView.setGameOver(gameOver);
        return this;
    }

    public void setLevel(int level) {
        gameMainView.setLevel(level);
    }

    private TextView createScoreView() {
        TextView textView = ViewUtils.createTextView(context);
        textView.setTextSize(20);
        textView.setTextColor(Color.RED);
        return textView;
    }
}
