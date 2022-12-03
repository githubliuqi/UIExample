package com.example.tetris.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Size;

import com.example.common.TLog;
import com.example.juiexample.event.EventHandler;
import com.example.juiexample.event.OnEventListener;
import com.example.tetris.model.GameDef;

public class GameMainView extends BaseGridView implements OnEventListener {

    private static final String TAG = "GameMainView";
    private final ActiveBox activeBox;
    private boolean gameOver = false;

    public GameMainView(Context context, Size size) {
        super(context);
        setGridSize(size);
        setGridTypes(new int[20][14]);

        activeBox = new ActiveBox(context);
        activeBox.setGridSize(size);
        addView(activeBox);
        resetActiveBox();

        EventHandler.getInstance()
                .addOnEventListener(GameDef.GAME_EVENT_KEY_LEFT, this)
                .addOnEventListener(GameDef.GAME_EVENT_KEY_RIGHT, this)
                .addOnEventListener(GameDef.GAME_EVENT_KEY_DOWN, this);
    }

    public GameMainView moveActiveBox(GameDef.KeyType keyType) {
        if (gameOver) {
            return this;
        }
        Rect activeBoxRect = new Rect(activeBox.getRectInParent());
        if (keyType == GameDef.KeyType.TYPE_MOVE_DOWN) {
            activeBoxRect.top += 1;
            activeBoxRect.bottom += 1;
            if (checkRect(activeBoxRect, activeBox.getGridTypes())){
                activeBox.moveDown();
            } else {
                update();
                gameOver = checkGameOver();
                if (gameOver) {
                    EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_OVER, null);
                    return this;
                }
            }
        } else if (keyType == GameDef.KeyType.TYPE_MOVE_LEFT) {
            activeBoxRect.left -= 1;
            activeBoxRect.right -= 1;
            if (checkRect(activeBoxRect, activeBox.getGridTypes())){
                activeBox.moveLeft();
            }
        } else if (keyType == GameDef.KeyType.TYPE_MOVE_RIGHT) {
            activeBoxRect.left += 1;
            activeBoxRect.right += 1;
            if (checkRect(activeBoxRect, activeBox.getGridTypes())){
                activeBox.moveRight();
            }
        } else if (keyType == GameDef.KeyType.TYPE_SWITCH_STYLE) {
            switchStyle();
        }
        return this;
    }

    private void switchStyle() {
        Rect activeBoxRect = new Rect(activeBox.getRectInParent());
        int w = activeBox.getColumnCount();
        int h = activeBox.getRowCount();
        if (activeBoxRect.top < w - h) {
            TLog.w(TAG, "垂直空间不足，无法翻转");
            return;
        }
        int[][] gridTypes = GameDef.rotate90(activeBox.getGridTypes());
        activeBoxRect.top = activeBoxRect.bottom - w;
        activeBoxRect.right = activeBoxRect.left + h;
        if (checkRect(activeBoxRect, gridTypes)) {
            activeBox.setGridTypes(gridTypes);
            Point newPoint = new Point(activeBoxRect.left, activeBoxRect.bottom - w);
            activeBox.setPoint(newPoint);
        }
    }

    private void update() {
        TLog.d(TAG, "update");
        Rect activeBoxRect = activeBox.getRectInParent();
        for (int i = activeBoxRect.left; i < activeBoxRect.right; i++) {
            for (int j = activeBoxRect.top; j < activeBoxRect.bottom; j++) {
                int gridType = activeBox.getGridTypes()[j - activeBoxRect.top][i - activeBoxRect.left];
                int oldType = getGridTypes()[j][i];
                if (oldType == 0 && gridType != 0) {
                    setGridType(new Point(i, j), gridType);
                    getItemView(new Point(i, j)).copyFrom(activeBox.getItemView(new Point(i - activeBoxRect.left, j - activeBoxRect.top)));
                }
            }
        }
        for (int j = getRowCount() - 1; j >= 0; j--) {
            int clearLineIndex = j;
            for (int i = 0; i < getColumnCount(); i++) {
                if (getItemView(new Point(i, j)).getGridType() == 0) {
                    clearLineIndex = -1;
                    break;
                }
            }
            if (clearLineIndex == j) {
                clearLine(clearLineIndex);
                j++;
            }
        }
        resetActiveBox();
    }

    private void clearLine(int lineIndex) {
        TLog.d(TAG, "clearLine:"+lineIndex);
        EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_SCORE_UPDATE, null);
        for (int j = lineIndex; j >= 0; j--) {
            for (int i = 0; i < getColumnCount(); i++) {
                if (j == 0) {
                    setGridType(new Point(i, j), 0);
                } else {
                    setGridType(new Point(i, j), getGridTypes()[j-1][i]);
                    getItemView(new Point(i, j)).copyFrom(getItemView(new Point(i, j-1)));
                }
            }
        }
    }

    private boolean checkGameOver() {
        Rect activeBoxRect = activeBox.getRectInParent();
        for (int i = activeBoxRect.left; i < activeBoxRect.right; i++) {
            for (int j = activeBoxRect.top; j < activeBoxRect.bottom; j++) {
                int boxGridType = activeBox.getGridTypes()[j - activeBoxRect.top][i - activeBoxRect.left];
                int mainGridType = getGridTypes()[j][i];
                if (boxGridType != 0 && mainGridType != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkRect(Rect inRect, int[][] inMatrix) {
        Rect outRect = getRectInParent();
        int[][] outMatrix = getGridTypes();
        return checkRect(outRect, outMatrix, inRect, inMatrix);
    }

    private boolean checkRect(Rect outRect, int[][] outMatrix, Rect inRect, int[][] inMatrix) {
        // 边界检查
        if (inRect.left < outRect.left
                || inRect.top < outRect.top
                || inRect.right > outRect.right
                || inRect.bottom > outRect.bottom) {
            return false;
        }

        // 重叠检查
        for (int i = inRect.top; i < inRect.bottom; i++) {
            for (int j = inRect.left; j < inRect.right; j++) {
                int inGridType = inMatrix[i - inRect.top][j - inRect.left];
                int outGridType = outMatrix[i][j];
                if (inGridType != 0 && outGridType != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 重置样式与位置
     * @return
     */
    private void resetActiveBox() {
        activeBox.randSetGridTypes();
        activeBox.setPoint(new Point((getColumnCount() - activeBox.getColumnCount()) / 2, 0));
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
