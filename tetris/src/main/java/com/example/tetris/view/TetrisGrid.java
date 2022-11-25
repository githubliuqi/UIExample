package com.example.tetris.view;

import android.content.Context;
import android.util.Size;
import android.widget.RelativeLayout;

import com.example.tetris.GridTypeMatrix;
import com.example.tetris.GridType;

public class TetrisGrid extends RelativeLayout {

    protected Context context;

    // width: column, height: row
    protected Size gridCount = new Size(20, 20);
    protected Size gridSize = new Size(60, 60);
    protected GridView[][] mainViews;
    protected GridTypeMatrix gridTypes;

    public TetrisGrid(Context context, Size size) {
        super(context);
        this.context = context;
        setGridSize(size);
    }

    public TetrisGrid setGridSize(Size size) {
        gridSize = size;
        return this;
    }

    public TetrisGrid setGridType(GridTypeMatrix gridTypes) {
        this.gridTypes = gridTypes;
        this.gridCount = new Size(gridTypes.getColumn(), gridTypes.getRow());
        removeAllViews();
        mainViews = new GridView[gridCount.getHeight()][gridCount.getWidth()];
        for (int i = 0; i < mainViews.length; i++) {
            for (int j = 0; j < mainViews[0].length; j++) {
                GridView view = onCreateGridView(gridTypes.getGridType(i, j));
                view.setSize(gridSize);
                mainViews[i][j] = view;
                addView(view);
                RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.width = gridSize.getWidth();
                params.height = gridSize.getHeight();
                params.leftMargin = j * gridSize.getWidth();
                params.topMargin = i * gridSize.getHeight();
                view.setLayoutParams(params);
            }
        }
        return this;
    }

    protected GridView onCreateGridView(GridType type) {
        return new GridView(context, type);
    }
}
