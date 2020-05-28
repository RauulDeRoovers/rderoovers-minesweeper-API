package com.rderoovers.minesweeper.domain;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperSquare {



    private final List<Pair<Integer, Integer>> neighborsPositions;
    private final int index;
    private final int row;
    private final int col;
    private boolean mined;
    private boolean revealed;
    private long minedNeighbors;

    public MinesweeperSquare(int index, int row, int col, boolean mined) {
        this.index = index;
        this.row = row;
        this.col = col;
        this.mined = mined;
        this.neighborsPositions = new ArrayList<>();
    }

    public int getIndex() {
        return index;
    }

    public boolean isMined() {
        return mined;
    }

    public void doesHoldMine() {
        this.mined = true;
    }

    public void reveal() {
        this.revealed = true;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public List<Pair<Integer, Integer>> getNeighborsPositions() {
        return neighborsPositions;
    }

    public void setNeighborsPositions(List<Pair<Integer, Integer>> neighborsIndexes) {
        this.neighborsPositions.clear();
        this.neighborsPositions.addAll(neighborsIndexes);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setNeighborMineCount(long minedNeighbors) {
        this.minedNeighbors = minedNeighbors;
    }

    public long getMinedNeighbors() {
        return minedNeighbors;
    }
}
