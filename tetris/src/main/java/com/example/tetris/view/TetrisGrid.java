package com.example.tetris.view;

import android.content.Context;
import android.util.Size;
import android.widget.RelativeLayout;

import com.example.tetris.GameDef.GridType;

public class TetrisGrid extends RelativeLayout {

    protected Context context;

    protected Size gridCount = new Size(20, 20);
    protected Size gridSize = new Size(60, 60);
    protected GridView[][] mainViews;
    public GridType[][] gridTypes;

    public TetrisGrid(Context context, Size size) {
        super(context);
        this.context = context;
        setGridSize(size);
    }

    public TetrisGrid setGridSize(Size size) {
        gridSize = size;
        return this;
    }

    public TetrisGrid setGridType(GridType[][] gridTypes) {
        this.gridTypes = gridTypes;
        this.gridCount = new Size(gridTypes.length, gridTypes[0].length);
        removeAllViews();
        mainViews = new GridView[gridCount.getWidth()][gridCount.getHeight()];
        for (int i = 0; i < mainViews.length; i++) {
            for (int j = 0; j < mainViews[0].length; j++) {
                GridView view = onCreateGridView(gridTypes[i][j]);
                view.setSize(gridSize);
                mainViews[i][j] = view;
                addView(view);
                RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.width = gridSize.getWidth();
                params.height = gridSize.getHeight();
                params.leftMargin = i * gridSize.getWidth();
                params.topMargin = j * gridSize.getHeight();
                view.setLayoutParams(params);
            }
        }
        return this;
    }

    protected GridView onCreateGridView(GridType type) {
        return new GridView(context, type);
    }
}
