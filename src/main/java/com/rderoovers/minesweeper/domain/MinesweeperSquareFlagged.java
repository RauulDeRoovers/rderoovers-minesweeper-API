package com.rderoovers.minesweeper.domain;

public class MinesweeperSquareFlagged extends MinesweeperSquareDTO {

    public MinesweeperSquareFlagged(long index) {
        super(index);
    }

    @Override
    protected MinesweeperSquareType initializeType() {
        return MinesweeperSquareType.FLAGGED;
    }

}
