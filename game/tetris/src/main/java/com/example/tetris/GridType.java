package com.example.tetris;

import java.util.Objects;

public class GridType {

    public int color;
    public boolean isNone;

    public GridType(boolean isNone, int color) {
        this.isNone = isNone;
        this.color = color;
    }

    public GridType(boolean isNone) {
        this(isNone, -1);
    }

    public GridType clone(GridType gridType) {
        this.isNone = gridType.isNone;
        this.color = gridType.color;
        return this;
    }

    public GridType clone() {
        GridType gridType = new GridType(this.isNone, this.color);
        return gridType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridType gridType = (GridType) o;
        return color == gridType.color && isNone == gridType.isNone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, isNone);
    }
}
