package com.example.tetris;

import android.graphics.Rect;

public class RectUtils {

    public static final boolean coincideLeft(Rect rect1, Rect rect2) {
        if (rect1 == null || rect2 == null) {
            return false;
        }
        return rect1.left == rect2.left;
    }

    public static final boolean coincideRight(Rect rect1, Rect rect2) {
        if (rect1 == null || rect2 == null) {
            return false;
        }
        return rect1.right == rect2.right;
    }

    public static final boolean coincideBottom(Rect rect1, Rect rect2) {
        if (rect1 == null || rect2 == null) {
            return false;
        }
        return rect1.bottom == rect2.bottom;
    }
}
