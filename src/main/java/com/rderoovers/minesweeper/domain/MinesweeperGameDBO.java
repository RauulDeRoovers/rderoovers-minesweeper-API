package com.rderoovers.minesweeper.domain;

public class MinesweeperGameDBO extends BaseMinesweeperGame {

    private MinesweeperSquare[][] squares;

    public MinesweeperGameDBO() {

    }

    public MinesweeperSquare[][] getSquares() {
        return squares;
    }

    public void setSquares(MinesweeperSquare[][] squares) {
        this.squares = squares;
    }

}
