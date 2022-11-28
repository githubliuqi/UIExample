package com.example.tetris.model;

import android.graphics.Color;

import java.util.Random;

public abstract class GridTypeMatrix {

    private static final int[] COLORS = {
            Color.MAGENTA, Color.BLUE, Color.YELLOW, Color.RED,
            Color.GREEN, Color.CYAN, Color.LTGRAY, Color.LTGRAY
    };

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
        GridType[][] arr = new GridType[gridTypes[0].length][gridTypes.length];
        for (int i = 0; i < gridTypes.length; i++) {
            for (int j = 0; j < gridTypes[0].length; j++) {
                arr[j][i] = gridTypes[gridTypes.length - i - 1][j];
            }
        }
        return new GridTypeMatrix() {
            @Override
            protected GridType[][] createGridType() {
                return arr;
            }
        };
    }

    protected abstract GridType[][] createGridType();
}
