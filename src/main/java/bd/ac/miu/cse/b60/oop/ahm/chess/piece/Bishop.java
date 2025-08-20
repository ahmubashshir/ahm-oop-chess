package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import bd.ac.miu.cse.b60.oop.ahm.chess.Color;

/**
 * The {@code Bishop} class represents a bishop chess piece that extends the {@code Piece} class.
 * It is capable of diagonal movements on the chessboard.
 *
 */
public class Bishop extends Piece
{

	/**
	 * Constructs a new bishop with the specified color.
	 *
	 * @param color  Sets {@code Color} of the Piece.
	 */
	public Bishop(Color color)
	{
		super("Bishop", color);
	}

	/**
	 * Checks if a move from the source square to the destination square is a valid diagonal move for the bishop.
	 *
	 * @param sourceRow the row index of the source square.
	 * @param sourceCol the column index of the source square.
	 * @param destRow   the row index of the destination square.
	 * @param destCol   the column index of the destination square.
	 * @param board     the chessboard represented by a 2D array of {@code Square}s.
	 * @return {@code true} if the move is valid, {@code false} otherwise.
	 */
	public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, Square[][] board)
	{
		// Check if the move is diagonal
		if (Math.abs(destRow - sourceRow) == Math.abs(destCol - sourceCol))
			{
				// Check if there are no pieces in the diagonal path

				// Assign 1 if upward movement (true), -1 if downward movement (false)
				int rowIncrement = (destRow > sourceRow) ? 1 : -1;
				// Assign 1 if rightward movement (true), -1 if leftward movement (false)
				int colIncrement = (destCol > sourceCol) ? 1 : -1;

				// initialized to the position immediately next to the source position in the
				// chosen diagonal direction.
				int currentRow = sourceRow + rowIncrement;
				int currentCol = sourceCol + colIncrement;

				// While loop with limit of destination coordinate
				while (currentRow != destRow && currentCol != destCol)
					{
						// Check if there is a piece in the diagonal path
						if (board[currentRow][currentCol].getPiece() != null)
							{
								return false;
							}
						// Increment loop parameters to continue index
						currentRow += rowIncrement;
						currentCol += colIncrement;
					}
				// No pieces in the diagonal path
				return true;
			}
		// Not a valid diagonal move
		return false;
	}
}
