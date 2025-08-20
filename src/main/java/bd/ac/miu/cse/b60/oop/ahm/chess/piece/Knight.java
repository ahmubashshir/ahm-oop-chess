package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import bd.ac.miu.cse.b60.oop.ahm.chess.Color;

/**
 * The {@code Knight} class represents a knight chess piece.
 * It extends the {@link Piece} class and implements specific
 * rules for the knight's movement on a chessboard.
 *
 */
public class Knight extends Piece
{

	/**
	 * Constructs a new {@code Knight} object with the specified color.
	 *
	 * @param color  Sets {@code Color} of the Piece.
	 */
	public Knight(Color color)
	{
		super("Knight", color);
	}

	/**
	* Checks if the proposed move for the knight is valid based on the rules of chess.
	* The knight moves in an L-shaped pattern, two squares in one direction and one square
	* perpendicular to that direction, or vice versa.
	*
	* @param sourceRow  The current row of the knight.
	* @param sourceCol  The current column of the knight.
	* @param destRow    The destination row for the move.
	* @param destCol    The destination column for the move.
	* @param board      The chessboard represented as a 2D array of squares.
	* @return {@code true} if the move is valid, {@code false} otherwise.
	*/
	public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, Square[][] board)
	{
		// Calculate absolute difference between current piece pos. and destination pos.
		int rowDiff = Math.abs(destRow - sourceRow);
		int colDiff = Math.abs(destCol - sourceCol);

		// Check if the move is an L-shaped move for a knight
		boolean isLShapedMove = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);

		// Check if the destination is within the bounds of the board
		boolean isWithinBounds = destRow >= 0 && destRow < board.length && destCol >= 0 && destCol < board[0].length;

		return isLShapedMove && isWithinBounds;
	}

}
