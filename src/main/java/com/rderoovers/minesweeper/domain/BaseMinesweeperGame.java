package com.rderoovers.minesweeper.domain;

public class BaseMinesweeperGame {

    private boolean victory;
    private boolean finished;
    private int rowCount;
    private int columnCount;
    private long id;

    public boolean isVictory() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BaseMinesweeperGame() {

    }

    public BaseMinesweeperGame(long id, int rowCount, int columnCount, boolean finished, boolean victory) {
        this.id = id;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.finished = finished;
        this.victory = victory;
    }

}
