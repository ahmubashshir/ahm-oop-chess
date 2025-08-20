package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import bd.ac.miu.cse.b60.oop.ahm.chess.Color;

/**
 * The Pawn class represents a pawn chess piece.
 *
 */
public class Pawn extends Piece
{

	private boolean hasMoved; // Keep track of whether the pawn has moved

	/**
	 * Constructs a new Pawn object with the specified color.
	 *
	 * @param color  Sets {@code Color} of the Piece.
	 */

	public Pawn(Color color)
	{
		super("Pawn", color);
		this.hasMoved = false;
	}

	/**
	* Checks if a move from the source position to the destination position is a valid move for the pawn.
	*
	* The method considers the current state of the chessboard and the specific rules for pawn movement.
	*
	* @param sourceRow The row index of the source position.
	* @param sourceCol The column index of the source position.
	* @param destRow   The row index of the destination position.
	* @param destCol   The column index of the destination position.
	* @param board     The chessboard represented as a 2D array of Square objects.
	* @return          {@code true} if the move is valid, {@code false} otherwise.
	*/
	public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, Square[][] board)
	{
		int direction;
		// Get direction of pawn reliant on what color
		if (getIsWhite())
			{
				direction = 1;
			}
		else
			{
				direction = -1;
			}

		// Check if the destination is within the bounds of the board
		boolean isWithinBounds = ((destRow >= 0) && (destRow < board.length) && (destCol >= 0)
		                          && (destCol < board[0].length));

		// Pass only if in bounds
		if (isWithinBounds)
			{
				// Move forward
				if ((sourceCol == destCol) && (board[destRow][destCol].getPiece() == null))
					{
						// Move one square
						if (sourceRow + direction == destRow)
							{
								hasMoved = true;
								return true;
							}
						// Move two squares on its first move
						else if ((!hasMoved) && (sourceRow + 2 * direction == destRow)
						         && (board[sourceRow + direction][destCol].getPiece() == null))
							{
								hasMoved = true;
								return true;
							}
					}
				// Capture diagonally
				else if ((Math.abs(destCol - sourceCol) == 1) && (sourceRow + direction == destRow))
					{
						// Check if there is an opponent's piece to capture
						Piece targetPiece = board[destRow][destCol].getPiece();
						return ((targetPiece != null) && (targetPiece.getIsWhite() != getIsWhite()));
					}
			}

		// Not a valid move for the pawn
		return false;
	}
}
