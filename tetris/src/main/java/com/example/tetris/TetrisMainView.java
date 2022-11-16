package com.example.tetris;

import android.content.Context;
import android.graphics.Rect;
import android.util.Size;

import com.example.common.TLog;

public class TetrisMainView extends TetrisGrid {

    private static final String TAG = "TetrisMainView";
    private final ActiveBox activeBox;
    private final Rect rect = new Rect();

    public TetrisMainView(Context context, Size size, Size count) {
        super(context, size, count);
        activeBox = new ActiveBox(context, size, new Size(4, 4));
        activeBox.update();
        rect.left = 0;
        rect.top = 0;
        rect.right = gridCount.getWidth();
        rect.bottom = gridCount.getHeight();

        Rect activeBoxRect = new Rect();
        activeBoxRect.left = count.getWidth() / 2;
        activeBoxRect.right = activeBoxRect.left + activeBox.gridCount.getWidth();
        activeBoxRect.top = 0;
        activeBoxRect.bottom = activeBox.gridCount.getHeight();
        activeBox.setRect(activeBoxRect);
    }

    @Override
    public TetrisMainView setGridSize(Size size) {
        super.setGridSize(size);
        return this;
    }

    public TetrisMainView setGridLine(int borderWidth, int borderColor) {
        super.setGridLine(borderWidth, borderColor);
        return this;
    }

    @Override
    public TetrisMainView update() {
        super.update();
        addView(activeBox);
        return this;
    }

    public TetrisMainView moveActiveBox() {
        Rect activeBoxRect = activeBox.getRect();
        if (RectUtils.coincideBottom(rect, activeBoxRect)) {
            activeBoxRect.top = 0;
            activeBoxRect.bottom = activeBox.gridCount.getHeight();
        } else {
            activeBoxRect.bottom += 1;
            activeBoxRect.top += 1;
        }
        activeBox.setRect(activeBoxRect);
        TLog.d(TAG, rect.toString());
        TLog.d(TAG, activeBoxRect.toString());
        boolean coincideBootom = RectUtils.coincideBottom(rect, activeBoxRect);
        TLog.e(TAG, "coincideBootom = " + coincideBootom);
        if (coincideBootom) {

            return this;
        }
        return this;
    }

}
