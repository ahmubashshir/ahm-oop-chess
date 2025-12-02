package bd.ac.miu.cse.b60.oop.ahm;

import bd.ac.miu.cse.b60.oop.ahm.chess.Coord;
import bd.ac.miu.cse.b60.oop.ahm.chess.Display;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.MoveStatus;
import bd.ac.miu.cse.b60.oop.ahm.chess.Player;
import bd.ac.miu.cse.b60.oop.ahm.chess.State;
import bd.ac.miu.cse.b60.oop.ahm.chess.display.CLIDisplay;
import java.time.LocalTime;

/**
 * Entry point class for the {@code Chess} game.
 * Handles the main menu, user input, and game loop.
 * All I/O operations are delegated to the Display interface.
 */
public final class Chess implements Display.StateListener, Display.MoveListener {

	/** Default time limit for each player. */
	public static final LocalTime timeLimit = LocalTime.parse("05:00");

	private Display display;
	private Game game;

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

	public void run() {
		display.run();
	}

	private void endGame() {
		if (game == null) return;
		game.end();
		game = null;
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
		chess.run();
	}

	/**
	 * Handle game state changes from the display
	 *
	 * @param state current game state
	 */
	@Override
	public void onStateChange(State state) {
		switch (state) {
		case START: {
			newGame();
			break;
		}
		case END: {
			endGame();
			break;
		}
		case EXIT: {
			if (game != null) {
				display.showMessage("End current game first.");
			}
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
		if (game == null) {
			return;
		}

		Player currentPlayer = game.getCurrentPlayer();
		try {
			// Attempt to make the move
			MoveStatus moveStatus = game.move(source, destination);

			// Display the move result
			display.showMoveStatus(moveStatus);

			if (moveStatus == MoveStatus.Ok) {
				currentPlayer.pauseTimer();
				game.setNumOfTurns(game.getCurrentPlayer(), 1);
				game.switchPlayer();
				updateGameState();
			}
		} catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
			display.showError(e.getMessage());
		}
	}

	/**
	 * Start a new chess game
	 */
	private void newGame() {
		// Start game
		// Start a new game
		game = new Game(timeLimit);
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
		updateGameState();
		// The game loop is now handled by the display implementation
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
	 * Updates the display with the current game state.
	 * This should be called whenever the game state changes.
	 */
	public void updateGameState() {
		if (game == null || isGameOver()) {
			return;
		}

		// Display current game state
		display.updateCapturedPieces(game);
		display.updateBoard(game);
		updatePlayerDisplay();
	}

	/**
	 * Check if the game is over and handle end game conditions
	 *
	 * @return true if the game is over, false otherwise
	 */
	private boolean isGameOver() {
		if (game == null) return true;

		Player currentPlayer = game.getCurrentPlayer();
		String playerColor = currentPlayer.getPlayerID() == 1
		                     ? "Black"
		                     : "White";

		if (game.isDraw()) {
			// Screen management is handled by the display implementation
			display.showGameEnd(
			    "Both players have run out of turns, game ends in a draw"
			);
			endGame();
			return true;
		} else if (game.isTimeFinished()) {
			// Screen management is handled by the display implementation
			display.showGameEnd(
			    playerColor + " loses as they ran out of time."
			);
			endGame();
			return true;
		} else if (!game.isKingAlive()) {
			// Screen management is handled by the display implementation
			display.showGameEnd(
			    playerColor + " King is dead, they lose the game"
			);
			endGame();
			return true;
		} else if (game.isCheck()) {
			display.showCheckWarning();
		}

		return false;
	}
}
