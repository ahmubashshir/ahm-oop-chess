package bd.ac.miu.cse.b60.oop.ahm.chess;

import java.time.LocalTime;

/**
 * The {@code Display} interface is responsible for displaying the current state of
 * the chessboard and the captured pieces of each player to the display device.
 *
 * <p>This class defines the interface for rendering the chess game state.
 * Different implementations can provide various user interfaces (e.g., command line, GUI).</p>
 *
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.display.CLIDisplay
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Square
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Player
 */
public interface Display {
	/**
	 * Prints the current state of the chessboard to the display device.
	 *
	 * @param board a 2D array of {@code Square} representing the chessboard.
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Square
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Piece
	 */
	void printBoard(Square[][] board);

	/**
	 * Prints the captured pieces for each player.
	 *
	 * @param players an array of {@code Player} objects representing the players in the game.
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Player
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Piece#getCaptured()
	 */
	void printCapturedPieces(Player[] players);

	/**
	 * Asks Player to enter coordinate for an action.
	 *
	 * <p>This method gets input coordinates for piece movement or selection.
	 * It can be implemented as a no-op in event-driven UI implementations.</p>
	 *
	 * @param query info about the wanted coordinate.
	 * @return a {@code Coord} object containing the column and row, or {@code null}
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Coord
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game#move
	 */
	Coord getCoord(String query);

	/**
	 * Displays the main menu and reads the user's choice.
	 *
	 * <p>This method renders the game's main menu and handles user selection.
	 * It can be implemented as a no-op in event-driven UI implementations.</p>
	 *
	 * @return the {@code MenuResult} corresponding to the user's menu selection
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.MenuResult
	 * @see bd.ac.miu.cse.b60.oop.ahm.Chess
	 */
	MenuResult mainMenu();

	/**
	 * Starts the display event loop.
	 *
	 * For CLI implementations, this can be a synchronous loop that handles user input.
	 * For GUI implementations, this should start the GUI framework's event dispatch thread.
	 */
	void run();

	/**
	 * Displays a message to the user.
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
	 * @param playerColor the color of the current player
	 * @param timeConsumed the time consumed by the current player
	 */
	void showPlayerInfo(String playerColor, LocalTime timeConsumed);

	/**
	 * Shows a move status message.
	 *
	 * @param status the status of the move
	 */
	void showMoveStatus(MoveStatus status);

	/**
	 * Shows the game end message.
	 *
	 * @param message the end game message
	 */
	void showGameEnd(String message);

	/**
	 * Shows a check warning.
	 */
	void showCheckWarning();

	/**
	 * Checks if the user has requested to exit the game during coordinate input.
	 *
	 * @return true if the user has requested to exit, false otherwise
	 */
	default boolean isExitRequested() {
		return false;
	}

	/**
	 * Resets the exit request flag.
	 */
	default void resetExitFlag() {
		// Default implementation does nothing
	}

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
	void addMenuListener(MenuListener listener);

	/**
	 * Interface for move event listeners.
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
	 * Interface for menu event listeners.
	 */
	interface MenuListener {
		/**
		 * Called when a menu selection is made by the user.
		 *
		 * @param result the selected menu option
		 */
		void onMenuSelected(MenuResult result);
	}
}
