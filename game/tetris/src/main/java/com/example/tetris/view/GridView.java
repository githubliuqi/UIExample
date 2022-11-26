package com.example.tetris.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.util.Size;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.tetris.GridType;

public class GridView extends View {

    private GridType gridType;
    private final GradientDrawable gradientDrawable = new GradientDrawable();
    private int strokeWidth = 1;
    private int strokeColor = 0xAF666666;
    private Size size = new Size(0, 0);

    public GridView(Context context, GridType type) {
        super(context);
        setGridType(type);
    }

    public GridView setGridType(GridType type) {
        gridType = type;
        gradientDrawable.setColor(gridType.color);
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

    public GridView setCornerRadius(int radius) {
        gradientDrawable.setCornerRadius(radius);
        setBackground(gradientDrawable);
        return this;
    }

    public GridView setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        setBackground(gradientDrawable);
        return this;
    }

    public GridView setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        setBackground(gradientDrawable);
        return this;
    }

    public GridType getGridType() {
        return gridType;
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
