package bd.ac.miu.cse.b60.oop.ahm.chess;

import java.time.LocalTime;

/**
 * Interface for displaying the state of a chess game and interacting with the user.
 * <p>
 * Implementations of this interface are responsible for rendering the chessboard,
 * showing captured pieces, handling user input, and displaying messages or errors.
 * This abstraction allows for different user interfaces (e.g., CLI, GUI) to be plugged into the game logic.
 * </p>
 *
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.display.CLIDisplay
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Square
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Player
 */
public interface Display {
	/**
	 * Updates the display to show the current state of the chessboard.
	 *
	 * @param game the current game state to be rendered
	 */
	void updateBoard(final Game game);

	/**
	 * Updates the display to show the captured pieces for each player.
	 *
	 * @param game the current game state containing captured pieces
	 */
	void updateCapturedPieces(final Game game);

	/**
	 * Prompts the user to enter a coordinate for an action (e.g., move or selection).
	 * <p>
	 * Implementations may ignore this in event-driven UIs.
	 * </p>
	 *
	 * @param query a message describing the requested coordinate
	 * @return a {@code Coord} object containing the column and row, or {@code null} if cancelled
	 */
	Coord getCoord(String query);

	/**
	 * Displays the main menu and handles user selection.
	 * <p>
	 * Implementations may ignore this in event-driven UIs.
	 * </p>
	 */
	void showMainMenu();

	/**
	 * Starts the display event loop.
	 * <p>
	 * For CLI implementations, this may be a synchronous loop that handles user input.
	 * For GUI implementations, this should start the GUI framework's event dispatch thread.
	 * </p>
	 */
	void run();

	/**
	 * Displays a general message to the user.
	 *
	 * @param message the message to display
	 */
	void showMessage(String message);

	/**
	 * Displays an error message to the user.
	 *
	 * @param message the error message to display
	 */
	void showError(String message);

	/**
	 * Updates the display with the current player's information.
	 *
	 * @param playerColor the color of the current player (e.g., "White" or "Black")
	 * @param timeConsumed the time consumed by the current player
	 */
	void showPlayerInfo(String playerColor, LocalTime timeConsumed);

	/**
	 * Displays a move status message to the user.
	 *
	 * @param status the status of the move
	 */
	void showMoveStatus(MoveStatus status);

	/**
	 * Displays a message indicating the game has ended.
	 *
	 * @param message the end game message
	 */
	void showGameEnd(String message);

	/**
	 * Displays a warning that the current player's king is in check.
	 */
	void showCheckWarning();

	/**
	 * Registers a listener for piece move events.
	 *
	 * @param listener the listener to be notified when a piece move is requested
	 */
	void addMoveListener(MoveListener listener);

	/**
	 * Registers a listener for menu selection events.
	 *
	 * @param listener the listener to be notified when a menu item is selected
	 */
	void addMenuListener(StateListener listener);

	/**
	 * Listener interface for piece move events.
	 */
	interface MoveListener {
		/**
		 * Called when a move is requested by the user.
		 *
		 * @param source the source coordinate
		 * @param destination the destination coordinate
		 */
		void onMoveRequested(Coord source, Coord destination);
	}

	/**
	 * Listener interface for menu selection events.
	 */
	interface StateListener {
		/**
		 * Called when a menu selection is made by the user.
		 *
		 * @param result the selected menu option
		 */
		void onStateChange(State result);
	}
}
