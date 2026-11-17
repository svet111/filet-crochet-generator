package com.example.crochet_generator.model;

public class CrochetGrid {
    private final int width;
    private final int height;
    private final boolean[][] cells; // true = filled block, false = open mesh

    public CrochetGrid(boolean[][] cells) {
        this.height = cells.length;
        this.width = cells.length > 0 ? cells[0].length : 0;
        this.cells = cells;
    }

    public boolean isFilled(int row, int col) {
        return cells[row][col];
    }

    public boolean[][] getCells() {
        return cells;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}