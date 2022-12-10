package com.example.sheep.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.common.TLog;
import com.example.juiexample.common.grid.GridItemView;
import com.example.juiexample.event.EventHandler;
import com.example.juiexample.utils.ToastUtils;
import com.example.sheep.model.GameDef;

public class GameMainView extends RelativeLayout
        implements GridItemView.OnUpdateListener, View.OnClickListener {

    private static final String TAG = "GameMainView";

    private boolean gameOver = false;
    private final Size gridSize;
    private final Size size;

    public GameMainView(Context context, Size gridSize, Size size) {
        super(context);
        this.gridSize = gridSize;
        this.size = size;
        setLevel(5);
    }

    @Override
    public void onUpdate(GridItemView view) {
        GameGridItemView itemView = (GameGridItemView) view;
        itemView.setStrokeWidth(5);
        itemView.setCornerRadius(5);
        itemView.setStrokeColor(Color.BLACK);
        itemView.setColor(Color.WHITE);
        itemView.setImageResId(GameDef.IMAGES[view.getGridType()]);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (gameOver) {
            return;
        }
        GameGridItemView currentView = (GameGridItemView) view;
        if (currentView.isShadow()) {
            TLog.w(TAG, "isCovered");
            return;
        }
        currentView.setOnClickListener(null);
        removeView(view);
        updateShadow();
        onRemoveView(currentView);
        if (getChildCount() == 0) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("pass", true);
            EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_OVER, bundle);
        }
    }

    public void onRemoveView(GameGridItemView view) {

    }

    private void updateShadow() {
        for(int i = 0; i < getChildCount(); i++) {
            GameGridItemView itemView = (GameGridItemView) getChildAt(i);
            itemView.showShadow(isCovered(itemView));
        }
    }

    private boolean isCovered( GameGridItemView currentView) {
        boolean isCovered = false;
        Rect currentRect = currentView.getRect();
        for (int i = 0; i < getChildCount(); i++) {
            GameGridItemView otherView = (GameGridItemView) getChildAt(i);
            if (otherView.getLayerIndex() <= currentView.getLayerIndex()) {
                continue;
            }
            Rect otherRect = otherView.getRect();
            if (Rect.intersects(currentRect, otherRect)) {
                isCovered = true;
                break;
            }
        }
        return isCovered;
    }

    public Point getRandPoint() {
        int x = GameDef.RANDOM.nextInt(size.getWidth()/ 50) * 50;
        int y = GameDef.RANDOM.nextInt(size.getHeight() / 50) * 50;
        return new Point(Math.min(x, size.getWidth() - gridSize.getWidth()), Math.min(y, size.getHeight() - gridSize.getHeight()));
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setLevel(int level) {
        removeAllViews();
        int typeCount  = 4 + level;
        int count = typeCount * 3 * (level + 1);
        for(int i = 0; i < count; i++) {
            GameGridItemView itemView = new GameGridItemView(getContext());
            itemView.setLayerIndex(i);
            itemView.setSize(gridSize);
            itemView.setPoint(getRandPoint());
            itemView.setOnUpdateListener(this);
            itemView.setGridType(i % typeCount);
            addView(itemView);
        }
        updateShadow();
    }
}
