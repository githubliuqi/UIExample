package com.example.juiexample.common.grid;

import android.content.Context;
import android.graphics.Point;
import android.util.Size;
import android.widget.RelativeLayout;

public class GridItemView extends RelativeLayout {

    private static final String TAG = "GridItemView";

    protected final Point point = new Point();
    private Size size = new Size(0, 0);
    private int gridType = -1;
    private OnUpdateListener onUpdateListener;

    public GridItemView(Context context) {
        super(context);
    }

    public GridItemView setPoint(Point point) {
        this.point.x = point.x;
        this.point.y = point.y;
        setLayoutParams();
        return this;
    }

    public Point getPoint() {
        return new Point(point);
    }

    public GridItemView setSize(Size size) {
        this.size = new Size(size.getWidth(), size.getHeight());
        return this;
    }

    public Size getSize() {
        return size;
    }

    public int getGridType() {
        return gridType;
    }

    public void setGridType(int gridType) {
        boolean updateType = this.gridType != gridType;
        this.gridType = gridType;
        if (updateType && onUpdateListener != null) {
            onUpdateListener.onUpdate(this);
        }
    }

    public void copyFrom(GridItemView srcView) {

    }

    private void setLayoutParams() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        if (params == null) {
            params = new RelativeLayout.LayoutParams(size.getWidth(), size.getHeight());
        }
        params.leftMargin = point.x * size.getWidth();
        params.topMargin = point.y * size.getHeight();
//        TLog.d(TAG, String.format("leftMargin=%s, topMargin=%s", params.leftMargin, params.topMargin));
        setLayoutParams(params);
    }

    public void setOnUpdateListener(OnUpdateListener listener) {
        onUpdateListener = listener;
    }

    public interface OnUpdateListener {
        void onUpdate(GridItemView view);
    }
}
