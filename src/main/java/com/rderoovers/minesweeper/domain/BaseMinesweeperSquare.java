package com.rderoovers.minesweeper.domain;

public abstract class BaseMinesweeperSquare {

    private final long index;

    public long getIndex() {
        return index;
    }

    protected BaseMinesweeperSquare(long index) {
        this.index = index;
    }
}
