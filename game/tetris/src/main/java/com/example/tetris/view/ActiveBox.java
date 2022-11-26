package com.example.tetris.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Size;
import android.widget.RelativeLayout;

import com.example.tetris.GridType;
import com.example.tetris.GridTypeMatrix;
import com.example.tetris.GridTypeMatrixManager;

public class ActiveBox extends TetrisGrid {

    private static final String TAG = "ActiveBox";
    private final Rect rect = new Rect();
    private final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    private final GridTypeMatrixManager matrixManager = new GridTypeMatrixManager();

    public ActiveBox(Context context, Size size) {
        super(context, size);
        setBackgroundColor(Color.TRANSPARENT);
        setLayoutParams(params);
        initMatrix(matrixManager);
    }

    private void initMatrix(GridTypeMatrixManager matrixManager) {
        matrixManager.addMatrix(new int[][]{{1, 1, 1, 1}});
        matrixManager.addMatrix(new int[][]{{1}, {1}, {1}, {1}});
        matrixManager.addMatrix(new int[][]{{1, 1}, {1, 1}});
        matrixManager.addMatrix(new int[][]{{1, 0}, {1, 0}, {1, 1}});
        matrixManager.addMatrix(new int[][]{{0, 1}, {0, 1}, {1, 1}});
        matrixManager.addMatrix(new int[][]{{0, 1, 0}, {1, 1, 1}});
//        matrixManager.addMatrix(new int[][]{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}});
//        matrixManager.addMatrix(new int[][]{{1, 0, 1}, {0, 1, 0}, {1, 0, 1}});
//        matrixManager.addMatrix(new int[][]{{1, 1, 1}, {0, 1, 0}, {1, 1, 1}});
    }

    public ActiveBox setRect(Rect rect) {
        this.rect.set(rect);
        params.leftMargin = rect.left * gridSize.getWidth();
        params.topMargin = rect.top * gridSize.getHeight();
        setLayoutParams(params);
        return this;
    }

    public Rect getRect() {
        return rect;
    }

    public ActiveBox moveLeft() {
        rect.left -= 1;
        rect.right -= 1;
        setRect(rect);
        return this;
    }

    public ActiveBox moveRight() {
        rect.left += 1;
        rect.right += 1;
        setRect(rect);
        return this;
    }

    public ActiveBox moveDown() {
        rect.bottom += 1;
        rect.top += 1;
        setRect(rect);
        return this;
    }

    @Override
    protected GridView getGridView(GridType type) {
        GridView view = super.getGridView(type);
        view.setStrokeWidth(2);
        view.setCornerRadius(2);
        view.setStrokeColor(Color.TRANSPARENT);
        if (type.isNone) {
            view.setVisibility(INVISIBLE);
        }
        return view;
    }

    /**
     * 重置样式与位置
     * @param outGridCount
     * @return
     */
    public ActiveBox resetRandGridType(Size outGridCount) {
        GridTypeMatrix gridTypeMatrix = matrixManager.getRandomMatrix();
        gridTypeMatrix.randomColor();
        setGridType(gridTypeMatrix);
        Rect activeBoxRect = new Rect();
        activeBoxRect.left = (outGridCount.getWidth() - gridCount.getWidth()) / 2;
        activeBoxRect.right = activeBoxRect.left + gridCount.getWidth();
        activeBoxRect.top = 0;
        activeBoxRect.bottom = activeBoxRect.top + gridCount.getHeight();
        setRect(activeBoxRect);
        return this;
    }
}
