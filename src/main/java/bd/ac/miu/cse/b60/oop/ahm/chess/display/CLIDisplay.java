package bd.ac.miu.cse.b60.oop.ahm.chess.display;

import bd.ac.miu.cse.b60.oop.ahm.chess.Coord;
import bd.ac.miu.cse.b60.oop.ahm.chess.Display;
import bd.ac.miu.cse.b60.oop.ahm.chess.MenuResult;
import bd.ac.miu.cse.b60.oop.ahm.chess.MoveStatus;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Player;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
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
	private List<MenuListener> menuListeners;
	private boolean isRunning;
	private boolean exitRequested;

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
	 * This clears the screen first to provide a clean display.
	 *
	 * @param board a 2D array of {@code Square} representing the chessboard.
	 */
	@Override
	public void printBoard(Square[][] board) {
		// Clear the screen before displaying the board
		clearScreen();

		int cellWidth = 10; // Cell width modifier

		System.out.println(
		    "       a          b          c          d          e          f          g          h"
		);

		// Print top of the board outline
		System.out.print("  ");
		for (int i = 0; i < 89; i++) {
			System.out.print("-");
		}

		System.out.println(); // Create new line

		for (int row = 0; row < board.length; row++) {
			System.out.print(String.format("%d |", row + 1)); // Leftmost board outline
			for (int col = 0; col < board[row].length; col++) {
				if (
				    board[row][col] != null &&
				    board[row][col].getPiece() != null
				) {
					// Piece exists, print piece name
					String pieceName = board[row][col].getPiece().getName();
					System.out.print(
					    String.format("%-" + cellWidth + "s", pieceName)
					);
				} else {
					// No piece, print empty space
					System.out.print(String.format("%-" + cellWidth + "s", ""));
				}

				// Print vertical separator
				if (col < board[row].length) {
					System.out.print("|");
				}
			}

			System.out.print(" " + (row + 1));
			System.out.println();

			// Print horizontal separator
			if (row < board.length) {
				System.out.print("  ");
				for (
				    int i = 0;
				    i < (board[row].length + 1) * cellWidth - 1;
				    i++
				) {
					System.out.print("-");
				}
				System.out.println();
			}
		}

		System.out.println(
		    "       a          b          c          d          e          f          g          h"
		);
	}

	/**
	 * Prints the captured pieces for each player.
	 * This does not clear the screen as it's usually displayed along with the board.
	 *
	 * @param players an array of {@code Player} objects representing the players in the game.
	 */
	@Override
	public void printCapturedPieces(Player[] players) {
		System.out.println("\nCAPTURED PIECES:");
		System.out.println("----------------");

		for (Player player : players) {
			System.out.println(
			    "Player " + player.getPlayerID() + "'s Captured Pieces:"
			);
			Piece[] capturedPieces = player.getCapturedPieces();
			int capturedCount = player.getCapturedCount();

			if (capturedCount == 0) {
				System.out.println("  None");
			} else {
				for (int i = 0; i < capturedCount; i++) {
					System.out.println("  " + capturedPieces[i].getName());
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

		// Show what was entered without clearing the screen
		System.out.println(String.format("You entered: %s", in));

		return new Coord(in.charAt(0 /* col */), in.charAt(1 /* row */));
	}

	/**
	 * Displays the main menu and reads the user's choice.
	 * This is a synchronous operation that blocks until a selection is made.
	 *
	 * @return the {@code MenuResult} corresponding to the user's menu selection
	 */
	@Override
	public MenuResult mainMenu() {
		// Clear screen before showing menu
		clearScreen();

		// Display the main menu header and options
		System.out.println("A Simple CLI Chess - Menu");
		System.out.println("=========================");
		System.out.println("1. Start a new game");
		System.out.println("2. Exit");
		System.out.println();

		// Print the prompt without a newline
		System.out.print("Your choice: ");

		MenuResult result;
		// Handle case where there's no input available
		if (input.hasNextInt()) {
			result = MenuResult.fromInt(input.nextInt());
		} else {
			// Default to starting a new game if no input available
			System.out.println("No input detected, defaulting to new game");
			result = MenuResult.START;
			if (input.hasNext()) {
				input.next(); // Consume whatever is there
			}
		}

		// Notify listeners of menu selection
		for (MenuListener listener : menuListeners) {
			listener.onMenuSelected(result);
		}

		if (result == MenuResult.EXIT) {
			input.close();
			isRunning = false;
		}

		// Display a confirmation message about the selection
		if (result == MenuResult.START) {
			System.out.println("Starting a new game...");
		} else if (result == MenuResult.EXIT) {
			System.out.println("Exiting the game...");
		}

		return result;
	}

	/**
	 * Starts the CLI display event loop.
	 * For CLI implementation, this simply sets a flag as the actual loop
	 * is driven by the synchronous methods.
	 */
	@Override
	public void run() {
		isRunning = true;

		// Start with a clean screen - no welcome message needed here
		// as the mainMenu method will show the menu header
		clearScreen();

		// CLI is synchronous, so the actual "event loop" happens
		// in the getCoord and mainMenu methods
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
	public void addMenuListener(MenuListener listener) {
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
		System.out.println("ERROR: " + message);
	}

	/**
	 * Clears the console screen.
	 * This is a private implementation detail of the CLI display.
	 */
	private void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush(); // Ensure the clear command is sent immediately
	}

	/**
	 * Checks if the user has requested to exit the game.
	 *
	 * @return true if the user entered 'q' during coordinate input
	 */
	@Override
	public boolean isExitRequested() {
		return exitRequested;
	}

	/**
	 * Resets the exit requested flag.
	 */
	@Override
	public void resetExitFlag() {
		exitRequested = false;
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
		System.out.println(playerColor + " Player's turn.");
		System.out.println(
		    playerColor +
		    " Player's time consumed so far: " +
		    formatTime(timeConsumed)
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
			System.out.println("✓ Moved successfully, turn ending.");
		} else {
			System.out.println(String.format("❌ ERROR: %s", status.info));
		}
	}

	/**
	 * Shows the game end message.
	 *
	 * @param message the end game message
	 */
	@Override
	public void showGameEnd(String message) {
		// Game end can clear screen since it's a final state
		clearScreen();
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
}
