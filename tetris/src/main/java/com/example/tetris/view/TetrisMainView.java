package com.example.tetris.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Size;

import com.example.common.TLog;
import com.example.juiexample.event.EventHandler;
import com.example.juiexample.event.OnEventListener;
import com.example.tetris.GameDef;
import com.example.tetris.GameDef.GridType;

import java.util.Random;

public class TetrisMainView extends TetrisGrid implements OnEventListener {

    private static final String TAG = "TetrisMainView";
    private final ActiveBox activeBox;
    private final Rect rect = new Rect();
    private final Random random = new Random();
    private boolean gameOver = false;

    public TetrisMainView(Context context, Size size) {
        super(context, size);
        activeBox = new ActiveBox(context, size);
        EventHandler.getInstance()
                .addOnEventListener(GameDef.GAME_EVENT_KEY_LEFT, this)
                .addOnEventListener(GameDef.GAME_EVENT_KEY_RIGHT, this)
                .addOnEventListener(GameDef.GAME_EVENT_KEY_DOWN, this);
    }

    @Override
    public TetrisMainView setGridSize(Size size) {
        super.setGridSize(size);
        return this;
    }

    @Override
    public TetrisMainView setGridType(GridType[][] gridTypes) {
        super.setGridType(gridTypes);
        rect.left = 0;
        rect.top = 0;
        rect.right = gridCount.getWidth();
        rect.bottom = gridCount.getHeight();
        int rand = random.nextInt(GameDef.ACTIVEBOX_TYPE_LIST.size());
        updateActiveBoxGridType(GameDef.ACTIVEBOX_TYPE_LIST.get(rand));
        addView(activeBox);
        return this;
    }

    private TetrisMainView updateActiveBoxGridType(GridType[][] gridType) {
        activeBox.setGridType(gridType);
        Rect activeBoxRect = new Rect();
        activeBoxRect.left = (gridCount.getWidth() - activeBox.gridCount.getWidth()) / 2;
        activeBoxRect.right = activeBoxRect.left + activeBox.gridCount.getWidth();
        activeBoxRect.top = 0;
        activeBoxRect.bottom = activeBoxRect.top + activeBox.gridCount.getHeight();
        activeBox.setRect(activeBoxRect);
        return this;
    }

