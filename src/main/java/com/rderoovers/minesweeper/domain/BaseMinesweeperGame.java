package com.rderoovers.minesweeper.domain;

public class BaseMinesweeperGame {

    private boolean victory;
    private boolean finished;
    private int rowCount;
    private int columnCount;
    private int mineCount;
    private long id;
    private long playTime;

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

    public int getMineCount() {
        return mineCount;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }

    public BaseMinesweeperGame() {

    }

    public BaseMinesweeperGame(long id, int rowCount, int columnCount, int mineCount, long playTime, boolean finished, boolean victory) {
        this.id = id;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.finished = finished;
        this.victory = victory;
        this.mineCount = mineCount;
        this.playTime = playTime;
    }

}
