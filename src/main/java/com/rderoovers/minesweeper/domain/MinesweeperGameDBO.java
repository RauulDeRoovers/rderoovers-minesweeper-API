package com.rderoovers.minesweeper.domain;

public class MinesweeperGameDBO extends BaseMinesweeperGame {

    private int mineCount;
    private MinesweeperSquare[][] squares;

    public MinesweeperGameDBO() {

    }

    public int getMineCount() {
        return mineCount;
    }

    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }

    public MinesweeperSquare[][] getSquares() {
        return squares;
    }

    public void setSquares(MinesweeperSquare[][] squares) {
        this.squares = squares;
    }

}
