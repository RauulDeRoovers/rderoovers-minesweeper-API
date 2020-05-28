package com.rderoovers.minesweeper.domain;

public class MinesweeperGameUpdate {

    private final long id;
    private final int square;

    public MinesweeperGameUpdate(long id, int square) {
        this.id = id;
        this.square = square;
    }

    public long getId() {
        return id;
    }

    public int getSquare() {
        return square;
    }
}
