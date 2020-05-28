package com.rderoovers.minesweeper.domain;

public class MinesweeperSquareSafe extends MinesweeperSquareDTO {

    private final long neighborsWithMines;

    public long getNeighborsWithMines() {
        return neighborsWithMines;
    }

    public MinesweeperSquareSafe(long index, long neighborsWithMines) {
        super(index);
        this.neighborsWithMines = neighborsWithMines;
    }

    @Override
    protected MinesweeperSquareType initializeType() {
        return MinesweeperSquareType.SAFE;
    }

}