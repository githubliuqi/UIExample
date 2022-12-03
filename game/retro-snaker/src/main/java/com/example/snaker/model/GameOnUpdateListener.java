package com.example.snaker.model;

import android.graphics.Color;

import com.example.common.TLog;
import com.example.juiexample.common.grid.GridItemView;
import com.example.snaker.view.GameGridItemView;

public class GameOnUpdateListener implements GridItemView.OnUpdateListener {

    private static final String TAG = "GameOnUpdateListener";

    @Override
    public void onUpdate(GridItemView view) {
        int gridType = view.getGridType();
        GameGridItemView itemView = (GameGridItemView) view;
        if (gridType == 0) {
            itemView.setColor(0x2F666666);
        } else if (gridType == 1) {
            itemView.setColor(Color.RED);
        } else if (gridType == 2) {
            itemView.setCornerRadius(view.getSize().getWidth()/2);
            itemView.setColor(GameDef.COLORS[GameDef.RANDOM.nextInt(GameDef.COLORS.length)]);
        } else if (gridType == 10) {
            itemView.setColor(Color.RED);
        } else if (gridType == 11) {
            itemView.setColor(Color.RED);
        } else {
            TLog.e(TAG, "error gridType: " + gridType);
            return;
        }
    }
}
