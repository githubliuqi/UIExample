package com.example.snaker.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import com.example.juiexample.common.grid.GridItemView;

public class GameGridItemView extends GridItemView {

    private final GradientDrawable gradientDrawable = new GradientDrawable();
    private int strokeWidth = 1;
    private int strokeColor = 0xAF666666;
    private int color = 0x2F666666;

    public GameGridItemView(Context context) {
        super(context);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setColor(color);
        setBackground(gradientDrawable);
    }

    public GameGridItemView setColor(int color) {
        this.color = color;
        gradientDrawable.setColor(color);
        setBackground(gradientDrawable);
        return this;
    }

    public GameGridItemView setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        setBackground(gradientDrawable);
        return this;
    }

    public GameGridItemView setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        setBackground(gradientDrawable);
        return this;
    }

    public GameGridItemView setCornerRadius(int radius) {
        gradientDrawable.setCornerRadius(radius);
        setBackground(gradientDrawable);
        return this;
    }

    @Override
    public void copyFrom(GridItemView srcView) {
        if (srcView == null) {
            return;
        }
        GameGridItemView srcItemView = (GameGridItemView) srcView;
        setColor(srcItemView.color);
    }
}
