package com.example.tetris;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 矩阵样式管理器
 */
public class GridTypeMatrixManager {

    private static final GridTypeMatrixManager INSTANCE = new GridTypeMatrixManager();

    private final List<GridTypeMatrix> gridTypeMatrixs = new ArrayList<>();
    private final Random RANDOM = new Random();

    public static final GridTypeMatrixManager getInstance() {
        return INSTANCE;
    }

    private GridTypeMatrixManager() {
        addMatrix(new int[][]{{1, 1, 1, 1}});
        addMatrix(new int[][]{{1}, {1}, {1}, {1}});
        addMatrix(new int[][]{{1, 1}, {1, 1}});
        addMatrix(new int[][]{{1, 0}, {1, 0}, {1, 1}});
        addMatrix(new int[][]{{0, 1}, {0, 1}, {1, 1}});
        addMatrix(new int[][]{{0, 1, 0}, {1, 1, 1}});
        addMatrix(new int[][]{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}});
//        addMatrix(new int[][]{{1, 0, 1}, {0, 1, 0}, {1, 0, 1}});
//        addMatrix(new int[][]{{1, 1, 1}, {0, 1, 0}, {1, 1, 1}});
    }

    /**
     * 添加矩阵样式
     * @param matrix 二维数组，元素为0/1， 0：无色块，1：有色块
     * @return
     */
    public GridTypeMatrixManager addMatrix(int[][] matrix) {
        GridTypeMatrix gridTypeMatrix = new GridTypeMatrix() {
            @Override
            protected GridType[][] createGridType() {
                GridType[][] gridTypes = new GridType[matrix.length][matrix[0].length];
                for (int i = 0; i < gridTypes.length; i++) {
                    for (int j = 0; j < gridTypes[0].length; j++) {
                        int gridType = matrix[i][j];
                        gridTypes[i][j] = new GridType(gridType == 0, -1);
                    }
                }
                return gridTypes;
            }
        };
        gridTypeMatrixs.add(gridTypeMatrix);
        return this;
    }

    public GridTypeMatrixManager addMatrix(GridTypeMatrix matrix) {
        if (matrix != null) {
            gridTypeMatrixs.add(matrix);
        }
        return this;
    }

    public GridTypeMatrix getRandomMatrix() {
        int rand = RANDOM.nextInt(gridTypeMatrixs.size());
        return gridTypeMatrixs.get(rand);
    }

}
