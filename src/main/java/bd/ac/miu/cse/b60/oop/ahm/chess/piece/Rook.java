package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Color;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;

/**
 * The {@code Rook} class represents a rook chess piece that extends the {@code Piece} class.
 * It keeps track of whether the rook has moved or not.
 *
 */
public class Rook extends Piece {

	private boolean hasMoved; // Keep track of whether the rook has moved

	/**
	 * Constructs a new rook with the specified color.
	 *
	 * @param color  Sets {@code Color} of the Piece.
	 * @param game   Sets {@code Game} of the Piece.
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Color
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game
	 */
	public Rook(Color color, Game game) {
		super("Rook", color, game);
		this.hasMoved = false;
	}

	/**
	 * Returns the symbol representation of the rook.
	 *
	 * @return The symbol representation of the rook.
	 */
	public String getSymbol() {
		return isWhite() ? "♖" : "♜";
	}

	/**
	 * Checks if a move from the source square to the destination square is a valid move for the rook.
	 *
	 * @param sourceRow the row index of the source square.
	 * @param sourceCol the column index of the source square.
	 * @param destRow   the row index of the destination square.
	 * @param destCol   the column index of the destination square.
	 * @return {@code true} if the move is valid, {@code false} otherwise.
	 */
	public boolean isValidMove(
	    int sourceRow,
	    int sourceCol,
	    int destRow,
	    int destCol
	) {
		// Check if the move is either horizontal or vertical
		boolean isHorizontalMove = sourceRow == destRow && sourceCol != destCol;
		boolean isVerticalMove = sourceRow != destRow && sourceCol == destCol;
		final Square[][] board = this.game.getBoard();

		// Check if the destination is within the bounds of the board
		boolean isWithinBounds =
		    destRow >= 0 &&
		    destRow < board.length &&
		    destCol >= 0 &&
		    destCol < board[0].length;

		if ((isHorizontalMove || isVerticalMove) && isWithinBounds) {
			// Check for a clear path in the horizontal movement
			if (isHorizontalMove) {
				int minCol = Math.min(sourceCol, destCol);
				int maxCol = Math.max(sourceCol, destCol);

				for (int col = minCol + 1; col < maxCol; col++) {
					if (board[sourceRow][col].getPiece() != null) {
						// There is a piece in the horizontal path
						return false;
					}
				}
			}

			// Check for a clear path in the vertical movement
			if (isVerticalMove) {
				int minRow = Math.min(sourceRow, destRow);
				int maxRow = Math.max(sourceRow, destRow);

				for (int row = minRow + 1; row < maxRow; row++) {
					if (board[row][sourceCol].getPiece() != null) {
						// There is a piece in the vertical path
						return false;
					}
				}
			}

			// The path is clear
			hasMoved = true;
			return true;
		}

		// Not a valid horizontal or vertical move
		return false;
	}

	/**
	 * Checks if the rook has moved.
	 *
	 * @return {@code true} if the rook has moved, {@code false} otherwise.
	 */
	public boolean hasMoved() {
		return hasMoved;
	}

	/**
	 * Sets the moved status of the rook.
	 *
	 * @param hasMoved {@code true} if the rook has moved, {@code false} otherwise.
	 */
	public void setMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
}
