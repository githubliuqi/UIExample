package com.example.tetris;

import android.graphics.Color;

import java.util.Random;

public abstract class GridTypeMatrix {

    private static final int[] COLORS = {Color.MAGENTA, Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN};
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    protected final GridType[][] gridTypes;

    private boolean isRandomColor = true;

    public GridTypeMatrix() {
        gridTypes = createGridType();
    }

    public GridType getGridType(int row, int column) {
        return gridTypes[row][column];
    }

    public int getRow() {
        return gridTypes.length;
    }

    public int getColumn() {
        return gridTypes[0].length;
    }

    public GridTypeMatrix setRandomColor(boolean isRandomColor) {
        this.isRandomColor = isRandomColor;
        return this;
    }

    public GridTypeMatrix randomColor() {
        if (!isRandomColor) {
            return this;
        }
        int colorIndex = RANDOM.nextInt(COLORS.length);
        int color = COLORS[colorIndex];
        return setColor(color);
    }

    public GridTypeMatrix setColor(int color) {
        for(int i = 0; i < getRow(); i++) {
            for (int j = 0; j < getColumn(); j++) {
                getGridType(i, j).color = color;
            }
        }
        return this;
    }

    public GridTypeMatrix rotate90() {
        GridType[][] types = ArrayUtils.rotate90(gridTypes);
        return new GridTypeMatrix() {
            @Override
            protected GridType[][] createGridType() {
                return types;
            }
        };
    }

    protected abstract GridType[][] createGridType();
}
