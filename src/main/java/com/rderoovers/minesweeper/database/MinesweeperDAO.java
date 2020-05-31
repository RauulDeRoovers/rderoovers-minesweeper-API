package com.rderoovers.minesweeper.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rderoovers.minesweeper.domain.MinesweeperGameDBO;
import com.rderoovers.minesweeper.domain.MinesweeperGameLogin;

import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MinesweeperDAO {

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://ec2-18-210-214-86.compute-1.amazonaws.com:5432/df07oro9fp521j?user=lwlxavyzarlohc&password=377f80cec2979296388b29b9cc3d612bb3de07b40b0bec58c51bf6e9b8947318&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        return DriverManager.getConnection(url);
    }

    private String createJSON(MinesweeperGameDBO minesweeperGameDBO) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(minesweeperGameDBO);
    }

    private MinesweeperGameDBO createMinesweeperGameDBO(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, MinesweeperGameDBO.class);
    }

    private MinesweeperGameDBO createMinesweeperGameDBO(String json, long gameId) throws JsonProcessingException {
        MinesweeperGameDBO minesweeperGameDBO = this.createMinesweeperGameDBO(json);
        minesweeperGameDBO.setId(gameId);
        return minesweeperGameDBO;
    }

    public void createGame(MinesweeperGameDBO minesweeperGameDBO) throws SQLException, URISyntaxException, JsonProcessingException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO GAME (game_body, user_id, created_datetime, modified_datetime) VALUES (?,?,?,?) RETURNING game_id");
        String json = this.createJSON(minesweeperGameDBO);
        preparedStatement.setString(1, json);
        preparedStatement.setInt(2, 1);
        Timestamp createdDate = new Timestamp(System.currentTimeMillis());
        preparedStatement.setTimestamp(3, createdDate);
        preparedStatement.setTimestamp(4, createdDate);
        ResultSet rs = preparedStatement.executeQuery();
        try {
            rs.next();
            long gameId = rs.getLong(1);
            minesweeperGameDBO.setId(gameId);
        }
        catch (Exception e) {
            throw new SQLException("Creating game failed, no ID obtained.");
        }
        connection.close();
    }

    public void updateGame(MinesweeperGameDBO minesweeperGameDBO) throws SQLException, JsonProcessingException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE GAME SET game_body = ?, modified_datetime = ? WHERE game_id = ?");
        String json = this.createJSON(minesweeperGameDBO);
        preparedStatement.setString(1, json);
        Timestamp createdDate = new Timestamp(System.currentTimeMillis());
        preparedStatement.setTimestamp(2, createdDate);
        preparedStatement.setLong(3, minesweeperGameDBO.getId());
        int affectedRecords = preparedStatement.executeUpdate();
        if (affectedRecords != 1) {
            throw new SQLException("Creating game failed, no ID obtained.");
        }
        connection.close();
    }

    public void saveGame(MinesweeperGameDBO minesweeperGameDBO) throws URISyntaxException, SQLException, JsonProcessingException {
        if (minesweeperGameDBO.getId() > 0) {
            updateGame(minesweeperGameDBO);
        }
        else {
            createGame(minesweeperGameDBO);
        }
    }

    public MinesweeperGameDBO loadGame(long gameId) throws SQLException, JsonProcessingException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT game_body, user_id FROM game WHERE game_id = ?");
        preparedStatement.setLong(1, gameId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String json = resultSet.getString(1);
        connection.close();
        return this.createMinesweeperGameDBO(json, gameId);
    }

    /*
    private ResultSet executeQuery(String query, Consumer<PreparedStatement> preparedStatementFiller) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatementFiller.accept(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        resultSet.get(1);
        connection.close();
    }
    */

    public List<MinesweeperGameDBO> loadAllGames() throws SQLException, JsonProcessingException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT game_id, game_body, user_id FROM game");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<MinesweeperGameDBO> minesweeperGameDBOList = new ArrayList<>();
        while (resultSet.next()) {
            long gameId = resultSet.getLong(1);
            String json = resultSet.getString(2);
            MinesweeperGameDBO minesweeperGameDBO = this.createMinesweeperGameDBO(json);
            minesweeperGameDBO.setId(gameId);
            minesweeperGameDBOList.add(minesweeperGameDBO);
        }
        connection.close();
        return minesweeperGameDBOList;
    }

    public boolean deleteGame(long gameId) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM game WHERE game_id = ?");
        preparedStatement.setLong(1, gameId);
        int affectedRecords = preparedStatement.executeUpdate();
        connection.close();
        return affectedRecords == 1;
    }

    public MinesweeperGameLogin getUser(MinesweeperGameLogin minesweeperGameLogin) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id, password FROM users WHERE user _name= ?");
        preparedStatement.setString(1, minesweeperGameLogin.getUser());
        MinesweeperGameLogin dbLogin = new MinesweeperGameLogin();
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            long userId = rs.getLong(1);
            String password = rs.getString(2);
            dbLogin.setId(userId);
            dbLogin.setPassword(password);
        }
        connection.close();
        return dbLogin;
    }

    public void createUser(MinesweeperGameLogin minesweeperGameLogin) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO USERS (user_name, password) VALUES (?,?) RETURNING user_id");
        preparedStatement.setString(1, minesweeperGameLogin.getUser());
        preparedStatement.setString(2, minesweeperGameLogin.getPassword());
        ResultSet rs = preparedStatement.executeQuery();
        try {
            rs.next();
            long userId = rs.getLong(1);
            minesweeperGameLogin.setId(userId);
        }
        catch (Exception e) {
            throw new SQLException("Creating user failed, no ID obtained.");
        }
    }
}
