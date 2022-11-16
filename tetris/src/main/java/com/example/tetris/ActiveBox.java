package com.example.tetris;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Size;
import android.widget.RelativeLayout;

public class ActiveBox extends TetrisGrid {

    private Rect rect = new Rect();
    private final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);

    public ActiveBox(Context context, Size size, Size count) {
        super(context, size, count);
        int padding = 2;
        setPadding(padding, padding, padding, padding);
        setBackgroundColor(Color.YELLOW);
        setGridColor(Color.YELLOW);
        setLayoutParams(params);
    }

    @Override
    public ActiveBox update() {
        super.update();
        return this;
    }

    public ActiveBox setRect(Rect rect) {
        this.rect.set(rect);
        params.leftMargin = rect.left * gridSize.getWidth();
        params.topMargin = rect.top * gridSize.getHeight();
        setLayoutParams(params);
        return this;
    }

    public Rect getRect() {
        return rect;
    }
}
