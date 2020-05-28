package com.rderoovers.minesweeper.domain;

public abstract class MinesweeperSquareDTO extends BaseMinesweeperSquare {

    private final MinesweeperSquareType type;

    public MinesweeperSquareType getType() {
        return this.type;
    }

    public MinesweeperSquareDTO(long index) {
        super(index);
        this.type = initializeType();
    }

    protected abstract MinesweeperSquareType initializeType();

}
