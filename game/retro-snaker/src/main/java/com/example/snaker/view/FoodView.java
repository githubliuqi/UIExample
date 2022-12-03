package com.example.snaker.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Size;

import java.util.Random;

public class FoodView extends GameGridItemView {

    private static final int[] COLORS = {
            Color.MAGENTA, Color.BLUE, Color.YELLOW, Color.RED,
            Color.GREEN, Color.CYAN, Color.LTGRAY, Color.LTGRAY
    };

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public FoodView(Context context) {
        super(context);
    }

    @Override
    public FoodView setSize(Size size) {
        super.setSize(size);
//        setCornerRadius(size.getWidth()/2);
        return this;
    }

    private static int randColor() {
        return COLORS[RANDOM.nextInt(COLORS.length)];
    }

    public FoodView setRandColor() {
//        getGridType().color = randColor();
//        setGridType(getGridType());
        return this;
    }
}
