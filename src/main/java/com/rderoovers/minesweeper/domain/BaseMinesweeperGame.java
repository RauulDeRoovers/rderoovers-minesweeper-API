package com.rderoovers.minesweeper.domain;

public class BaseMinesweeperGame {

    // TODO: how to ensure victory is not set more than once?
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

    public void finish() {
        this.finished = true;
    }

    private void won() {
        this.victory = true;
    }

    private void lost() {
        this.victory = false;
    }

}
