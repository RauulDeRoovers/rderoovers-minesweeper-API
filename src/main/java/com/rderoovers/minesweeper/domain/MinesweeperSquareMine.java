package com.rderoovers.minesweeper.domain;

public class MinesweeperSquareMine extends MinesweeperSquareDTO {

    public MinesweeperSquareMine(long index) {
        super(index);
    }

    @Override
    protected MinesweeperSquareType initializeType() {
        return MinesweeperSquareType.MINE;
    }

}