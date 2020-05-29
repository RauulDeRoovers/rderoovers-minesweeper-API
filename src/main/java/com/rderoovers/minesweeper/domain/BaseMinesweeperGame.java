package com.rderoovers.minesweeper.domain;

public class BaseMinesweeperGame {

    private boolean victory;
    private boolean finished;
    private final int rowCount;
    private final int columnCount;
    private final long id;

    public BaseMinesweeperGame(long id, int rowCount, int columnCount, boolean finished, boolean victory) {
        this.id = id;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.finished = finished;
        this.victory = victory;
    }

    public boolean isVictory() {
        return victory;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public long getId() {
        return id;
    }

}
