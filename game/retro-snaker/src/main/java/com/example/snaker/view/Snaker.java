package com.example.snaker.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Size;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.juiexample.common.grid.GridItemView;
import com.example.snaker.model.GameDef;
import com.example.snaker.model.GameOnUpdateListener;

import java.util.ArrayList;
import java.util.List;

public class Snaker {

    private final List<GridItemView> gridViewList = new ArrayList<>();

    private Context context;
    private RelativeLayout parentView;
    private GameDef.MoveType moveDirection = GameDef.MoveType.TYPE_MOVE_LEFT;
    private Size nodeSize;
    private final GameOnUpdateListener gridTypeListener = new GameOnUpdateListener();

    public Snaker(Context context, RelativeLayout parentView, Size nodeSize) {
        this.context = context;
        this.parentView = parentView;
        this.nodeSize = nodeSize;
        addHeadView();
    }

    private void addHeadView() {
        GridItemView gridItemView = new GameGridItemView(context);
        gridItemView.setSize(nodeSize);
        gridItemView.setOnUpdateListener(gridTypeListener);
        gridItemView.setGridType(10);
        gridItemView.setPoint(new Point(5, 5));
        parentView.addView(gridItemView);
        gridViewList.add(gridItemView);
    }

    public Snaker addNodeView(GameGridItemView gridItemView) {
        GridItemView lastView = gridViewList.get(gridViewList.size()-1);
        gridItemView.setSize(nodeSize);
        Point lastPoint = lastView.getPoint();
        gridItemView.setOnUpdateListener(gridTypeListener);
        gridItemView.setGridType(11);
        gridItemView.setPoint(lastPoint);
        gridItemView.setVisibility(View.GONE);
        parentView.addView(gridItemView);
        gridViewList.add(gridItemView);
        return this;
    }

    public Snaker setMoveDirection(GameDef.MoveType moveType) {
        moveDirection = moveType;
        return this;
    }

    public Snaker setHeadPoint(Point point) {
        gridViewList.get(0).setPoint(point);
        gridViewList.get(0).setBackground(new ColorDrawable(Color.RED));
        gridViewList.get(0).setVisibility(View.VISIBLE);
        return this;
    }

    public Point getHeadPoint() {
        return gridViewList.get(0).getPoint();
    }

    public boolean move() {
        Point prePoint = new Point();

        GridItemView headView = gridViewList.get(0);
        prePoint = headView.getPoint();

        if (moveDirection == GameDef.MoveType.TYPE_MOVE_DOWN) {
            prePoint.y += 1;
        } else if (moveDirection == GameDef.MoveType.TYPE_MOVE_UP) {
            prePoint.y -= 1;
        } else if (moveDirection == GameDef.MoveType.TYPE_MOVE_LEFT) {
            prePoint.x -= 1;
        } else if (moveDirection == GameDef.MoveType.TYPE_MOVE_RIGHT) {
            prePoint.x += 1;
        }

        Rect parentRect = new Rect();
        Size gridSize = headView.getSize();
        parentView.getDrawingRect(parentRect);
        parentRect.left /= gridSize.getWidth();
        parentRect.top /= gridSize.getHeight();
        parentRect.right /= gridSize.getWidth();
        parentRect.bottom /= gridSize.getHeight();
        if (!parentRect.contains(prePoint.x, prePoint.y)) {
            return false;
        }

        for (int i = 0; i < gridViewList.size(); i++) {
            GridItemView gridView = gridViewList.get(i);
            if (gridView.getVisibility() == View.GONE) {
                gridView.setVisibility(View.VISIBLE);
                continue;
            }

            Point currentPoint = gridView.getPoint();
            gridView.setPoint(prePoint);

            prePoint.x = currentPoint.x;
            prePoint.y = currentPoint.y;
        }
        return true;
    }

    public int getLength() {
        return gridViewList.size();
    }

    public boolean overLapPoint(Point point) {
        for (int i = 0; i < gridViewList.size(); i++) {
            GridItemView gridView = gridViewList.get(i);
            if (gridView.getPoint().equals(point)) {
                return true;
            }
        }
        return false;
    }
}
