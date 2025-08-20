package bd.ac.miu.cse.b60.oop.ahm;

import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import bd.ac.miu.cse.b60.oop.ahm.chess.Player;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.Coord;
import bd.ac.miu.cse.b60.oop.ahm.chess.MoveStatus;

/**
 * Entry Point class
 */

public final class Chess
{
	public static final LocalTime timeLimit = LocalTime.parse("05:00");
	private Scanner input;

	private static final String[] statusMessage =
	{
		"ERROR: Invalid color piece to choose"
		, "ERROR: Invalid castling movement"
		, "ERROR: Path is not clear"
		, "ERROR: Invalid combination of movement"
		, "ERROR: Destination is of same color"
	};

	public Chess()
	{
		Scanner input = new Scanner(System.in);
	}

	public void stop()
	{
		input.close();
	}

	public int mainMenu()
	{
		System.out.println("1. Start a new game");
		System.out.println("2. Exit");
		System.out.print("Your choice: ");
		return input.nextInt();
	}

	public Coord getCoord(String query)
	{
		System.out.print(String.format("Enter the position %s ('-1' to quit) (x y): ", query));

		int col = input.nextInt();
		if (col == -1) return null;
		int row = input.nextInt();
		if (row == -1) return null;
		return new Coord(col, row);
	}

	/**
	 * Entry point of this game.
	 */
	public static final void main(String[] args)
	{
		Chess chess = new Chess();
		int mainMenuSelection;
		Game game = null;

		while (true)
			{
				mainMenuSelection = chess.mainMenu();
				if (mainMenuSelection == 1)   // Play game
					{
						game = new Game(timeLimit);
						game.initializePiecePositions();
						game.setMaxNumOfTurns(10);

						Player currentPlayer = game.getCurrentPlayer();

						// Start the timer for both but pause and then go back to player 0
						currentPlayer.startTimer();
						currentPlayer.pauseTimer();

						game.switchPlayer();
						currentPlayer = game.getCurrentPlayer();

						currentPlayer.startTimer();
						currentPlayer.pauseTimer();

						game.switchPlayer();

						while (true)
							{
								String playerColor;
								currentPlayer = game.getCurrentPlayer();

								if (currentPlayer.getPlayerID() == 1)
									{
										playerColor = "Black";
									}
								else
									{
										playerColor = "White";
									}

								System.out.println("\n=====================================================");
								System.out.println(playerColor + " Player's turn.");

								if (game.isDraw())   // If equal amount of turns set by parameter
									{
										System.out.println("=====================================================");

										System.out.println("Both players have ran out of turns, game ends in a draw");
										game.end();
										break;
									}
								else if (game.isTimeFinished())   // If time set by parameter has run out
									{
										System.out.println("=====================================================");

										System.out.println(playerColor + " loses as they ran out of time. ");
										game.end();
										break;
									}
								else if (!game.isKingAlive())   // If King was killed
									{
										System.out.println("=====================================================");

										System.out.println(playerColor + " King is dead, they lose the game");
										game.end();
										break;
									}
								if (game.isCheck())   // If King is under threat
									{
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

								currentPlayer.continueTimer(); // Continue timer for the next player's turn

								Coord src, dst;
								try
									{
										src = chess.getCoord("of piece to move");
										if(src == null)
											{
												System.out.println("Game ended forcefully.");
												game.end();
												break;
											}

										dst = chess.getCoord("of the square to move the piece to");
										if(dst == null)
											{
												System.out.println("Game ended forcefully.");
												game.end();
												break;
											}

										System.out.print("\033[H\033[2J");
									}
								catch(IllegalArgumentException e)
									{
										System.out.println("ERROR: Invalid parameters!");
										continue;
									}

								if (game.isTimeFinished())
									{
										System.out.println(playerColor + " loses as they ran out of time. ");
										System.out.println("-----------------------------------------------------");
										game.end();
										break;
									}

								var moveStatus = game.move(src, dst);
								if (moveStatus == MoveStatus.Ok)
									{
										System.out.println("Moved successfully, turn ending.");
										currentPlayer.pauseTimer(); // Pause timer after each move
										game.setNumOfTurns(game.getCurrentPlayer(), 1);
										game.switchPlayer();
									}
								else
									{
										System.out.println(String.format("ERROR: %s", moveStatus.info));
									}
							}

					}
				else if (mainMenuSelection == 2)
					{
						System.out.println("Thank you for playing");
						if(game != null) game.end();
						break;
					}
				else
					{
						System.out.println("ERROR: Invalid menu selection!");
					}
			}
		chess.stop();
	}

	// Helper method to format LocalTime as HH:mm:ss
	private static String formatTime(LocalTime time)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return time.format(formatter);
	}
}
