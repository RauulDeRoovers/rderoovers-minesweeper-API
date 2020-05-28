package com.rderoovers.minesweeper.domain;

public class MinesweeperSquareUnknown extends MinesweeperSquareDTO {

    public MinesweeperSquareUnknown(long index) {
        super(index);
    }

    @Override
    protected MinesweeperSquareType initializeType() {
        return MinesweeperSquareType.UNKNOWN;
    }

}