package com.rderoovers.minesweeper.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rderoovers.minesweeper.domain.MinesweeperGameDBO;

import java.net.URISyntaxException;
import java.sql.*;

public class MinesweeperDAO {

    private String createJSON(MinesweeperGameDBO minesweeperGameDBO) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(minesweeperGameDBO);
        return json;
    }

    private MinesweeperGameDBO createMinesweeperGameDBO(String json, long gameId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MinesweeperGameDBO minesweeperGameDBO = mapper.readValue(json, MinesweeperGameDBO.class);
        minesweeperGameDBO.setId(gameId);
        return minesweeperGameDBO;
    }

    public void createGame(MinesweeperGameDBO minesweeperGameDBO) throws SQLException, URISyntaxException, JsonProcessingException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO GAME (game_body, user_id, created_datetime, modified_datetime) VALUES (?,?,?,?) RETURNING game_id");
        String json = this.createJSON(minesweeperGameDBO);
        preparedStatement.setString(1, json);
        preparedStatement.setInt(2, 1);
        Date createdDate = new Date(System.currentTimeMillis());
        preparedStatement.setDate(3, createdDate);
        preparedStatement.setDate(4, createdDate);
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
        Date createdDate = new Date(System.currentTimeMillis());
        preparedStatement.setDate(2, createdDate);
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

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://ec2-18-210-214-86.compute-1.amazonaws.com:5432/df07oro9fp521j?user=lwlxavyzarlohc&password=377f80cec2979296388b29b9cc3d612bb3de07b40b0bec58c51bf6e9b8947318&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        return DriverManager.getConnection(url);
    }

    public MinesweeperGameDBO loadGame(long gameId) throws SQLException, JsonProcessingException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT game_body, user_id FROM game WHERE game_id = ?");
        preparedStatement.setLong(1, gameId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String json = resultSet.getString(1);
        connection.close();
        MinesweeperGameDBO minesweeperGameDBO = this.createMinesweeperGameDBO(json, gameId);
        return minesweeperGameDBO;
    }

}
