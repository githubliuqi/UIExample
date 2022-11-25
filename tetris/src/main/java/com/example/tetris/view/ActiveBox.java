package com.example.tetris.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Size;
import android.widget.RelativeLayout;

import com.example.common.TLog;
import com.example.tetris.GridType;
import com.example.tetris.GridTypeMatrix;

public class ActiveBox extends TetrisGrid {

    private static final String TAG = "ActiveBox";
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
    protected GridView onCreateGridView(GridType type) {
        GridView view = super.onCreateGridView(type);
        if (type.isNone) {
            view.setVisibility(INVISIBLE);
        }
        return view;
    }

    /**
     * 顺时针旋转
     * @return
     */
    public ActiveBox switchStyle() {
        int w = gridCount.getWidth();
        int h = gridCount.getHeight();
        if (rect.top < w - h) {
            TLog.w(TAG, "垂直空间不足，无法翻转");
            return this;
        }
        GridTypeMatrix gridType = gridTypes.rotate90();
        setGridType(gridType);
        rect.top = rect.bottom - w;
        rect.right = rect.left + h;
        setRect(rect);
        return this;
    }
}
