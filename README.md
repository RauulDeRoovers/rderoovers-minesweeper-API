# rderoovers-minesweeper-API

## Design and implement a documented RESTful API for the game (think of a mobile app for your API)
### @PostMapping("/create")
Receives the amount of rows, columns and mines the game should handle.

Request format: { rowCount: (integer: amount of rows wanted), columnCount: (integer: amount of columns wanted), mineCount: (integer: amount of mines wanted) }

Returns a JSON Object with the following structure: 
 - victory: false, since the game is new, and it is just beginning
 - finished: false, since the game is new, and it is just beginning
 - rowCount: the amount of rows requested
 - columnCount: the amount of columns requested
 - id: the game's id for further reference, should be used every time the game's status changes (a cell is clicked)
 - mineCount: the amount of mines the game has.
 - playTime: the time the player has spent on this game, so far.
 - squares: an array of arrays, with objects having following structure:
	 - index: square index in the game's grid
	 - type: UNKNOWN, MINE, SAFE, FLAGGED

Response Statuses:
 - HttpStatus.OK: if game was successfully created.
 - HttpStatus.BAD_REQUEST: invalid number of rows, columns or mines (includes checking at least 1 square is empty).
 
### @PatchMapping("/update")
Receives the game id for the game that has suffered a change (cell clicked) and an index specifying which cell was clicked.

Request format: { id: (long: game's id), index: (int: selected cell's index) }

Returns the modified game instance with updated flags.

Response Statuses:

 - HttpStatus.OK: if game was successfully updated.
 - HttpStatus.METHOD_NOT_ALLOWED: if the game has already finished.
 - HttpStatus.BAD_REQUEST: if id or index provided to identify either
   game or square are wrong.

### @PatchMapping("/flag")
Receives the game id for the game that has suffered a change (cell flagged) and an index specifying which cell was flagged.

Request format: { id: (long: game's id), index: (int: flagged cell's index) }

If cell is already revealed, nothing is changed. If cell has not been revealed, it is flagged, further clicks are ignored. Cell can be unflagged (by player) or revealed if holds no mine and a neighbor with no mine was revealed.

Returns the modified game instance with updated flags.

Response Statuses:
 - HttpStatus.OK: if square flagging was successful or no action was
   needed.
 - HttpStatus.METHOD_NOT_ALLOWED: if the game has already
   finished.
 - HttpStatus.BAD_REQUEST: if id or index provided to identify
   either game or square are wrong.

### @GetMapping("/get")
Optionally, receives the game id for the game that wants to be retrieved.

Request format: { id: (long: game's id) }

If id is provided and game exists, it returns the game found as the only element in the list. If no id is provided, all existing games are retrieved.

Response Statuses:
 - HttpStatus.OK: requested game or all games where found and retrieved.
 - HttpStatus.BAD_REQUEST: requested game was not found or has a wrong format (JSON format in DB), or any of the existing games have wrong formats (JSON format in DB).
 
### @DeleteMapping("/delete")
Receives the game id for the game that wants to be deleted.

Request format: { id: (long: game's id) }

Response Statuses:
 - HttpStatus.OK: game was found and deleted.
 - HttpStatus.INTERNAL_SERVER_ERROR: an internal error happened while trying to delete the game.
 - HttpStatus.BAD_REQUEST: game was not found.

### @PostMapping("/signin")
Receives the game id for the game that wants to be deleted.

Request format: { user: (string: user's name), password: (string: user's password) }

Response Statuses:
 - HttpStatus.OK: user already exists and has been successfully signed in.
 - HttpStatus.CREATED: user does not exist but was successfully created.
 - HttpStatus.BAD_REQUEST: an internal error happened while trying to sing the user in.

Within MinesweeperRestController, the following field should be injected through Bean:
 - //@Autowired
 - private final MinesweeperRestService minesweeperRestService;

## Implement an API client library for the API designed above. Ideally, in a different language, of your preference, to the one used for the API

AngularJS API client library: minesweeper-api.js
Exposed methods:
 - createNewGame: function(rows, cols, mines);
 - clickSquare: function(game, index);
 - flagSquare: function(game, index);
 - loadGame: function(id);
 - loadAllGames: function();
 - deleteGame: function(id);
 - signIn: function(user);

## When a cell with no adjacent mines is revealed, all adjacent squares will be revealed (and repeat)
 - Endpoint: @PatchMapping("/update")
 - API client method: clickSquare: function(game, index);

## Ability to 'flag' a cell with a question mark or red flag
 - Endpoint: @PatchMapping("/flag")
 - API client method: flagSquare: function(game, index);

## Detect when game is over
 - Endpoint: @PatchMapping("/update")
 - API client method: clickSquare: function(game, index);

Within retrieved data, game's *finished* field will read true and value in *victory* indicates whether player wins.

## Persistence
Through Heroku Postgres. Database scripts can be found in the RESTful API project, under resources/database. Tables used are: GAME and USERS.
Games are stored in JSON format:

 - (long) id, 
 - (int) rowCount, 
 - (int) columnCount, 
 - (int) mineCount,
 - (boolean) finished, 
 - (boolean) victory, 
 - (long) playTime, 
 - (Array[][])   squares:
	 - (boolean) mined,
	 - (boolean) revealed,
	 - (boolean) flagged,
	 - (int) index,
	 - (int) row,
	 - (int) col,
	 - (long) minedNeighbors,
	 - (Array) neighborsPositions

Within MinesweeperRestService, the following field should be injected through Bean:

 - //@Autowired
 - private MinesweeperDAO minesweeperDAO;

Within MinesweeperDAO, there are pending enhancements to reduce the amount of repeated code when running statements against DB.
Besides, we would need to ensure connections are properly closed even in an exception is throw within the method.
Finally, we need to take database connection parameters out of the code.

## Time tracking
Timer does not start until user clicks a cell. Once a cell is clicked and response is received, timer is started (if game is not over).
Subsequently, every user click on a cell stops the timer until a response is received and the elapsed time between clicks is stored in the DB to keep track of playing game.

Enhancement is pending to check received elapsed time is not less than registered time for the game.

## Ability to start a new game and preserve/resume the old ones
User can resume existing non-finished games as well as delete existing games (either finished or not).

## Ability to select the game parameters: number of rows, columns, and mines

Through endpoint *@PostMapping("/create")* (or API client library's method *createNewGame: function(rows, cols, mines)*), user can create a new game with specified parameters.

Server checks for the validity of the parameters, such as all being > 0 and number of mines being lower than the total available squares.

## Ability to support multiple users/accounts
**Not fully implemented.**

The idea was to use the USERS table and keep it simple by signing in users even if they don't exist.

Through endpoint *@PostMapping("/signin")* (or API client library's method *signIn: function(user)*), we should be able to do so. It will only fail if the user exists but the password does not match the one in the DB.

Several security enhancements would be recommended, such as security tokens, not handling password in plain text, etc.

Finally, we would need to actually user the user's id to filter games and ensure we only show each user their own games.

# Deliverables we expect:

 - URL where the game can be accessed and played (use any platform of your preference: heroku.com, aws.amazon.com, etc): https://minesweeper-rderoovers-test.herokuapp.com/
 - Code in a public Github repo:
	 - RESTful API & API client library: https://github.com/RauulDeRoovers/rderoovers-minesweeper-API
	 - 	Client: https://github.com/RauulDeRoovers/rderoovers-minesweeper-game
 - README file with the decisions taken and important notes: 
https://github.com/RauulDeRoovers/rderoovers-minesweeper-API/blob/master/README.md
