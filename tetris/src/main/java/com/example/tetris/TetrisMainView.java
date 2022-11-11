package com.example.tetris;

import android.content.Context;
import android.graphics.Rect;
import android.util.Size;
import android.widget.RelativeLayout;

import com.example.juiexample.common.TLog;

public class TetrisMainView extends TetrisGrid {

    private static final String TAG = "TetrisMainView";
    private final ActiveBox activeBox;
    private final Rect rect = new Rect();
    private final Rect activeBoxRect = new Rect();

    public TetrisMainView(Context context, Size size, Size count) {
        super(context, size, count);
        activeBox = new ActiveBox(context, size, new Size(4, 4));
        activeBox.update();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = (count.getWidth() / 2) * size.getWidth();
        params.topMargin = 0;
        activeBox.setLayoutParams(params);
        rect.left = 0;
        rect.top = 0;
        rect.right = gridCount.getWidth();
        rect.bottom = gridCount.getHeight();

        activeBoxRect.left = count.getWidth() / 2;
        activeBoxRect.right = activeBoxRect.left + activeBox.gridCount.getWidth();
        activeBoxRect.top = 0;
        activeBoxRect.bottom = activeBox.gridCount.getHeight();
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
        RelativeLayout.LayoutParams params = (LayoutParams) activeBox.getLayoutParams();
        params.topMargin += gridSize.getHeight();
        activeBox.setLayoutParams(params);
        activeBoxRect.bottom += 1;
        activeBoxRect.top += 1;
        TLog.d(TAG, rect.toString());
        TLog.d(TAG, activeBoxRect.toString());
        boolean intersects = Rect.intersects(rect, activeBoxRect);
        if (intersects) {
            TLog.e(TAG, "intersects = " + intersects);
            return this;
        }
        return this;
    }


}
