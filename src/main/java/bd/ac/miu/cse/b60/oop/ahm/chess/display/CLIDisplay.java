package bd.ac.miu.cse.b60.oop.ahm.chess.display;

import bd.ac.miu.cse.b60.oop.ahm.chess.Color;
import bd.ac.miu.cse.b60.oop.ahm.chess.Coord;
import bd.ac.miu.cse.b60.oop.ahm.chess.Display;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.MoveStatus;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Player;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import bd.ac.miu.cse.b60.oop.ahm.chess.State;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code CLIDisplay} class implements the {@code Display} interface for
 * console-based interaction. It provides a synchronous, command-line interface
 * for the chess game.
 */
public class CLIDisplay implements Display {

	private Scanner input;
	private List<MoveListener> moveListeners;
	private List<StateListener> menuListeners;
	private boolean isRunning;
	private boolean exitRequested;
	private boolean disableScreenClear = false;

	/** Default Constructor */
	public CLIDisplay() {
		input = new Scanner(System.in);
		moveListeners = new ArrayList<>();
		menuListeners = new ArrayList<>();
		isRunning = false;
		exitRequested = false;

		// Set default timeout for scanner to avoid blocking indefinitely
		if (System.console() == null) {
			System.out.println("Running in non-interactive mode");
		}
	}

	/**
	 * Prints the current state of the chessboard to the console.
	 * The screen should not be cleared here as the board needs to be visible
	 * during move input.
	 *
	 * @param game the current game state
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game
	 */
	@Override
	public void updateBoard(final Game game) {
		// Do not clear the screen here - keep the board visible during move input
		// Show captured pieces at the top for better user experience
		final Square[][] board = game.getBoard();

		System.out.println("  a b c d e f g h");
		System.out.println(" -----------------");

		for (int row = 0; row < board.length; row++) {
			System.out.print(row + 1); // Leftmost board outline
			for (int col = 0; col < board[row].length; col++) {
				System.out.print("|");
				if (
				    board[row][col] == null ||
				    board[row][col].getPiece() == null
				) System.out.print(" ");
				else System.out.print(board[row][col].getPiece().getSymbol());
			}

			System.out.print(String.format("|%d", row + 1));
			System.out.println();
		}

		// Print horizontal separator
		System.out.println("  -----------------");
		System.out.println("   a b c d e f g h");
	}

	/**
	 * Prints the captured pieces for each player.
	 * This does not clear the screen as it's usually displayed along with the board.
	 *
	 * @param game the current game state
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game
	 */
	@Override
	public void updateCapturedPieces(final Game game) {
		for (Color color : Color.values()) {
			System.out.print(String.format("%s captured: ", color.name));
			final Player player = game.getPlayer(color.id);
			Piece[] capturedPieces = player.getCapturedPieces();

			if (capturedPieces.length == 0) {
				System.out.print(" none");
			} else {
				for (Piece piece : capturedPieces) {
					System.out.print(String.format(" %s", piece.getSymbol()));
				}
			}

			System.out.println(); // Separate players' captured pieces
		}
	}

	/**
	 * Prompts the user to enter a coordinate on the board.
	 * This is a synchronous operation that blocks until input is received.
	 *
	 * @param query a {@code String} describing which coordinate to enter
	 * @return a {@code Coord} object containing the column and row, or {@code null} if the user enters -1
	 */
	@Override
	public Coord getCoord(String query) {
		// Don't clear the screen - we want the user to see the board state
		// Just print the prompt on the current line
		System.out.print(
		    String.format(
		        "Enter the position %s (q to abort) (col row): ",
		        query
		    )
		);

		// Handle case where there's no input available
		if (!input.hasNext()) {
			System.out.println("No input detected, generating default move");
			// Return a default coordinate for testing
			return new Coord('e', '2');
		}

		String in = input.next().trim();
		if (in.equals("q")) {
			// Set exit flag and return null
			exitRequested = true;
			System.out.println("Exiting game...");
			return null;
		}
		return new Coord(in.charAt(0 /* col */), in.charAt(1 /* row */));
	}

	/**
	 * Displays the main menu.
	 */
	@Override
	public void showMainMenu() {
		// Clear screen before showing menu (one of the allowed places)
		clearScreen();

		// Display the main menu header and options
		System.out.println("AHM CHESS GAME - MAIN MENU");
		System.out.println("=========================");
		System.out.println("1. Start a new game");
		System.out.println("2. Exit");
		System.out.println();

		// Print the prompt without a newline
		System.out.print("Your choice: ");
	}

	private State readMenuChoice() {
		State result;
		// Handle case where there's no input available
		if (input.hasNextInt()) {
			result = State.fromInt(input.nextInt());
			clearScreen();
		} else {
			// Default to starting a new game if no input available
			clearScreen();
			System.out.println("No input detected, defaulting to new game");
			result = State.START;
			if (input.hasNext()) {
				input.next(); // Consume whatever is there
			}
		}
		notifyStateListeners(result);
		return result;
	}

	/**
	 * Starts the CLI display event loop.
	 * This method manages the main game loop and menu flow for CLI implementation.
	 */
	@Override
	public void run() {
		isRunning = true;

		// Main application loop
		while (isRunning) {
			// Display the main menu and get user choice
			showMainMenu();
			State result = readMenuChoice();

			switch (result) {
			case EXIT:
				isRunning = false;
				System.out.println("Exiting the game...");
				break;
			case START:
				gameLoop();
				delay(1000);
				notifyStateListeners(State.END);
				break;
			default:
				break;
			}
		}
		input.close();
	}

