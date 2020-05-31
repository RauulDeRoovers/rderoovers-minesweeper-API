package com.rderoovers.minesweeper.domain;

public abstract class MinesweeperGameRequest {

    private MinesweeperGameLogin minesweeperGameLogin;

    public MinesweeperGameLogin getMinesweeperGameLogin() {
        return minesweeperGameLogin;
    }

    public void setMinesweeperGameLogin(MinesweeperGameLogin minesweeperGameLogin) {
        this.minesweeperGameLogin = minesweeperGameLogin;
    }

}
