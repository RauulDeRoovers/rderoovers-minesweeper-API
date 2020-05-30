package com.rderoovers.minesweeper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rderoovers.minesweeper.domain.GameSettings;
import com.rderoovers.minesweeper.domain.MinesweeperGameDTO;
import com.rderoovers.minesweeper.domain.MinesweeperGameUpdate;
import com.rderoovers.minesweeper.service.MinesweeperRestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.sql.SQLException;

@CrossOrigin(origins = "*")
@RestController()
public class MinesweeperRestController {

    //@Autowired
    private final MinesweeperRestService minesweeperRestService = new MinesweeperRestService();

    /*
    * Receives the amount of rows, columns and mines the game should handle.
    * Request format:
    *   {
    *       rowCount: (integer: amount of rows wanted),
    *       columnCount: (integer: amount of columns wanted),
    *       mineCount: (integer: amount of mines wanted)
    *   }
    * Returns a JSON Object with the following structure:
    *   victory: false, since the game is new, and it is just beginning
    *   finished: false, since the game is new, and it is just beginning
    *   rowCount: the amount of rows requested
    *   columnCount: the amount of columns requested
    *   id: the game's id for further reference, should be used every time the game's status changes (a cell is clicked)
    * Response Statuses:
    *  HttpStatus.OK: if game was successfully created.
    *  HttpStatus.BAD_REQUEST: invalid number of rows, columns or mines (includes checking at least 1 square is empty).
    * */
    @PostMapping("/create")
    public ResponseEntity<MinesweeperGameDTO> create(@RequestBody GameSettings gameSettings) {
        HttpStatus httpStatus;
        MinesweeperGameDTO minesweeperGameDTO = null;
        try {
            minesweeperGameDTO =  minesweeperRestService.createGame(gameSettings);
            httpStatus = HttpStatus.CREATED;
        }
        catch (IllegalArgumentException iae) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(minesweeperGameDTO, httpStatus);
    }

    /*
    * Receives the game id for the game that has suffered a change (cell clicked) and an index specifying which cell was clicked.
    * Request format:
    *   {
    *       id: (long: game's id),
    *       index: (int: selected cell's index)
    *   }
    * Returns the modified game instance with updated flags.
    * Response Statuses:
    *  HttpStatus.OK: if game was successfully updated.
    *  HttpStatus.METHOD_NOT_ALLOWED: if the game has already finished.
    *  HttpStatus.BAD_REQUEST: if id or index provided to identify either game or square are wrong.
    * */
    @PatchMapping("/update")
    public ResponseEntity<MinesweeperGameDTO> update(@RequestBody MinesweeperGameUpdate minesweeperGameUpdate) {
        HttpStatus httpStatus;
        MinesweeperGameDTO minesweeperGameDTO = null;
        try {
            minesweeperGameDTO = minesweeperRestService.updateGame(minesweeperGameUpdate);
            httpStatus = HttpStatus.OK;
        }
        catch (IllegalStateException ise) {
            httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException iae) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } catch (JsonProcessingException | SQLException | URISyntaxException e) {
            e.printStackTrace();
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(minesweeperGameDTO, httpStatus);
    }

    /*
     * Receives the game id for the game that has suffered a change (cell flagged) and an index specifying which cell was flagged.
     * Request format:
     *   {
     *       id: (long: game's id),
     *       index: (int: flagged cell's index)
     *   }
     * If cell is already revealed, nothing is changed.
     * If cell has not been revealed, it is flagged, further clicks are ignored.
     *  Cell can be unflagged (by player) or revealed if holds no mine and a neighbor with no mine was revealed.
     * Returns the modified game instance with updated flags.
     * Response Statuses:
     *  HttpStatus.OK: if square flagging was successful or no action was needed.
     *  HttpStatus.METHOD_NOT_ALLOWED: if the game has already finished.
     *  HttpStatus.BAD_REQUEST: if id or index provided to identify either game or square are wrong.
     * */
    @PatchMapping("/flag")
    public ResponseEntity<MinesweeperGameDTO> flag(@RequestBody MinesweeperGameUpdate minesweeperGameUpdate) {
        HttpStatus httpStatus;
        MinesweeperGameDTO minesweeperGameDTO = null;
        try {
            minesweeperGameDTO = minesweeperRestService.flagSquare(minesweeperGameUpdate);
            httpStatus = HttpStatus.OK;
        }
        catch (IllegalStateException ise) {
            ise.printStackTrace();
            httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException | JsonProcessingException | SQLException | URISyntaxException iae) {
            iae.printStackTrace();
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(minesweeperGameDTO, httpStatus);
    }
}