    public TetrisMainView moveActiveBox(GameDef.KeyType keyType) {
        if (gameOver) {
            return this;
        }
        Rect activeBoxRect = activeBox.getRect();
        TLog.d(TAG, rect.toString());
        TLog.d(TAG, activeBoxRect.toString());
        if (keyType == GameDef.KeyType.TYPE_MOVE_DOWN) {
            if (coincideBottom()) {
                update();
                gameOver = checkGameOver();
                if (gameOver) {
                    EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_OVER, null);
                    return this;
                }
            } else {
                activeBox.moveDown();
            }
        } else if (keyType == GameDef.KeyType.TYPE_MOVE_LEFT) {
            if (!coincideLeft()) {
                activeBox.moveLeft();
            }
        } else if (keyType == GameDef.KeyType.TYPE_MOVE_RIGHT) {
            if (!coincideRight()) {
                activeBox.moveRight();
            }
        }
        return this;
    }

    private boolean coincideLeft() {
        Rect activeBoxRect = activeBox.getRect();
        if (activeBoxRect.left == rect.left) {
            TLog.d(TAG, "coincideLeft");
            return true;
        }
        for (int i = activeBoxRect.left; i < activeBoxRect.right; i++) {
            for (int j = activeBoxRect.top; j < activeBoxRect.bottom; j++) {
                GridView boxGrid = activeBox.mainViews[i - activeBoxRect.left][j - activeBoxRect.top];
                GridView mainGrid = mainViews[i - 1][j];
                if (boxGrid.getGridType() != GridType.TYPE_DEFAULT
                        && mainGrid.getGridType() != GridType.TYPE_DEFAULT) {
                    TLog.d(TAG, "coincideLeft");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean coincideRight() {
        Rect activeBoxRect = activeBox.getRect();
        if (activeBoxRect.right - 1 == rect.right - 1) {
            TLog.d(TAG, "coincideRight");
            return true;
        }
        for (int i = activeBoxRect.left; i < activeBoxRect.right; i++) {
            for (int j = activeBoxRect.top; j < activeBoxRect.bottom; j++) {
                GridView boxGrid = activeBox.mainViews[i - activeBoxRect.left][j - activeBoxRect.top];
                GridView mainGrid = mainViews[i + 1][j];
                if (boxGrid.getGridType() != GridType.TYPE_DEFAULT
                        && mainGrid.getGridType() != GridType.TYPE_DEFAULT) {
                    TLog.d(TAG, "coincideRight");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean coincideBottom() {
        Rect activeBoxRect = activeBox.getRect();
        if (activeBoxRect.bottom - 1 == rect.bottom - 1) {
            TLog.d(TAG, "coincideBottom");
            return true;
        }
        for (int i = activeBoxRect.left; i < activeBoxRect.right; i++) {
            for (int j = activeBoxRect.top; j < activeBoxRect.bottom; j++) {
                GridView boxGrid = activeBox.mainViews[i - activeBoxRect.left][j - activeBoxRect.top];
                GridView mainGrid = mainViews[i][j+1];
                if (boxGrid.getGridType() != GridType.TYPE_DEFAULT
                        && mainGrid.getGridType() != GridType.TYPE_DEFAULT) {
                    TLog.d(TAG, "coincideBottom");
                    return true;
                }
            }
        }
        return false;
    }

    private void update() {
        TLog.d(TAG, "update");
        Rect activeBoxRect = activeBox.getRect();
        for (int i = activeBoxRect.left; i < activeBoxRect.right; i++) {
            for (int j = activeBoxRect.top; j < activeBoxRect.bottom; j++) {
                GridView view = activeBox.mainViews[i - activeBoxRect.left][j - activeBoxRect.top];
                GridType oldType = mainViews[i][j].getGridType();
                TLog.d(TAG, mainViews[i][j].toString());
                if (oldType == GridType.TYPE_DEFAULT && view.getGridType() != GridType.TYPE_DEFAULT) {
                    mainViews[i][j].setGridType(view.getGridType());
                }
            }
        }
        for (int j = gridCount.getHeight() - 1; j >= 0; j--) {
            int clearLineIndex = j;
            for (int i = 0; i < gridCount.getWidth() - 1; i++) {
                if (mainViews[i][j].getGridType() == GridType.TYPE_DEFAULT) {
                    clearLineIndex = -1;
                    break;
                }
            }
            clearLine(clearLineIndex);
        }
        int rand = random.nextInt(GameDef.ACTIVEBOX_TYPE_LIST.size());
        updateActiveBoxGridType(GameDef.ACTIVEBOX_TYPE_LIST.get(rand));
    }

    private void clearLine(int lineIndex) {
        if (lineIndex <= 0 || lineIndex >= gridCount.getHeight()) {
            return;
        }
        EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_SCORE_UPDATE, null);
        for (int j = lineIndex; j >= 0; j--) {
            for (int i = 0; i < gridCount.getWidth() - 1; i++) {
                if (j == 0) {
                    mainViews[i][j].setGridType(GridType.TYPE_DEFAULT);
                } else {
                    mainViews[i][j].setGridType(mainViews[i][j-1].getGridType());
                }
            }
        }
    }

    private boolean checkGameOver() {
        Rect activeBoxRect = activeBox.getRect();
        TLog.d(TAG, "checkGameOver, rect=" + rect);
        TLog.d(TAG, "checkGameOver, activeBoxRect=" + activeBoxRect);
        for (int i = activeBoxRect.left; i < activeBoxRect.right; i++) {
            for (int j = activeBoxRect.top; j < activeBoxRect.bottom; j++) {
                GridView boxGrid = activeBox.mainViews[i - activeBoxRect.left][j - activeBoxRect.top];
                GridView mainGrid = mainViews[i][j];
                if (boxGrid.getGridType() != GridType.TYPE_DEFAULT
                        && mainGrid.getGridType() != GridType.TYPE_DEFAULT) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onEvent(String key, Bundle bundle) {
        if (GameDef.GAME_EVENT_KEY_DOWN.equals(key)) {
            moveActiveBox(GameDef.KeyType.TYPE_MOVE_DOWN);
        } else if (GameDef.GAME_EVENT_KEY_LEFT.equals(key)) {
            moveActiveBox(GameDef.KeyType.TYPE_MOVE_LEFT);
        } else if (GameDef.GAME_EVENT_KEY_RIGHT.equals(key)) {
            moveActiveBox(GameDef.KeyType.TYPE_MOVE_RIGHT);
        }
    }
}
