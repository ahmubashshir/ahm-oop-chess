package bd.ac.miu.cse.b60.oop.ahm;

import bd.ac.miu.cse.b60.oop.ahm.chess.Coord;
import bd.ac.miu.cse.b60.oop.ahm.chess.Display;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.MenuResult;
import bd.ac.miu.cse.b60.oop.ahm.chess.MoveStatus;
import bd.ac.miu.cse.b60.oop.ahm.chess.Player;
import bd.ac.miu.cse.b60.oop.ahm.chess.display.CLIDisplay;
import java.time.LocalTime;

/**
 * Entry point class for the {@code Chess} game.
 * Handles the main menu, user input, and game loop.
 * All I/O operations are delegated to the Display interface.
 */
public final class Chess implements Display.MenuListener, Display.MoveListener {

	/** Default time limit for each player. */
	public static final LocalTime timeLimit = LocalTime.parse("05:00");

	private Display display;
	private Game game;
	private boolean gameInProgress = false;

	/**
	 * Constructs a new {@code Chess} instance.
	 *
	 * @param display the display implementation to use
	 */
	public Chess(Display display) {
		this.display = display;

		// Register event listeners
		display.addMenuListener(this);
		display.addMoveListener(this);
	}

	/**
	 * Entry point of the {@code Chess} game.
	 *
	 * @param args command-line arguments (not used)
	 */
	public static final void main(String[] args) {
		// Create display implementation
		Display display = new CLIDisplay();

		// Create and setup game controller
		Chess chess = new Chess(display);

		// Start display event loop
		display.run();

		// For CLI display, manually trigger the main menu
		if (display instanceof CLIDisplay) {
			MenuResult result = display.mainMenu();
			chess.onMenuSelected(result);
		}
	}

	/**
	 * Handle menu selection events from the display
	 *
	 * @param result the selected menu option
	 */
	@Override
	public void onMenuSelected(MenuResult result) {
		switch (result) {
		case START: {
			startNewGame();
			break;
		}
		case EXIT: {
			if (game != null) {
				game.end();
			}
			display.showMessage("Thank you for playing");
			System.exit(0);
			break;
		}
		default:
			display.showError("Invalid menu selection!");
			break;
		}
	}

	/**
	 * Handle move request events from the display
	 *
	 * @param source the source coordinate
	 * @param destination the destination coordinate
	 */
	@Override
	public void onMoveRequested(Coord source, Coord destination) {
		if (!gameInProgress || game == null) {
			return;
		}

		Player currentPlayer = game.getCurrentPlayer();
		String playerColor = currentPlayer.getPlayerID() == 1
		                     ? "Black"
		                     : "White";

		try {
			// Attempt to make the move
			MoveStatus moveStatus = game.move(source, destination);

			// Display the move result and update the UI
			display.showMoveStatus(moveStatus);

			if (moveStatus == MoveStatus.Ok) {
				currentPlayer.pauseTimer();
				game.setNumOfTurns(game.getCurrentPlayer(), 1);
				game.switchPlayer();

				// Update the display in sequence
				game.printCapturedPieces();
				game.printBoard();
				updatePlayerDisplay();

				// Check game state
				checkGameState();
			}
		} catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
			// Show error without using clearScreen
			display.showError("Invalid parameters!");
			display.showMessage(e.toString());
		}
	}

	/**
	 * Start a new chess game
	 */
	private void startNewGame() {
		// Start game
		// Start a new game
		game = new Game(timeLimit, display);
		game.initializePiecePositions();
		game.setMaxNumOfTurns(10);

		Player currentPlayer = game.getCurrentPlayer();

		// Initialize timers
		currentPlayer.startTimer();
		currentPlayer.pauseTimer();

		game.switchPlayer();
		currentPlayer = game.getCurrentPlayer();
		currentPlayer.startTimer();
		currentPlayer.pauseTimer();
		game.switchPlayer();

		// Set game in progress flag
		gameInProgress = true;

		// Show initial game state in a specific sequence
		game.printCapturedPieces();
		game.printBoard();
		updatePlayerDisplay();

		// Start game loop for CLI display
		if (display instanceof CLIDisplay) {
			gameLoop();
		}
	}

	/**
	 * Update the player information display
	 */
	private void updatePlayerDisplay() {
		// Show current player and time
		Player currentPlayer = game.getCurrentPlayer();
		String playerColor = currentPlayer.getPlayerID() == 1
		                     ? "Black"
		                     : "White";

		display.showPlayerInfo(playerColor, currentPlayer.getTimeConsumed());

		// Continue timer for current player
		currentPlayer.continueTimer();
	}

	/**
	 * Handles the game loop for CLI display
	 */
	private void gameLoop() {
		while (gameInProgress) {
			Player currentPlayer = game.getCurrentPlayer();

			// Check game state first
			if (checkGameState()) {
				break; // Game is over
			}

			// Get moves from the player
			Coord src, dst;
			try {
				// Display game state before asking for move
				game.printCapturedPieces();
				game.printBoard();
				updatePlayerDisplay();

				src = display.getCoord("of piece to move");
				if (src == null) {
					// Check if user requested to exit (entered 'q')
					if (display.isExitRequested()) {
						display.showMessage("Returning to main menu...");
						game.end();
						gameInProgress = false;
						display.resetExitFlag();
						display.mainMenu();
						break;
					} else {
						display.showGameEnd("Game ended forcefully.");
						game.end();
						gameInProgress = false;
						break;
					}
				}

				dst = display.getCoord("of the square to move the piece to");
				if (dst == null) {
					// Check if user requested to exit (entered 'q')
					if (display.isExitRequested()) {
						display.showMessage("Returning to main menu...");
						game.end();
						gameInProgress = false;
						display.resetExitFlag();
						display.mainMenu();
						break;
					} else {
						display.showGameEnd("Game ended forcefully.");
						game.end();
						gameInProgress = false;
						break;
					}
				}

				// Process the move directly for CLI
				onMoveRequested(src, dst);
			} catch (
				    IllegalArgumentException
				    | StringIndexOutOfBoundsException e
				) {
				// Show error without using clearScreen
				display.showError("Invalid parameters!");
				display.showMessage(e.toString());
			}
		}
	}

	/**
	 * Check if the game is over and handle end game conditions
	 *
	 * @return true if the game is over, false otherwise
	 */
	private boolean checkGameState() {
		if (!gameInProgress) return true;

		Player currentPlayer = game.getCurrentPlayer();
		String playerColor = currentPlayer.getPlayerID() == 1
		                     ? "Black"
		                     : "White";

		if (game.isDraw()) {
			display.showGameEnd(
			    "Both players have run out of turns, game ends in a draw"
			);
			game.end();
			gameInProgress = false;
			return true;
		} else if (game.isTimeFinished()) {
			display.showGameEnd(
			    playerColor + " loses as they ran out of time."
			);
			game.end();
			gameInProgress = false;
			return true;
		} else if (!game.isKingAlive()) {
			display.showGameEnd(
			    playerColor + " King is dead, they lose the game"
			);
			game.end();
			gameInProgress = false;
			return true;
		} else if (game.isCheck()) {
			display.showCheckWarning();
		}

		return false;
	}
}
