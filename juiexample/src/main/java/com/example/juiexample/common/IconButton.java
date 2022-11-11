package com.example.juiexample.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;

public class IconButton extends AppCompatImageButton {

    private boolean state;
    private int iconResId1;
    private int iconResId2;

    public IconButton(Context context) {
        this(context, null);
    }

    public IconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        iconResId1 = android.R.drawable.ic_input_add;
        iconResId2 = android.R.drawable.ic_input_delete;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                state = !state;
                IconButton.this.setState(state);
                IconButton.this.onEventCallback();
            }
        });
    }

    public IconButton setState(boolean state) {
        this.state = state;
        setImageResource(state ? iconResId2 : iconResId1);
        return this;
    }

    public IconButton setIcon1(int redId) {
        iconResId1 = redId;
        return this;
    }

    public IconButton setIcon2(int redId) {
        iconResId2 = redId;
        return this;
    }

    public void onEventCallback() {

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setState(state);
    }
}
