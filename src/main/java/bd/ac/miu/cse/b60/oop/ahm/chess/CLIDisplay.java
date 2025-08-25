package bd.ac.miu.cse.b60.oop.ahm.chess;

import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Player;
import java.util.Scanner;

/**
 * The {@code Display} class is responsible for printing the current state of
 * the chessboard and the captured pieces of each player to the console.
 */
public class CLIDisplay extends Display {

	private Scanner input;

	/** Default Constructor */
	public CLIDisplay() {
		input = new Scanner(System.in);
	}

	/**
	 * Prints the current state of the chessboard to the console.
	 *
	 * @param board a 2D array of {@code Square} representing the chessboard.
	 */
	public void printBoard(Square[][] board) {
		int cellWidth = 10; // Cell width modifier

		System.out.println("       a          b          c          d          e          f          h          h");

		// Print top of the board outline
		System.out.print("  ");
		for (int i = 0; i < 89; i++) {
			System.out.print("-");
		}

		System.out.println(); // Create new line

		for (int row = 0; row < board.length; row++) {
			System.out.print(String.format("%d |", row + 1)); // Leftmost board outline
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] != null && board[row][col].getPiece() != null) {
					// Piece exists, print piece name
					String pieceName = board[row][col].getPiece().getName();
					System.out.print(String.format("%-" + cellWidth + "s", pieceName));
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
				for (int i = 0; i < (board[row].length + 1) * cellWidth - 1; i++) {
					System.out.print("-");
				}
				System.out.println();
			}
		}

		System.out.println("       a          b          c          d          e          f          h          h");
	}

	/**
	 * Prints the captured pieces for each player.
	 *
	 * @param players an array of {@code Player} objects representing the players in the game.
	 */
	public void printCapturedPieces(Player[] players) {
		for (Player player : players) {
			System.out.println("Player " + player.getPlayerID() + "'s Captured Pieces:");
			Piece[] capturedPieces = player.getCapturedPieces();
			int capturedCount = player.getCapturedCount();

			for (int i = 0; i < capturedCount; i++) {
				System.out.println(capturedPieces[i].getName());
			}

			System.out.println(); // Separate players' captured pieces
		}
	}

	/**
	 * Prompts the user to enter a coordinate on the board.
	 *
	 * @param query a {@code String} describing which coordinate to enter
	 * @return a {@code Coord} object containing the column and row, or {@code null} if the user enters -1
	 */
	public Coord getCoord(String query) {
		System.out.print(String.format("Enter the position %s (q to abort) (col row): ", query));
		String in = input.next().trim();
		if(in.equals("q")) return null;
		System.out.println(String.format(":%s:", in));

		return new Coord(in.charAt(0 /* col */), in.charAt(1 /* row */));
	}

	/**
	 * Displays the main menu and reads the user's choice.
	 *
	 * can be no-op in event driven ui.
	 *
	 * @return the {@code MenuResult} corresponding to the user's menu selection
	 */
	public MenuResult mainMenu() {
		System.out.println("1. Start a new game");
		System.out.println("2. Exit");
		System.out.print("Your choice: ");
		MenuResult ret = MenuResult.fromInt(input.nextInt());
		if (ret == MenuResult.EXIT)
			input.close();
		System.out.print("\033[H\033[2J");
		return ret;
	}
}
