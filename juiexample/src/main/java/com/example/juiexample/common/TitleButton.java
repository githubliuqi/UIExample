package com.example.juiexample.common;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.common.utils.AppUtils;
import com.example.juiexample.utils.ViewUtils;

public class TitleButton extends TitleLayout {

    protected final Button button;

    public TitleButton(Context context) {
        this(context, null);
    }

    public TitleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        button = createButton();
        addView(button);
    }

    private Button createButton() {
        Button view = ViewUtils.createButton(context);
        view.setTextColor(Color.WHITE);
        view.setPadding(0, 0, 0, 0);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(0xFF0062E3);
        gradientDrawable.setCornerRadius(10);
//        gradientDrawable.setStroke(1, 0xFF0062E3);
        view.setBackground(gradientDrawable);
        int w = AppUtils.dip2px(40);
        int h = AppUtils.dip2px(30);
        view.setMinHeight(h);
        view.setMinWidth(w);
        view.setMinimumHeight(h);
        view.setMinimumWidth(w);
        return view;
    }

    @Override
    public final TitleButton setTitle(String title) {
        return (TitleButton) super.setTitle(title);
    }

    public TitleButton setButtonText(String text) {
        button.setText(text);
        return this;
    }

    public TitleButton setButtonListener(OnClickListener listener) {
        button.setOnClickListener(listener);
        return this;
    }
}
