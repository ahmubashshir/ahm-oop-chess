package bd.ac.miu.cse.b60.oop.ahm;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.Coord;
import bd.ac.miu.cse.b60.oop.ahm.chess.Player;
import bd.ac.miu.cse.b60.oop.ahm.chess.CLIDisplay;
import bd.ac.miu.cse.b60.oop.ahm.chess.MoveStatus;
import bd.ac.miu.cse.b60.oop.ahm.chess.MenuResult;
/**
 * Entry point class for the {@code Chess} game.
 * Handles the main menu, user input, and game loop.
 */
public final class Chess {

	/** Default time limit for each player. */
	public static final LocalTime timeLimit = LocalTime.parse("05:00");

	/**
	 * Constructs a new {@code Chess} instance and initializes the {@code Scanner}.
	 */
	public Chess() {}


	/**
	 * Entry point of the {@code Chess} game.
	 *
	 * @param args command-line arguments (not used)
	 */
	public static final void main(String[] args) {
		CLIDisplay disp = new CLIDisplay();
		Game game = null;

		System.out.print("\033[H\033[2J");
		while (true) {
			switch(disp.mainMenu()) {
			case START: { // Start game
				game = new Game(timeLimit, disp);
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
						src = disp.getCoord("of piece to move");
						if (src == null) {
							System.out.println("Game ended forcefully.");
							game.end();
							break;
						}

						dst = disp.getCoord("of the square to move the piece to");
						if (dst == null) {
							System.out.println("Game ended forcefully.");
							game.end();
							break;
						}

						System.out.print("\033[H\033[2J");
					} catch (IllegalArgumentException|StringIndexOutOfBoundsException e) {
						System.out.print("\033[H\033[2J");
						System.out.println("ERROR: Invalid parameters!");
						System.out.println(e);
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
			};
			break;
			case EXIT: {
				System.out.println("Thank you for playing");
				if (game != null) game.end();
				return;
			}
			default:
				System.out.println("ERROR: Invalid menu selection!");
				break;
			}
		}
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
