package com.example.juiexample.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.annotation.Nullable;

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
        return ViewUtils.createButton(context);
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
