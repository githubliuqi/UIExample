package com.example.tetris.view;

import android.content.Context;
import android.graphics.Point;
import android.view.View;

import com.example.common.TLog;
import com.example.juiexample.common.grid.GridItemView;
import com.example.tetris.model.GameDef;

import java.util.ArrayList;
import java.util.List;

public class ActiveBox extends BaseGridView {

    private static final String TAG = "ActiveBox";
    private final List<int[][]> gridTypesList = new ArrayList<>();
    private int color;

    public ActiveBox(Context context) {
        super(context);
        initGridTypesList();
        randSetGridTypes();
    }

    private void initGridTypesList() {
        gridTypesList.add(new int[][]{{1, 1, 1, 1}});
        gridTypesList.add(new int[][]{{1}, {1}, {1}, {1}});
        gridTypesList.add(new int[][]{{1, 1}, {1, 1}});
        gridTypesList.add(new int[][]{{1, 0}, {1, 0}, {1, 1}});
        gridTypesList.add(new int[][]{{0, 1}, {0, 1}, {1, 1}});
        gridTypesList.add(new int[][]{{0, 1, 0}, {1, 1, 1}});
        gridTypesList.add(new int[][]{{0, 1}, {1, 1}, {1, 0}});
        gridTypesList.add(new int[][]{{1, 0}, {1, 1}, {0, 1}});
        gridTypesList.add(new int[][]{{0, 1, 0}, {1, 0, 1}});
        gridTypesList.add(new int[][]{{0, 1, 0}, {1, 1, 1}});

//        gridTypesList.add(new int[][]{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}});
//        gridTypesList.add(new int[][]{{1, 0, 1}, {0, 1, 0}, {1, 0, 1}});
//        gridTypesList.add(new int[][]{{1, 1, 1}, {0, 1, 0}, {1, 1, 1}});
    }

    public ActiveBox moveLeft() {
        Point p = getPoint();
        setPoint(new Point(p.x - 1, p.y));
        return this;
    }

    public ActiveBox moveRight() {
        Point p = getPoint();
        setPoint(new Point(p.x + 1, p.y));
        return this;
    }

    public ActiveBox moveDown() {
        Point p = getPoint();
        setPoint(new Point(p.x, p.y + 1));
        return this;
    }

    @Override
    public void onUpdate(GridItemView view) {
        int gridType = view.getGridType();
        GameGridItemView itemView = (GameGridItemView) view;
        if (gridType == 0) {
            itemView.setColor(0x2F666666);
            view.setVisibility(View.GONE);
        } else if (gridType == 1) {
            itemView.setColor(color);
        } else {
            TLog.e(TAG, "error gridType: " + gridType);
            return;
        }
    }

    public ActiveBox randSetGridTypes() {
        randSetColor();
        int[][] gridTypeMatrix = gridTypesList.get(GameDef.RANDOM.nextInt(gridTypesList.size()));
        setGridTypes(gridTypeMatrix);
        return this;
    }

    private void randSetColor() {
        color = GameDef.COLORS[GameDef.RANDOM.nextInt(GameDef.COLORS.length)];
    }
}
