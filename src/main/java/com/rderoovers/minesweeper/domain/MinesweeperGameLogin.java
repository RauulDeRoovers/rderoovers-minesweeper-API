package com.rderoovers.minesweeper.domain;

public class MinesweeperGameLogin {

    private long id;
    private String user;
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MinesweeperGameLogin() {

    }
}
