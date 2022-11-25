package com.example.tetris;

import com.example.common.TLog;

import java.util.Arrays;

public class ArrayUtils {

    private static final String TAG = "ArrayUtils";

    public static int[][] rotate90(int[][] array) {
        if (array == null) {
            return null;
        }
        TLog.d(TAG, toString(array));
        int[][] arr = new int[array[0].length][array.length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                arr[j][i] = array[array.length - i - 1][j];
            }
        }
        TLog.d(TAG, toString(arr));
        return arr;
    }

    public static GridType[][] rotate90(GridType[][] array) {
        if (array == null) {
            return null;
        }
        TLog.d("liuqi", toString(array));
        GridType[][] arr = new GridType[array[0].length][array.length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                arr[j][i] = array[array.length - i - 1][j];
            }
        }
        TLog.d("liuqi", toString(arr));
        return arr;
    }

    public static final String toString(int[][] array) {
        if (array == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(Arrays.toString(array[i]));
            sb.append(",\n");
        }
        return sb.toString();
    }

    public static final String toString(GridType[][] array) {
        if (array == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(Arrays.toString(array[i]));
            sb.append(",\n");
        }
        return sb.toString();
    }
}
