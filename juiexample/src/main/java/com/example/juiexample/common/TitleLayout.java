package com.example.juiexample.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.juiexample.event.EventHandler;
import com.example.juiexample.utils.ViewUtils;

public abstract class TitleLayout extends LinearLayout {

    protected final TextView textView;
    protected final Context context;
    protected final EventHandler eventHandler = EventHandler.getInstance();

    public TitleLayout(Context context) {
        this(context, null);
    }

    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = getContext();
        this.textView = createTitleView();
        addView(textView);
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding(10,5, 10, 5);
        // space : space居中，实现左元素居左，右元素居右
        View space = new View(context);
        addView(space);
        LayoutParams params = (LayoutParams) space.getLayoutParams();
        params.height = 1;
        params.weight = 1;
    }

    private TextView createTitleView() {
        TextView textView = ViewUtils.createTextView(context);
        textView.setGravity(Gravity.LEFT);
        return textView;
    }

    public TitleLayout setTitle(String title) {
        textView.setText(title);
        return this;
    }
}
