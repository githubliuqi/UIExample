package com.example.tetris.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Size;
import android.widget.RelativeLayout;

import com.example.juiexample.common.grid.GridItemView;
import com.example.juiexample.common.grid.GridView;

public class BaseGridView extends GridView {

    private final Point point = new Point(0, 0);

    private final Rect rect = new Rect();

    public BaseGridView(Context context) {
        super(context);
    }

    public BaseGridView setPoint(Point point) {
        this.point.x = point.x;
        this.point.y = point.y;
        setLayoutParams();
        updateRect();
        return this;
    }

    @Override
    public BaseGridView setGridTypes(int[][] gridTypes) {
        super.setGridTypes(gridTypes);
        updateRect();
        return this;
    }

    public Point getPoint() {
        return new Point(point);
    }

    public Rect getRectInParent() {
        return new Rect(rect);
    }

    private void updateRect() {
        rect.left = point.x;
        rect.top = point.y;
        rect.right = rect.left + getColumnCount();
        rect.bottom = rect.top + getRowCount();
    }

    private void setLayoutParams() {
        Size size = getGridSize();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        if (params == null) {
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        params.leftMargin = point.x * size.getWidth();
        params.topMargin = point.y * size.getHeight();
//        TLog.d(TAG, String.format("leftMargin=%s, topMargin=%s", params.leftMargin, params.topMargin));
        setLayoutParams(params);
    }

    @Override
    protected GridItemView getGridItemView() {
        GameGridItemView itemView = new GameGridItemView(context);
        return itemView;
    }
}
