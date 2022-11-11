package com.example.juiexample.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;

import androidx.annotation.Nullable;

import com.example.juiexample.utils.ViewUtils;

public class TitleSwitch extends TitleLayout {

    protected final Switch mSwitch;

    public TitleSwitch(Context context) {
        this(context, null);
    }

    public TitleSwitch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mSwitch = createSwitch();
        addView(mSwitch);
    }

    private Switch createSwitch() {
        return ViewUtils.createSwitch(context);
    }

    @Override
    public final TitleSwitch setTitle(String title) {
        return (TitleSwitch) super.setTitle(title);
    }
}
