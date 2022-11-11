package com.example.tetris;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Size;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Random;

public class TetrisGrid extends RelativeLayout {

    protected Context context;

    protected Size gridCount = new Size(20, 20);
    protected Size gridSize = new Size(60, 60);
    private View[][] mainViews;

    private int[] colors = new int[] {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.GREEN};
    private Random random = new Random(colors.length);
    private final GradientDrawable gradientDrawable = new GradientDrawable();

    public TetrisGrid(Context context, Size size, Size count) {
        super(context);
        this.context = context;
        setGridSize(size);
        setGridCount(count);
        gradientDrawable.setStroke(1, Color.RED);
        gradientDrawable.setColor(Color.GRAY);
        int padding = 5;
        setPadding(padding, padding, padding, padding);
        setBackgroundColor(Color.RED);
    }

    public TetrisGrid setGridSize(Size size) {
        gridSize = size;
        return this;
    }

    public TetrisGrid setGridCount(Size size) {
        gridCount = size;
        mainViews = new View[size.getWidth()][size.getHeight()];
        return this;
    }

    public TetrisGrid update() {
        removeAllViews();
        for (int i = 0; i < mainViews.length; i++) {
            for (int j = 0; j < mainViews[0].length; j++) {
                View view = new View(context);
                mainViews[i][j] = view;
                addView(view);
                RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.width = gridSize.getWidth();
                params.height = gridSize.getHeight();
                params.leftMargin = i * gridSize.getWidth();
                params.topMargin = j * gridSize.getHeight();
//                view.setBackgroundColor(colors[random.nextInt(colors.length)]);
                view.setBackground(gradientDrawable);
            }
        }
        return this;
    }

    public TetrisGrid setGridLine(int borderWidth, int borderColor) {
        gradientDrawable.setStroke(borderWidth, borderColor);
        return this;
    }

    public TetrisGrid setGridColor(int color) {
        gradientDrawable.setColor(color);
        return this;
    }
}
