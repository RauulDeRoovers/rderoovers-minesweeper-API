package com.rderoovers.minesweeper.domain;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MinesweeperGame extends MinesweeperGameDBO {

    private final List<MinesweeperSquare> flatSquares;

    public MinesweeperGame(MinesweeperGameDBO minesweeperGameDBO) {
        this.setId(minesweeperGameDBO.getId());
        this.setRowCount(minesweeperGameDBO.getRowCount());
        this.setColumnCount(minesweeperGameDBO.getColumnCount());
        this.setFinished(minesweeperGameDBO.isFinished());
        this.setVictory(minesweeperGameDBO.isVictory());
        this.setMineCount(minesweeperGameDBO.getMineCount());
        this.setSquares(minesweeperGameDBO.getSquares());
        this.flatSquares = new ArrayList<>();
        for (MinesweeperSquare[] row : this.getSquares()) {
            flatSquares.addAll(Arrays.asList(row));
        }
        this.flatSquares.forEach(this::updateSquareNeighborsMines);
    }

    private void updateSquareNeighborsMines(MinesweeperSquare minesweeperSquare) {
        List<MinesweeperSquare> neighbors = new ArrayList<>();
        for (Integer position : minesweeperSquare.getNeighborsPositions()) {
            //int neighborIndex = (position.getKey() * this.getColumnCount()) + position.getValue();
            neighbors.add(this.flatSquares.get(position));
        }
        long mineNeighbors = neighbors.stream().filter(MinesweeperSquare::isMined).count();
        minesweeperSquare.setNeighborMineCount(mineNeighbors);
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
        if (this.flatSquares.stream().filter(MinesweeperSquare::isRevealed).count() == (this.getRowCount() * this.getColumnCount()) - this.getMineCount()) {
            this.setResult(true);
        }
    }

    private void revealNeighbors(List<MinesweeperSquare> neighbors) {
        List<MinesweeperSquare> unrevealedNeighbors = neighbors.stream().filter(x -> !x.isRevealed()).collect(Collectors.toList());
        unrevealedNeighbors.forEach(this::checkSquare);
    }

    private List<MinesweeperSquare> getSquareNeighbors(MinesweeperSquare square) {
        List<MinesweeperSquare> neighbors = new ArrayList<>();
        for (Integer position : square.getNeighborsPositions()) {
            neighbors.add(this.flatSquares.get(position));
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
        if (this.isFinished()) {
            throw new IllegalStateException("Game is finished, no further operations are allowed.");
        }
        this.setVictory(victory);
        this.setFinished(true);
        this.flatSquares.stream().filter(MinesweeperSquare::isMined).forEach(MinesweeperSquare::reveal);
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
