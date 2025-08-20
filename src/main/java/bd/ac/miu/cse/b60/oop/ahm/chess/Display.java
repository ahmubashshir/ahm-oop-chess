package bd.ac.miu.cse.b60.oop.ahm.chess;

import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;

public class Display
{

	public void printBoard(Square[][] board)
	{
		int cellWidth = 10; // Cell width modifier

		System.out.println("     1          2          3          4          5          6          7          8");

		// Print top of the board outline
		for (int i = 0; i < 89; i++)
			{
				System.out.print("-");
			}

		System.out.println(); // Create new line

		for (int row = 0; row < board.length; row++)
			{
				System.out.print("|"); // Leftmost board outline
				for (int col = 0; col < board[row].length; col++)
					{
						// Check if piece is present for instance of coordinate
						if (board[row][col] != null && board[row][col].getPiece() != null)
							{
								// Piece exists, print piece data
								String pieceName = board[row][col].getPiece().getName();
								System.out.print(String.format("%-" + cellWidth + "s", pieceName));
							}
						else
							{
								// Piece does not exist, print nothing
								System.out.print(String.format("%-" + cellWidth + "s", ""));
							}

						// Print vertical line
						if (col < board[row].length)
							{
								System.out.print("|");
							}
					}
				System.out.print(" " + (row + 1));
				System.out.println(); // Move to the next line after printing each row

				// Print horizontal lines
				if (row < board.length)
					{
						for (int i = 0; i < (board[row].length + 1) * cellWidth - 1; i++)
							{
								System.out.print("-");
							}
						System.out.println();
					}
			}
		System.out.println("     1          2          3          4          5          6          7          8");
	}

	/**
	* Prints the captured pieces for each player.
	*
	* @param players An array of {@code Player} objects representing the players in
	*                the game.
	*/
	public void printCapturedPieces(Player[] players)
	{
		for (Player player : players)
			{
				System.out.println("Player " + player.getPlayerID() + "'s Captured Pieces:");
				Piece[] capturedPieces = player.getCapturedPieces();
				int capturedCount = player.getCapturedCount();

				for (int i = 0; i < capturedCount; i++)
					{
						System.out.println(capturedPieces[i].getName());
					}

				System.out.println(); // Separate players' captured pieces
			}
	}
}
