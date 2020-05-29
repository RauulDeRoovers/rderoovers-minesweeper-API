package com.rderoovers.minesweeper.domain;

public class MinesweeperGameDTO extends BaseMinesweeperGame {

    private final MinesweeperSquareDTO[][] squares;

    public MinesweeperSquareDTO[][] getSquares() {
        return squares;
    }

    public MinesweeperGameDTO(long id, int rowCount, int columnCount, boolean finished, boolean victory, MinesweeperSquareDTO[][] squares) {
        super(id, rowCount, columnCount, finished, victory);
        this.squares = squares;
    }

}
