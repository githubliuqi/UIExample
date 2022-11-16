package com.example.tetris.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.util.Size;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.tetris.GameDef.GridType;

public class GridView extends View {

    private GridType gridType;
    private final GradientDrawable gradientDrawable = new GradientDrawable();
    private int strokeWidth = 1;
    private int strokeColor = Color.BLACK;
    private Size size = new Size(0, 0);

    public GridView(Context context, GridType type) {
        super(context);
        setGridType(type);
    }

    public GridView setGridType(GridType type) {
        if (gridType == type) {
            return this;
        }
        gridType = type;
        int color = getColor();
        gradientDrawable.setColor(color);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        setBackground(gradientDrawable);
        return this;
    }

    public GridView setSize(Size size) {
        if (size != null) {
            this.size = size;
        }
        return this;
    }

    public GridType getGridType() {
        return gridType;
    }

    private int getColor() {
        if (gridType == GridType.TYPE_DEFAULT) {
            return Color.GRAY;
        } else if (gridType == GridType.TYPE_RED) {
            return Color.RED;
        } else if (gridType == GridType.TYPE_GREEN) {
            return Color.GREEN;
        } else if (gridType == GridType.TYPE_BLUE) {
            return Color.BLUE;
        } else if (gridType == GridType.TYPE_MAGENTA) {
            return Color.MAGENTA;
        } else if (gridType == GridType.TYPE_YELLOW) {
            return Color.YELLOW;
        } else {
            return Color.GRAY;
        }
    }

    @Override
    public String toString() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        Point point = new Point();
        point.x = params.leftMargin / size.getWidth();
        point.y = params.topMargin / size.getHeight();
        return "GridView{" +
                "gridType=" + gridType +
                ", x=" + point.x +
                ", y=" + point.y +
                '}';
    }
}
