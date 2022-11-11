package com.example.tetris;

import android.content.Context;
import android.graphics.Color;
import android.util.Size;
import android.widget.RelativeLayout;

import com.example.juiexample.common.TLog;

public class ActiveBox extends TetrisGrid {

    public ActiveBox(Context context, Size size, Size count) {
        super(context, size, count);
        int padding = 2;
        setPadding(padding, padding, padding, padding);
        setBackgroundColor(Color.YELLOW);
        setGridColor(Color.YELLOW);
    }

    @Override
    public ActiveBox update() {
        super.update();
        return this;
    }
}
