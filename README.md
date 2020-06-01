#rderoovers-minesweeper-API

## Design and implement a documented RESTful API for the game (think of a mobile app for your API)
* @PostMapping("/create")
Receives the amount of rows, columns and mines the game should handle.
 Request format:
   {
       rowCount: (integer: amount of rows wanted),
       columnCount: (integer: amount of columns wanted),
       mineCount: (integer: amount of mines wanted)
   }
 Returns a JSON Object with the following structure:
   victory: false, since the game is new, and it is just beginning
   finished: false, since the game is new, and it is just beginning
   rowCount: the amount of rows requested
   columnCount: the amount of columns requested
   id: the game's id for further reference, should be used every time the game's status changes (a cell is clicked)
	mineCount: the amount of mines the game has.
	playTime: the time the player has spent on this game, so far.
   squares: an array of arrays, with objects having following structure:
		index: square index in the game's grid
		type: UNKNOWN, MINE, SAFE, FLAGGED
 Response Statuses:
  HttpStatus.OK: if game was successfully created.
  HttpStatus.BAD_REQUEST: invalid number of rows, columns or mines (includes checking at least 1 square is empty).
* @PatchMapping("/update")
Receives the game id for the game that has suffered a change (cell clicked) and an index specifying which cell was clicked.
Request format:
  {
       id: (long: game's id),
       index: (int: selected cell's index)
  }
Returns the modified game instance with updated flags.
Response Statuses:
HttpStatus.OK: if game was successfully updated.
HttpStatus.METHOD_NOT_ALLOWED: if the game has already finished.
HttpStatus.BAD_REQUEST: if id or index provided to identify either game or square are wrong.


@PatchMapping("/flag")
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

@GetMapping("/get")
/*
 * Optionally, receives the game id for the game that wants to be retrieved.
 * Request format:
 * 	 {
 *   	id: (long: game's id)
 *   }
 * If id is provided and game exists, it returns the game found as the only element in the list. If no id is provided, all existing games are retrieved.
 * Response Statuses:
 *  HttpStatus.OK: requested game or all games where found and retrieved.
 *  HttpStatus.BAD_REQUEST: requested game was not found or has a wrong format (JSON format in DB), or any of the existing games have wrong formats (JSON format in DB).
 * */

@DeleteMapping("/delete")
/*
 * Receives the game id for the game that wants to be deleted.
 * Request format:
 *   {
 *       id: (long: game's id)
 *   }
 * Response Statuses:
 *  HttpStatus.OK: game was found and deleted.
 *  HttpStatus.INTERNAL_SERVER_ERROR: an internal error happened while trying to delete the game.
 *  HttpStatus.BAD_REQUEST: game was not found.
 * */

/*
* OK means user already exists
* CREATE means user was created
* */
@PostMapping("/signin")
/*
 * Receives the game id for the game that wants to be deleted.
 * Request format:
 *   {
 *      user: (string: user's name),
 * 		password: (string: user's password)
 *   }
 * Response Statuses:
 *  HttpStatus.OK: user already exists and has been successfully signed in.
 *  HttpStatus.CREATED: user does not exist but was successfully created.
 *  HttpStatus.BAD_REQUEST: an internal error happened while trying to sing the user in.
 * */
 
 Within MinesweeperRestController, the following field should be injected through Bean:
	//@Autowired
	private final MinesweeperRestService minesweeperRestService;

We ask that you complete the following challenge to evaluate your development skills. Please use the programming language and framework discussed during your interview to accomplish the following task.

PLEASE DO NOT FORK THE REPOSITORY. WE NEED A PUBLIC REPOSITORY FOR THE REVIEW. 

## The Game
Develop the classic game of [Minesweeper](https://en.wikipedia.org/wiki/Minesweeper_(video_game))

## Show your work

1.  Create a Public repository ( please dont make a pull request, clone the private repository and create a new plublic one on your profile)
2.  Commit each step of your process so we can follow your thought process.

## What to build
The following is a list of items (prioritized from most important to least important) we wish to see:
* Design and implement  a documented RESTful API for the game (think of a mobile app for your API)
* Implement an API client library for the API designed above. Ideally, in a different language, of your preference, to the one used for the API
* When a cell with no adjacent mines is revealed, all adjacent squares will be revealed (and repeat)
* Ability to 'flag' a cell with a question mark or red flag
* Detect when game is over
* Persistence
* Time tracking
* Ability to start a new game and preserve/resume the old ones
* Ability to select the game parameters: number of rows, columns, and mines
* Ability to support multiple users/accounts
 
## Deliverables we expect:
* URL where the game can be accessed and played (use any platform of your preference: heroku.com, aws.amazon.com, etc)
* Code in a public Github repo
* README file with the decisions taken and important notes

## Time Spent
You need to fully complete the challenge. We suggest not to spend more than 5 days total.  Please make commits as often as possible so we can see the time you spent and please do not make one commit.  We will evaluate the code and time spent.
 
What we want to see is how well you handle yourself given the time you spend on the problem, how you think, and how you prioritize when time is sufficient to solve everything.

Please email your solution as soon as you have completed the challenge or the time is up.
