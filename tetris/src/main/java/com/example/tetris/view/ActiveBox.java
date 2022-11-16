package com.example.tetris.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Size;
import android.widget.RelativeLayout;

import com.example.tetris.GameDef;

public class ActiveBox extends TetrisGrid {

    private final Rect rect = new Rect();
    private final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);

    public ActiveBox(Context context, Size size) {
        super(context, size);
        setBackgroundColor(Color.TRANSPARENT);
        setLayoutParams(params);
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

    public ActiveBox moveLeft() {
        rect.left -= 1;
        rect.right -= 1;
        setRect(rect);
        return this;
    }

    public ActiveBox moveRight() {
        rect.left += 1;
        rect.right += 1;
        setRect(rect);
        return this;
    }

    public ActiveBox moveDown() {
        rect.bottom += 1;
        rect.top += 1;
        setRect(rect);
        return this;
    }

    @Override
    protected GridView onCreateGridView(GameDef.GridType type) {
        GridView view = super.onCreateGridView(type);
        if (type == GameDef.GridType.TYPE_DEFAULT) {
            view.setVisibility(INVISIBLE);
        }
        return view;
    }
}
