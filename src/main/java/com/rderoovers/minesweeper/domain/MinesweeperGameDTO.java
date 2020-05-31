package com.rderoovers.minesweeper.domain;

public class MinesweeperGameDTO extends BaseMinesweeperGame {

    private MinesweeperSquareDTO[][] squares;

    public MinesweeperSquareDTO[][] getSquares() {
        return squares;
    }

    public void setSquares(MinesweeperSquareDTO[][] squares) {
        this.squares = squares;
    }

    public MinesweeperGameDTO() {

    }

    public MinesweeperGameDTO(long id, int rowCount, int columnCount, int mineCount, long playTime, boolean finished, boolean victory, MinesweeperSquareDTO[][] squares) {
        super(id, rowCount, columnCount, mineCount, playTime, finished, victory);
        this.squares = squares;
    }

}
