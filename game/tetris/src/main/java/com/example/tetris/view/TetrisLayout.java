package com.example.tetris.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Size;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.common.utils.AppUtils;
import com.example.juiexample.event.OnEventListener;
import com.example.juiexample.utils.ViewUtils;
import com.example.tetris.GameDef;
import com.example.tetris.GridTypeMatrix;
import com.example.tetris.GridType;

public class TetrisLayout extends LinearLayout {

    private final Context context;
    private OnEventListener onEventListener;
    private final TetrisMainView tetrisMainView;

    private TextView scoreTextView;

    public TetrisLayout(Context context) {
        super(context);
        this.context = context;
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFF13223F);

        scoreTextView = createScoreView();
        addView(scoreTextView);

        int size = AppUtils.dip2px(20);
        tetrisMainView = new TetrisMainView(context, new Size(size, size));
        LinearLayout.LayoutParams mainParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(tetrisMainView, mainParams);

        KeyLayout keyLayout = new KeyLayout(context);
        LinearLayout.LayoutParams keyParams = new LayoutParams(AppUtils.dip2px(250), LayoutParams.WRAP_CONTENT);
        keyParams.topMargin = AppUtils.dip2px(20);
        addView(keyLayout, keyParams);
    }

    public TetrisLayout setOnEventListener(OnEventListener listener) {
        onEventListener = listener;
        return this;
    }

    public TetrisLayout moveActiveBox() {
        tetrisMainView.moveActiveBox(GameDef.KeyType.TYPE_MOVE_DOWN);
        return this;
    }

    public TetrisLayout setScore(int score) {
        scoreTextView.setText(String.format("Score:"+score));
        return this;
    }

    public TetrisLayout switchActiveBoxStyle() {
        tetrisMainView.moveActiveBox(GameDef.KeyType.TYPE_SWITCH_STYLE);
        return this;
    }

    private TextView createScoreView() {
        TextView textView = ViewUtils.createTextView(context);
        textView.setTextSize(20);
        textView.setTextColor(Color.RED);
        return textView;
    }
}
