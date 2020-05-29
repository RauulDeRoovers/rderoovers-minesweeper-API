package com.rderoovers.minesweeper.domain;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MinesweeperGame {

    private boolean victory;
    private boolean finished;
    private final long id;
    private final int rowCount;
    private final int columnCount;
    private final int mineCount;
    private final MinesweeperSquare[][] squares;
    private final List<MinesweeperSquare> flatSquares;

    public MinesweeperGame(long id, int rowCount, int columnCount, int mineCount, MinesweeperSquare[][] squares) {
        this.id = id;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        this.squares = squares;
        this.flatSquares = new ArrayList<>();
        for (MinesweeperSquare[] row : squares) {
            flatSquares.addAll(Arrays.asList(row));
        }
        this.flatSquares.forEach(this::updateSquareNeighborsMines);
    }

    private void updateSquareNeighborsMines(MinesweeperSquare minesweeperSquare) {
        List<MinesweeperSquare> neighbors = new ArrayList<>();
        for (Pair<Integer, Integer> position : minesweeperSquare.getNeighborsPositions()) {
            int neighborIndex = (position.getKey() * this.columnCount) + position.getValue();
            neighbors.add(this.flatSquares.get(neighborIndex));
        }
        long mineNeighbors = neighbors.stream().filter(MinesweeperSquare::isMined).count();
        minesweeperSquare.setNeighborMineCount(mineNeighbors);
    }

    public long getId() {
        return id;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public MinesweeperSquare[][] getSquares() {
        return squares;
    }

    public void selectSquare(int index) throws IndexOutOfBoundsException {
        if (index > this.flatSquares.size()) {
            throw new IndexOutOfBoundsException();
        }
        MinesweeperSquare square = this.flatSquares.get(index);
        if (square.isFlagged()) {
            return;
        }
        if (square.isMined()) {
            this.setResult(false);
            return;
        }
        square.reveal();
        // find neighbors
        List<MinesweeperSquare> neighbors = this.getSquareNeighbors(square);
        if (neighbors.stream().noneMatch(MinesweeperSquare::isMined)) {
            this.revealNeighbors(neighbors);
        }
        if (this.flatSquares.stream().filter(MinesweeperSquare::isRevealed).count() == (this.rowCount * this.columnCount) - this.mineCount) {
            this.setResult(true);
        }
    }

    private void revealNeighbors(List<MinesweeperSquare> neighbors) {
        List<MinesweeperSquare> unrevealedNeighbors = neighbors.stream().filter(x -> !x.isRevealed()).collect(Collectors.toList());
        unrevealedNeighbors.forEach(this::checkSquare);
    }

    private List<MinesweeperSquare> getSquareNeighbors(MinesweeperSquare square) {
        List<MinesweeperSquare> neighbors = new ArrayList<>();
        for (Pair<Integer, Integer> position : square.getNeighborsPositions()) {
            int neighborIndex = (position.getKey() * this.columnCount) + position.getValue();
            neighbors.add(this.flatSquares.get(neighborIndex));
        }
        return neighbors;
    }

    private void checkSquare(MinesweeperSquare square) {
        square.reveal();
        List<MinesweeperSquare> neighbors = this.getSquareNeighbors(square);
        if (neighbors.stream().anyMatch(MinesweeperSquare::isMined)) {
            return;
        }
        this.revealNeighbors(neighbors);
    }

    private void setResult(boolean victory) {
        if (this.finished) {
            throw new IllegalStateException("Game is finished, no further operations are allowed.");
        }
        this.victory = victory;
        this.finished = true;
        this.flatSquares.stream().filter(MinesweeperSquare::isMined).forEach(MinesweeperSquare::reveal);
    }

    public boolean isVictory() {
        return victory;
    }

    public boolean isFinished() {
        return finished;
    }

    public void flagSquare(int index) throws IndexOutOfBoundsException{
        if (index > this.flatSquares.size()) {
            throw new IndexOutOfBoundsException();
        }
        MinesweeperSquare square = this.flatSquares.get(index);
        if (square.isRevealed()) {
            return;
        }
        if (square.isFlagged()) {
            square.unflag();
        }
        else {
            square.flag();
        }
    }
}
