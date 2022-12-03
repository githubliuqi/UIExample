package com.example.juiexample.common.grid;

import android.content.Context;
import android.graphics.Point;
import android.util.Size;
import android.widget.RelativeLayout;

import com.example.common.TLog;

public class GridView extends RelativeLayout implements GridItemView.OnUpdateListener {

    private static final String TAG = "GridView";

    private int rowCount;
    private int columnCount;
    private Size gridSize = new Size(0, 0);
    private GridItemView[][] itemViews;
    private int[][] gridTypes;
    protected Context context;

    public GridView(Context context) {
        super(context);
        this.context = context;
    }

    public GridView setGridTypes(int[][] gridTypes) {
        this.gridTypes = gridTypes;
        rowCount = gridTypes.length;
        columnCount = gridTypes[0].length;
        removeAllViews();
        addViews();
        return this;
    }

    public int[][] getGridTypes() {
        return gridTypes;
    }

    public GridView setGridType(Point point, int type) {
        GridItemView itemView = getItemView(point);
        if (itemView == null) {
            return this;
        }
        itemView.setGridType(type);
        gridTypes[point.y][point.x] = type;
        return this;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public GridView setGridSize(Size size) {
        this.gridSize = new Size(size.getWidth(), size.getHeight());
        if (itemViews != null) {
            for (int i = 0; i < itemViews.length; i++) {
                for (int j = 0; j < itemViews[0].length; j++) {
                    GridItemView view = itemViews[i][j];
                    view.setSize(gridSize);
                    view.setPoint(new Point(j, i));
                }
            }
        }
        return this;
    }

    public Size getGridSize() {
        return gridSize;
    }

    public GridItemView getItemView(Point point) {
        if (itemViews == null) {
            TLog.e(TAG, "please call apply first");
            return null;
        }
        if (itemViews.length <= point.y || itemViews[0].length <= point.x) {
            TLog.e(TAG, String.format("getGridView, point(%s, %s) is invalid!!!", point.x, point.y));
            return null;
        }
        return itemViews[point.y][point.x];
    }

    private void addViews() {
//        TLog.d(TAG, String.format("addViews, row=%s, column=%s", getRowCount(), getColumnCount()));
        itemViews = new GridItemView[getRowCount()][getColumnCount()];
        for (int i = 0; i < itemViews.length; i++) {
            for (int j = 0; j < itemViews[0].length; j++) {
                GridItemView view = getGridItemView();
                if (view == null) {
                    TLog.e(TAG, "getGridItemView return null!!!");
                    return;
                }
                itemViews[i][j] = view;
                view.setOnUpdateListener(this);
                view.setSize(gridSize);
                view.setGridType(gridTypes[i][j]);
                view.setPoint(new Point(j, i));
                addView(view);
            }
        }
    }

    protected GridItemView getGridItemView() {
        GridItemView itemView = new GridItemView(context);
        return itemView;
    }

    @Override
    public void onUpdate(GridItemView view) {

    }
}