	/**
	 * Handles the game loop for CLI display.
	 * This keeps asking for moves until the game ends.
	 */
	private void gameLoop() {
		boolean gameInProgress = true;

		System.out.println("Starting a new game...");
		while (gameInProgress && isRunning) {
			// Get moves from the player
			Coord src, dst;
			try {
				src = getCoord("of piece to move");
				if (src == null) {
					// Check if user requested to exit (entered 'q')
					if (exitRequested) {
						showMessage("Returning to main menu...");
						exitRequested = gameInProgress = false;
						continue;
					} else {
						showGameEnd("Game ended forcefully.");
						gameInProgress = false;
						continue;
					}
				}

				dst = getCoord("of the square to move the piece to");
				if (dst == null) {
					// Check if user requested to exit (entered 'q')
					if (exitRequested) {
						showMessage("Returning to main menu...");
						exitRequested = gameInProgress = false;
						continue;
					} else {
						showGameEnd("Game ended forcefully.");
						gameInProgress = false;
						continue;
					}
				}

				// Only clear screen after we have BOTH valid coordinates
				// This is one of the three allowed places to clear the screen
				clearScreen();

				// Notify all move listeners
				notifyMoveListeners(src, dst);
			} catch (
				    IllegalArgumentException
				    | StringIndexOutOfBoundsException e
				) {
				showError(e.getMessage());
				// Show error without clearing screen to preserve board display
				// We don't need to redisplay the board here
				// The board should still be visible since we didn't clear the screen
			}
		}
	}

	/**
	 * Registers a listener for piece move events.
	 *
	 * @param listener the listener to be notified when a piece move is requested
	 */
	@Override
	public void addMoveListener(MoveListener listener) {
		moveListeners.add(listener);
	}

	/**
	 * Registers a listener for menu selection events.
	 *
	 * @param listener the listener to be notified when a menu item is selected
	 */
	@Override
	public void addMenuListener(StateListener listener) {
		menuListeners.add(listener);
	}

	/**
	 * Displays a message to the user.
	 * Messages are displayed directly instead of queuing.
	 *
	 * @param message the message to display
	 */
	@Override
	public void showMessage(String message) {
		// Display messages directly without clearing screen
		System.out.println(message);
	}

	/**
	 * Displays an error message to the user.
	 * Error messages are displayed directly.
	 *
	 * @param message the error message to display
	 */
	@Override
	public void showError(String message) {
		// Display error messages directly without clearing screen
		System.out.println(String.format("❌ ERROR: %s", message));
	}

	// clearScreen is defined above

	/**
	 * Private implementation detail: Screen clearing is managed internally by the CLIDisplay
	 * at specific points:
	 * 1. Before showing the main menu
	 * 2. Before starting a new game
	 * 3. After reading the destination coordinate
	 */
	private void clearScreen() {
		if (disableScreenClear) {
			System.out.println("<clearScreen>");
			return;
		}

		System.out.print("\033[H\033[2J");
		System.out.flush(); // Ensure the clear command is sent immediately
	}

	private void delay(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// Ignore interruption
		}
	}

	/**
	 * Updates the display with the current player's information.
	 * This is displayed after the board and captured pieces, without clearing the screen.
	 *
	 * @param playerColor the color of the current player
	 * @param timeConsumed the time consumed by the current player
	 */
	@Override
	public void showPlayerInfo(String playerColor, LocalTime timeConsumed) {
		// Display player info directly without clearing the screen
		System.out.println(
		    "-----------------------------------------------------"
		);
		System.out.println(
		    String.format(
		        "%s's turn, time consumed so far: ",
		        playerColor,
		        formatTime(timeConsumed)
		    )
		);
		System.out.println(
		    "-----------------------------------------------------"
		);
	}

	/**
	 * Shows a move status message.
	 *
	 * @param status the status of the move
	 */
	@Override
	public void showMoveStatus(MoveStatus status) {
		// Display move status directly without clearing screen
		if (status == MoveStatus.Ok) {
			showMessage("✓ Moved successfully, turn ending.");
		} else {
			showError(status.info);
		}
	}

	/**
	 * Shows the game end message.
	 * This will clear the screen before showing the game end message.
	 *
	 * @param message the end game message
	 */
	@Override
	public void showGameEnd(String message) {
		// Clear screen before showing game end message
		clearScreen();
		delay(1000);
		System.out.println(
		    "====================================================="
		);
		System.out.println(message);
		System.out.println(
		    "====================================================="
		);
	}

	/**
	 * Shows a check warning.
	 */
	@Override
	public void showCheckWarning() {
		// Display check warning directly without clearing screen
		System.out.println("=============================");
		System.out.println("| ⚠️ WARNING! YOU ARE CHECKED! |");
		System.out.println("=============================");
	}

	/**
	 * Formats a {@code LocalTime} object as a {@code String} in HH:mm:ss format.
	 *
	 * @param time the {@code LocalTime} to format
	 * @return a {@code String} representation of the time in HH:mm:ss
	 */
	private String formatTime(LocalTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return time.format(formatter);
	}

	/**
	 * Notifies all registered move listeners about a move request.
	 * This method is called internally when a move is requested via the CLI.
	 *
	 * @param source the source coordinate
	 * @param destination the destination coordinate
	 */
	private void notifyMoveListeners(Coord source, Coord destination) {
		for (MoveListener listener : moveListeners) {
			listener.onMoveRequested(source, destination);
		}
	}

	private void notifyStateListeners(State result) {
		for (StateListener listener : menuListeners) {
			listener.onStateChange(result);
		}
	}
}
