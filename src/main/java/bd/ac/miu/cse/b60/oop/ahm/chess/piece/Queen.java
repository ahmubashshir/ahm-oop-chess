package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import bd.ac.miu.cse.b60.oop.ahm.chess.Color;

/**
 * The {@code Queen} class represents a queen chess piece that extends the
 * {@code Piece} class.
 * It includes methods to check the validity of a move for the queen on a given
 * chessboard.
 *
 * The queen can move horizontally, vertically, or diagonally and must have a
 * clear path to the destination.
 *
 */
public class Queen extends Piece
{

	/**
	 * Constructs a new {@code Queen} object with the specified color.
	 *
	 * @param color  Sets {@code Color} of the Piece.
	 */
	public Queen(Color color)
	{
		super("Queen", color);
	}

	/**
	* Checks if the move for the queen from the source position to the destination
	* position is valid.
	* The move must be either horizontal, vertical, or diagonal, and there should
	* be a clear path to the destination.
	*
	* @param sourceRow The row index of the source position.
	* @param sourceCol The column index of the source position.
	* @param destRow   The row index of the destination position.
	* @param destCol   The column index of the destination position.
	* @param board     The chessboard represented as a 2D array of squares.
	* @return {@code true} if the move is valid, {@code false} otherwise.
	*/
	public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, Square[][] board)
	{
		// Check if the move is either horizontal, vertical, or diagonal
		boolean isHorizontalMove = ((sourceRow == destRow) && (sourceCol != destCol));
		boolean isVerticalMove = ((sourceRow != destRow) && (sourceCol == destCol));
		boolean isDiagonalMove = (Math.abs(destRow - sourceRow) == Math.abs(destCol - sourceCol));

		// Check if the destination is within the bounds of the board
		boolean isWithinBounds = ((destRow >= 0) && (destRow < board.length) && (destCol >= 0)
		                          && (destCol < board[0].length));

		if (isWithinBounds && (isHorizontalMove || isVerticalMove || isDiagonalMove))
			{

				// Check for a clear path in the horizontal movement
				if (isHorizontalMove)
					{
						int minCol = Math.min(sourceCol, destCol);
						int maxCol = Math.max(sourceCol, destCol);

						for (int col = minCol + 1; col < maxCol; col++)
							{
								// Check if piece in path, if so return false
								if (board[sourceRow][col].getPiece() != null)
									{
										return false;
									}
							}
					}

				// Check for a clear path in the vertical movement
				if (isVerticalMove)
					{
						int minRow = Math.min(sourceRow, destRow);
						int maxRow = Math.max(sourceRow, destRow);

						for (int row = minRow + 1; row < maxRow; row++)
							{
								// Check if piece in path, if so return false
								if (board[row][sourceCol].getPiece() != null)
									{
										return false;
									}
							}
					}

				// Check for a clear path in the diagonal movement
				if (isDiagonalMove)
					{
						if (Math.abs(destRow - sourceRow) == Math.abs(destCol - sourceCol))
							{
								int rowIncrement = (destRow > sourceRow) ? 1 : -1;
								int colIncrement = (destCol > sourceCol) ? 1 : -1;

								int currentRow = sourceRow + rowIncrement;
								int currentCol = sourceCol + colIncrement;

								// Corrected loop condition
								while (currentRow != destRow || currentCol != destCol)
									{
										// Check if piece in path, if so return false
										if (board[currentRow][currentCol].getPiece() != null)
											{
												return false;
											}
										// Increment loop parameters to continue index
										currentRow += rowIncrement;
										currentCol += colIncrement;
									}
							}
					}

				// Path is clear
				return true;
			}

		// Invalid move
		return false;
	}
}
