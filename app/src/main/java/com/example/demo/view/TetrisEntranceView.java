package com.example.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.juiexample.common.TitleButton;
import com.example.tetris.TetrisDialog;

public class TetrisEntranceView extends TitleButton implements View.OnClickListener {

    public TetrisEntranceView(Context context) {
        this(context, null);
    }

    public TetrisEntranceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setButtonListener(this::onClick);
        setTitle("Tetris");
    }

    @Override
    public void onClick(View v) {
        TetrisDialog dialog = new TetrisDialog(context);
        dialog.show();
    }
}
