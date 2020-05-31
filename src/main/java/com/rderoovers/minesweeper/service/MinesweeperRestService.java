package com.rderoovers.minesweeper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rderoovers.minesweeper.database.MinesweeperDAO;
import com.rderoovers.minesweeper.domain.*;
import javafx.util.Pair;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MinesweeperRestService {

    //@Autowired
    private MinesweeperDAO minesweeperDAO = new MinesweeperDAO();

    private List<Integer> calculateNeighborsPositions(MinesweeperSquare square, MinesweeperGameSettings gameSettings) {
        int colCount = gameSettings.getColumnCount();
        List<Pair<Integer, Integer>> neighborsPositions = new ArrayList<>();
        int row = square.getRow();
        int col = square.getCol();

        boolean hasLeftNeighbor = square.getCol() > 0;
        boolean hasTopNeighbor = square.getRow() > 0;
        boolean hasRightNeighbor = square.getCol() < colCount - 1;
        boolean hasBottomNeighbor = square.getRow() < gameSettings.getRowCount() - 1;

        if (hasLeftNeighbor) neighborsPositions.add(new Pair<>(row, col - 1));
        if (hasLeftNeighbor && hasTopNeighbor) neighborsPositions.add(new Pair<>(row - 1, col - 1));
        if (hasTopNeighbor) neighborsPositions.add(new Pair<>(row - 1, col));
        if (hasTopNeighbor && hasRightNeighbor) neighborsPositions.add(new Pair<>(row - 1, col + 1));
        if (hasRightNeighbor) neighborsPositions.add(new Pair<>(row, col + 1));
        if (hasRightNeighbor && hasBottomNeighbor) neighborsPositions.add(new Pair<>(row + 1, col + 1));
        if (hasBottomNeighbor) neighborsPositions.add(new Pair<>(row + 1, col));
        if (hasBottomNeighbor && hasLeftNeighbor) neighborsPositions.add(new Pair<>(row + 1, col - 1));

        return neighborsPositions.stream().map(x -> (x.getKey() * colCount) + x.getValue()).collect(Collectors.toList());
    }

    private MinesweeperGameDTO createMinesweeperGameDTO(MinesweeperGameDBO minesweeperGameDBO) {
        int rowCount = minesweeperGameDBO.getRowCount();
        int colCount = minesweeperGameDBO.getColumnCount();
        MinesweeperSquareDTO[][] squares = new MinesweeperSquareDTO[rowCount][colCount];
        for (int row = 0; row < rowCount; row++) {
            MinesweeperSquare[] rowSquares = minesweeperGameDBO.getSquares()[row];
            for (int col = 0; col < colCount; col++) {
                MinesweeperSquare square = rowSquares[col];
                long index = square.getIndex();
                squares[row][col] = square.isFlagged() ?
                        new MinesweeperSquareFlagged(index) : !square.isRevealed() ?
                        new MinesweeperSquareUnknown(index) : square.isMined() ?
                        new MinesweeperSquareMine(index) :
                        new MinesweeperSquareSafe(index, square.getMinedNeighbors());
            }
        }
        return new MinesweeperGameDTO(minesweeperGameDBO.getId(), rowCount, colCount, minesweeperGameDBO.getMineCount(), minesweeperGameDBO.getPlayTime(), minesweeperGameDBO.isFinished(), minesweeperGameDBO.isVictory(), squares);
    }

    private void createMines(MinesweeperSquare[][] squares, int rowCount, int columnCount, int mineCount) {
        int minesSet = 0;
        while (minesSet < mineCount) {
            int randomInteger = (int) (new Random().nextDouble() * rowCount);
            int randomInteger2 = (int) (new Random().nextDouble() * columnCount);
            if (squares[randomInteger][randomInteger2].isMined()) {
                continue;
            }

            squares[randomInteger][randomInteger2].mine();
            minesSet++;
        }
    }

    private MinesweeperSquare[][] createSquares(MinesweeperGameSettings gameSettings) {
        MinesweeperSquare[][] squares = new MinesweeperSquare[gameSettings.getRowCount()][gameSettings.getColumnCount()];
        int index = 0;
        for (int row = 0; row < gameSettings.getRowCount(); row++) {
            for (int col = 0; col < gameSettings.getColumnCount(); col++) {
                MinesweeperSquare square = new MinesweeperSquare(index, row, col, false);
                squares[row][col] = square;
                List<Integer> neighborsPositions = calculateNeighborsPositions(square, gameSettings);
                square.setNeighborsPositions(neighborsPositions);
                index++;
            }
        }
        return squares;
    }

    public MinesweeperGameDTO createGame(MinesweeperGameSettings gameSettings) throws IllegalArgumentException {
        if (gameSettings.getRowCount() < 0) throw new IllegalArgumentException("Invalid number of rows.");
        if (gameSettings.getColumnCount() < 0) throw new IllegalArgumentException("Invalid number of columns.");
        if (gameSettings.getMineCount() < 1) throw new IllegalArgumentException("Invalid number of mines.");
        if (!((gameSettings.getRowCount() * gameSettings.getColumnCount()) > gameSettings.getMineCount())) throw new IllegalArgumentException("Too many mines.");

        MinesweeperSquare[][] squares = createSquares(gameSettings);
        createMines(squares, gameSettings.getRowCount(), gameSettings.getColumnCount(), gameSettings.getMineCount());

        MinesweeperGameDBO minesweeperGameDBO = new MinesweeperGameDBO();
        minesweeperGameDBO.setRowCount(gameSettings.getRowCount());
        minesweeperGameDBO.setColumnCount(gameSettings.getColumnCount());
        minesweeperGameDBO.setMineCount(gameSettings.getMineCount());
        minesweeperGameDBO.setSquares(squares);
        try {
            minesweeperDAO.saveGame(minesweeperGameDBO);
        } catch (URISyntaxException | SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return this.createMinesweeperGameDTO(minesweeperGameDBO);
    }

    public MinesweeperGameDTO updateGame(MinesweeperGameUpdate minesweeperGameUpdate) throws IllegalArgumentException, JsonProcessingException, SQLException, URISyntaxException {
        long gameId = minesweeperGameUpdate.getId();
        MinesweeperGameDBO minesweeperGameDBO;
        try {
            minesweeperGameDBO = minesweeperDAO.loadGame(gameId);
        }
        // TODO: ensure we catch the proper exception
        catch (Exception e) {
            throw new IllegalArgumentException();
        }
        if (minesweeperGameDBO.isFinished()) {
            throw new IllegalStateException("Game is finished, no further operations are allowed.");
        }
        minesweeperGameDBO.setPlayTime(minesweeperGameUpdate.getTime());
        int index = minesweeperGameUpdate.getSquare();
        MinesweeperGame minesweeperGame = new MinesweeperGame(minesweeperGameDBO);
        minesweeperGame.selectSquare(index);
        minesweeperDAO.saveGame(minesweeperGame);
        return this.createMinesweeperGameDTO(minesweeperGame);
    }

    public MinesweeperGameDTO flagSquare(MinesweeperGameUpdate minesweeperGameUpdate) throws JsonProcessingException, SQLException, URISyntaxException {
        long gameId = minesweeperGameUpdate.getId();
        MinesweeperGameDBO minesweeperGameDBO;
        try {
            minesweeperGameDBO = minesweeperDAO.loadGame(gameId);
        }
        // TODO: ensure we catch the proper exception
        catch (Exception e) {
            throw new IllegalArgumentException();
        }
        if (minesweeperGameDBO.isFinished()) {
            throw new IllegalStateException("Game is finished, no further operations are allowed.");
        }
        int index = minesweeperGameUpdate.getSquare();
        minesweeperGameDBO.setPlayTime(minesweeperGameUpdate.getTime());
        MinesweeperGame minesweeperGame = new MinesweeperGame(minesweeperGameDBO);
        minesweeperGame.flagSquare(index);
        minesweeperDAO.saveGame(minesweeperGame);
        return this.createMinesweeperGameDTO(minesweeperGame);
    }

    public List<MinesweeperGameDTO> loadAllGames() throws SQLException, JsonProcessingException {
        return minesweeperDAO.loadAllGames().stream().map(this::createMinesweeperGameDTO).collect(Collectors.toList());
    }

    public MinesweeperGameDTO loadGame(Long id) throws SQLException, JsonProcessingException {
        return this.createMinesweeperGameDTO(minesweeperDAO.loadGame(id));
    }

    public boolean deleteGame(Long id) throws SQLException {
        return minesweeperDAO.deleteGame(id);
    }

    public Boolean signIn(MinesweeperGameLogin minesweeperGameLogin) throws SQLException {
        if (minesweeperGameLogin.getUser().length() < 4) throw new IllegalArgumentException("Invalid user name.");
        if (minesweeperGameLogin.getPassword().length() < 4) throw new IllegalArgumentException("Invalid user password.");

        MinesweeperGameLogin dbUser = minesweeperDAO.getUser(minesweeperGameLogin);
        if (dbUser.getId() == 0) {
            // user does not exist
            minesweeperDAO.createUser(minesweeperGameLogin);
            return false;
        }
        else {
            // user exists, check password
            if (!dbUser.getPassword().equals(minesweeperGameLogin.getPassword())) {
                throw new SecurityException();
            }
            minesweeperGameLogin.setId(dbUser.getId());
            return true;
        }
    }
}
