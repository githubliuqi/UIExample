package com.example.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.juiexample.common.TitleButton;
import com.example.tetris.TetrisDialog;

public class TetrisTestView extends TitleButton implements View.OnClickListener {

    public TetrisTestView(Context context) {
        this(context, null);
    }

    public TetrisTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setButtonListener(this::onClick);
        setTitle("Tetris");
        setButtonText("ok");
    }

    @Override
    public void onClick(View v) {
        TetrisDialog dialog = new TetrisDialog(context);
        dialog.show();
    }
}
