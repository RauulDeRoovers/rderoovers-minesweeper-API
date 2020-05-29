package com.rderoovers.minesweeper.service;

import com.rderoovers.minesweeper.domain.*;
import javafx.util.Pair;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class MinesweeperRestService {

    private final AtomicLong counter = new AtomicLong();
    private final Map<Long, MinesweeperGame> games = new HashMap<>();

    // TODO: use indexes instead of Pair
    // 3x3:
    // 7 => row ? 7 / 3 = 2 | col ? 7 - (3 * 2) = 1 => (2,1) where only col count is used
    // 3x4:
    // 3 => row ? 3 / 4 = 0 | col ? 3 - (4 * 0) = 3 => (0,3)
    // 10 => row ? 10 / 4 = 2 | col ? 10 - (4 * 2) = 2 => (2,2)
    // 4x2:
    // 5 => row ? 5 / 2 = 2 | col ? 5 - (2 * 2) = 1 => (2,1)
    private List<Pair<Integer, Integer>> calculateNeighborsPositions(MinesweeperSquare square, GameSettings gameSettings) {
        List<Pair<Integer, Integer>> neighborsPositions = new ArrayList<>();
        int row = square.getRow();
        int col = square.getCol();

        boolean hasLeftNeighbor = square.getCol() > 0;
        boolean hasTopNeighbor = square.getRow() > 0;
        boolean hasRightNeighbor = square.getCol() < gameSettings.getColumnCount() - 1;
        boolean hasBottomNeighbor = square.getRow() < gameSettings.getRowCount() - 1;

        if (hasLeftNeighbor) neighborsPositions.add(new Pair<>(row, col - 1));
        if (hasLeftNeighbor && hasTopNeighbor) neighborsPositions.add(new Pair<>(row - 1, col - 1));
        if (hasTopNeighbor) neighborsPositions.add(new Pair<>(row - 1, col));
        if (hasTopNeighbor && hasRightNeighbor) neighborsPositions.add(new Pair<>(row - 1, col + 1));
        if (hasRightNeighbor) neighborsPositions.add(new Pair<>(row, col + 1));
        if (hasRightNeighbor && hasBottomNeighbor) neighborsPositions.add(new Pair<>(row + 1, col + 1));
        if (hasBottomNeighbor) neighborsPositions.add(new Pair<>(row + 1, col));
        if (hasBottomNeighbor && hasLeftNeighbor) neighborsPositions.add(new Pair<>(row + 1, col - 1));

        return neighborsPositions;
    }

    private MinesweeperGameDTO createMinesweeperGameDTO(MinesweeperGame minesweeperGame) {
        int rowCount = minesweeperGame.getRowCount();
        int colCount = minesweeperGame.getColumnCount();
        MinesweeperSquareDTO[][] squares = new MinesweeperSquareDTO[rowCount][colCount];
        for (int row = 0; row < rowCount; row++) {
            MinesweeperSquare[] rowSquares = minesweeperGame.getSquares()[row];
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
        return new MinesweeperGameDTO(minesweeperGame.getId(), rowCount, colCount, minesweeperGame.isFinished(), minesweeperGame.isVictory(), squares);
    }

    public MinesweeperGameDTO createGame(GameSettings gameSettings) throws IllegalArgumentException {
        if (gameSettings.getRowCount() < 0) throw new IllegalArgumentException("Invalid number of rows.");
        if (gameSettings.getColumnCount() < 0) throw new IllegalArgumentException("Invalid number of columns.");
        if (gameSettings.getMineCount() < 1) throw new IllegalArgumentException("Invalid number of mines.");
        if (!((gameSettings.getRowCount() * gameSettings.getColumnCount()) > gameSettings.getMineCount())) throw new IllegalArgumentException("Too many mines.");

        MinesweeperSquare[][] squares = new MinesweeperSquare[gameSettings.getRowCount()][gameSettings.getColumnCount()];
        int index = 0;
        for (int row = 0; row < gameSettings.getRowCount(); row++) {
            for (int col = 0; col < gameSettings.getColumnCount(); col++) {
                MinesweeperSquare square = new MinesweeperSquare(index, row, col, false);
                squares[row][col] = square;
                List<Pair<Integer, Integer>> neighborsIndexes = calculateNeighborsPositions(square, gameSettings);
                square.setNeighborsPositions(neighborsIndexes);
                index++;
            }
        }
        int minesSet = 0;
        while (minesSet < gameSettings.getMineCount()) {
            int randomInteger = (int) (new Random().nextDouble() * gameSettings.getRowCount());
            int randomInteger2 = (int) (new Random().nextDouble() * gameSettings.getColumnCount());
            if (squares[randomInteger][randomInteger2].isMined()) {
                continue;
            }

            squares[randomInteger][randomInteger2].mine();
            minesSet++;
        }

        long id = counter.incrementAndGet();
        MinesweeperGame minesweeperGame = new MinesweeperGame(id, gameSettings.getRowCount(), gameSettings.getColumnCount(), gameSettings.getMineCount(), squares);
        games.put(id, minesweeperGame);
        return this.createMinesweeperGameDTO(minesweeperGame);
    }

    public MinesweeperGameDTO updateGame(MinesweeperGameUpdate minesweeperGameUpdate) throws IllegalArgumentException {
        long gameId = minesweeperGameUpdate.getId();
        if (!games.containsKey(gameId)) {
            throw new IllegalArgumentException();
        }
        MinesweeperGame minesweeperGame = games.get(gameId);
        if (minesweeperGame.isFinished()) {
            throw new IllegalStateException("Game is finished, no further operations are allowed.");
        }
        int index = minesweeperGameUpdate.getSquare();
        minesweeperGame.selectSquare(index);
        return this.createMinesweeperGameDTO(minesweeperGame);
    }

    public MinesweeperGameDTO flagSquare(MinesweeperGameUpdate minesweeperGameUpdate) {
        long gameId = minesweeperGameUpdate.getId();
        if (!games.containsKey(gameId)) {
            throw new IllegalArgumentException();
        }
        MinesweeperGame minesweeperGame = games.get(gameId);
        if (minesweeperGame.isFinished()) {
            throw new IllegalStateException("Game is finished, no further operations are allowed.");
        }
        int index = minesweeperGameUpdate.getSquare();
        minesweeperGame.flagSquare(index);
        return this.createMinesweeperGameDTO(minesweeperGame);
    }
}
