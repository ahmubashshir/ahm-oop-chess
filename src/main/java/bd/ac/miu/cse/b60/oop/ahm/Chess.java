package bd.ac.miu.cse.b60.oop.ahm;

import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import bd.ac.miu.cse.b60.oop.ahm.chess.Player;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.Coord;
import bd.ac.miu.cse.b60.oop.ahm.chess.MoveStatus;

/**
 * Entry point class for the {@code Chess} game.
 * Handles the main menu, user input, and game loop.
 */
public final class Chess {

	/** Default time limit for each player. */
	public static final LocalTime timeLimit = LocalTime.parse("05:00");

	/** {@code Scanner} object used for reading user input. */
	private Scanner input;

	/**
	 * Constructs a new {@code Chess} instance and initializes the {@code Scanner}.
	 */
	public Chess() {
		input = new Scanner(System.in);
	}

	/**
	 * Closes the {@code Scanner} used for input.
	 */
	public void stop() {
		input.close();
	}

	/**
	 * Displays the main menu and reads the user's choice.
	 *
	 * @return the integer corresponding to the user's menu selection
	 */
	public int mainMenu() {
		System.out.println("1. Start a new game");
		System.out.println("2. Exit");
		System.out.print("Your choice: ");
		return input.nextInt();
	}

	/**
	 * Prompts the user to enter a coordinate on the board.
	 *
	 * @param query a {@code String} describing which coordinate to enter
	 * @return a {@code Coord} object containing the column and row, or {@code null} if the user enters -1
	 */
	public Coord getCoord(String query) {
		System.out.print(String.format("Enter the position %s ('-1' to quit) (col row): ", query));
		int col = input.nextInt();
		if (col == -1) return null;
		int row = input.nextInt();
		if (row == -1) return null;
		return new Coord(col - 1, row -1);
	}

	/**
	 * Entry point of the {@code Chess} game.
	 *
	 * @param args command-line arguments (not used)
	 */
	public static final void main(String[] args) {
		Chess chess = new Chess();
		Game game = null;

		while (true) {
			int mainMenuSelection = chess.mainMenu();

			if (mainMenuSelection == 1) { // Start game
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

				// Game loop
				while (true) {
					currentPlayer = game.getCurrentPlayer();
					String playerColor = currentPlayer.getPlayerID() == 1 ? "Black" : "White";

					System.out.println("\n=====================================================");
					System.out.println(playerColor + " Player's turn.");

					if (game.isDraw()) {
						System.out.println("=====================================================");
						System.out.println("Both players have run out of turns, game ends in a draw");
						game.end();
						break;
					} else if (game.isTimeFinished()) {
						System.out.println("=====================================================");
						System.out.println(playerColor + " loses as they ran out of time.");
						game.end();
						break;
					} else if (!game.isKingAlive()) {
						System.out.println("=====================================================");
						System.out.println(playerColor + " King is dead, they lose the game");
						game.end();
						break;
					} else if (game.isCheck()) {
						System.out.println("=============================");
						System.out.println("| WARNING! YOU ARE CHECKED! |");
						System.out.println("=============================");
					}

					System.out.println("-----------------------------------------------------");
					game.printCapturedPieces();
					System.out.println("-----------------------------------------------------");

					game.printBoard();

					System.out.println("-----------------------------------------------------");
					System.out.println(playerColor + " Player's time consumed so far: "
					                   + formatTime(currentPlayer.getTimeConsumed()));
					System.out.println("-----------------------------------------------------");

					currentPlayer.continueTimer(); // Continue timer

					Coord src, dst;
					try {
						src = chess.getCoord("of piece to move");
						if (src == null) {
							System.out.println("Game ended forcefully.");
							game.end();
							break;
						}

						dst = chess.getCoord("of the square to move the piece to");
						if (dst == null) {
							System.out.println("Game ended forcefully.");
							game.end();
							break;
						}

						System.out.print("\033[H\033[2J");
					} catch (IllegalArgumentException e) {
						System.out.println("ERROR: Invalid parameters!");
						continue;
					}

					if (game.isTimeFinished()) {
						System.out.println(playerColor + " loses as they ran out of time.");
						System.out.println("-----------------------------------------------------");
						game.end();
						break;
					}

					MoveStatus moveStatus = game.move(src, dst);
					if (moveStatus == MoveStatus.Ok) {
						System.out.println("Moved successfully, turn ending.");
						currentPlayer.pauseTimer();
						game.setNumOfTurns(game.getCurrentPlayer(), 1);
						game.switchPlayer();
					} else {
						System.out.println(String.format("ERROR: %s", moveStatus.info));
					}
				}

			} else if (mainMenuSelection == 2) {
				System.out.println("Thank you for playing");
				if (game != null) game.end();
				break;
			} else {
				System.out.println("ERROR: Invalid menu selection!");
			}
		}

		chess.stop();
	}

	/**
	 * Formats a {@code LocalTime} object as a {@code String} in HH:mm:ss format.
	 *
	 * @param time the {@code LocalTime} to format
	 * @return a {@code String} representation of the time in HH:mm:ss
	 */
	private static String formatTime(LocalTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return time.format(formatter);
	}
}
