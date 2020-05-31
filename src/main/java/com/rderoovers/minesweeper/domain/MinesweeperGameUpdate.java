package com.rderoovers.minesweeper.domain;

public class MinesweeperGameUpdate {

    private final long id;
    private final int square;
    private final long time;

    public MinesweeperGameUpdate(long id, long time, int square) {
        this.id = id;
        this.square = square;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public int getSquare() {
        return square;
    }

    public long getTime() {
        return time;
    }
}
