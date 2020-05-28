package com.rderoovers.minesweeper.domain;

public class MinesweeperGameDTO extends BaseMinesweeperGame {

    private final MinesweeperSquareDTO[][] squares;

    public MinesweeperSquareDTO[][] getSquares() {
        return squares;
    }

    public MinesweeperGameDTO(MinesweeperGame minesweeperGame) {
        super(minesweeperGame.getId(), minesweeperGame.getRowCount(), minesweeperGame.getColumnCount(), minesweeperGame.isFinished(), minesweeperGame.isVictory());
        this.squares = new MinesweeperSquareDTO[this.getRowCount()][this.getColumnCount()];
        for (int row = 0; row < this.getRowCount(); row++) {
            MinesweeperSquare[] rowSquares = minesweeperGame.getSquares()[row];
            for (int col = 0; col < this.getColumnCount(); col++) {
                MinesweeperSquare square = rowSquares[col];
                long index = square.getIndex();
                this.squares[row][col] = !square.isRevealed() ?
                        new MinesweeperSquareUnknown(index) : square.isMined() ?
                            new MinesweeperSquareMine(index) :
                            new MinesweeperSquareSafe(index, square.getMinedNeighbors());
            }
        }
    }

}
