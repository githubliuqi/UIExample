package com.example.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.juiexample.common.TitleButton;
import com.example.snaker.RetroSnakerDialog;

public class RetroSnakerEntranceView extends TitleButton implements View.OnClickListener {

    public RetroSnakerEntranceView(Context context) {
        this(context, null);
    }

    public RetroSnakerEntranceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setButtonListener(this::onClick);
        setTitle("RetroSnaker");
    }

    @Override
    public void onClick(View v) {
        RetroSnakerDialog dialog = new RetroSnakerDialog(context);
        dialog.show();
    }
}
