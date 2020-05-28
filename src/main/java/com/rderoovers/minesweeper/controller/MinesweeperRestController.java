package com.rderoovers.minesweeper.controller;

import com.rderoovers.minesweeper.domain.*;
import com.rderoovers.minesweeper.service.MinesweeperRestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    * */
    // TODO: param validations and error documentation
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
    * */
    // TODO: param validations and error documentation
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
        catch (IndexOutOfBoundsException iobe) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(minesweeperGameDTO, httpStatus);
    }
}