package com.rderoovers.minesweeper.domain;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperSquare {

    private boolean mined;
    private boolean revealed;
    private boolean flagged;
    private int index;
    private int row;
    private int col;
    private long minedNeighbors;
    private List<Integer> neighborsPositions;

    public MinesweeperSquare() {
        this.neighborsPositions = new ArrayList<>();
    }

    public MinesweeperSquare(int index, int row, int col, boolean mined) {
        this.index = index;
        this.row = row;
        this.col = col;
        this.mined = mined;
        this.neighborsPositions = new ArrayList<>();
    }

    public boolean isMined() {
        return mined;
    }

    public void setMined(boolean mined) {
        this.mined = mined;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void getCol(int col) {
        this.col = col;
    }

    public void setNeighborMineCount(long minedNeighbors) {
        this.minedNeighbors = minedNeighbors;
    }

    public long getMinedNeighbors() {
        return minedNeighbors;
    }

    public List<Integer> getNeighborsPositions() {
        return neighborsPositions;
    }

    public void setNeighborsPositions(List<Integer> neighborsIndexes) {
        this.neighborsPositions.clear();
        this.neighborsPositions.addAll(neighborsIndexes);
    }

    public void mine() {
        this.mined = true;
    }

    public void reveal() {
        this.revealed = true;
        this.flagged = false;
    }

    public void flag() {
        flagged = true;
    }

    public void unflag() {
        flagged = false;
    }

}
