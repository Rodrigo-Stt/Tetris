package tetris;

import java.awt.Color;

public enum Tetromino {
    I(new int[][] {{1,1,1,1}},               new Color(0, 245, 255)),
    O(new int[][] {{1,1},{1,1}},              new Color(245, 166, 35)),
    T(new int[][] {{0,1,0},{1,1,1}},          new Color(155, 89, 182)),
    L(new int[][] {{1,0,0},{1,1,1}},          new Color(52, 152, 219)),
    J(new int[][] {{0,0,1},{1,1,1}},          new Color(231, 76, 60)),
    S(new int[][] {{0,1,1},{1,1,0}},          new Color(126, 211, 33)),
    Z(new int[][] {{1,1,0},{0,1,1}},          new Color(208, 2, 27));

    public final int[][] shape;
    public final Color color;

    Tetromino(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public static Tetromino random() {
        Tetromino[] values = values();
        return values[(int)(Math.random() * values.length)];
    }

    public int[][] rotated() {
        int rows = shape.length, cols = shape[0].length;
        int[][] result = new int[cols][rows];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                result[c][rows - 1 - r] = shape[r][c];
        return result;
    }
}
