package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import bd.ac.miu.cse.b60.oop.ahm.chess.Color;

/**
 * Represents the {@code King} piece in chess.
 * Extends the {@code Piece} class and tracks whether the king has moved.
 * Handles validation for standard king moves and castling.
 */
public class King extends Piece {
	private boolean hasMoved; // Tracks whether the king has moved

	/**
	 * Constructs a new {@code King} with the specified {@code Color}.
	 *
	 * @param color The {@code Color} of the king ({@code Color.WHITE} or {@code Color.BLACK})
	 */
	public King(Color color) {
		super("King", color);
		this.hasMoved = false;
	}

	/**
	 * Determines whether a move from the source square to the destination square
	 * is valid for a {@code King}.
	 * Supports both regular one-square moves in any direction and castling.
	 *
	 * @param sourceRow The row index of the source square
	 * @param sourceCol The column index of the source square
	 * @param destRow   The row index of the destination square
	 * @param destCol   The column index of the destination square
	 * @param board     The chessboard as a 2D array of {@code Square}s
	 * @return {@code true} if the move is valid, {@code false} otherwise
	 */
	public boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, Square[][] board) {
		// Check one-square move
		boolean isOneSquareMove = (Math.abs(destRow - sourceRow) <= 1) && (Math.abs(destCol - sourceCol) <= 1);
		// Check bounds
		boolean isWithinBounds = (destRow >= 0) && (destRow < board.length) && (destCol >= 0) && (destCol < board[0].length);

		if (isWithinBounds) {
			if (isOneSquareMove) {
				this.hasMoved = true;
				return true;
			} else if (!hasMoved && (Math.abs(destCol - sourceCol) == 2) && (destRow == sourceRow)) {
				int rookCol = (destCol > sourceCol) ? board[0].length - 1 : 0;
				Square rookSquare = board[destRow][rookCol];

				if ((rookSquare.getPiece() instanceof Rook) && (!((Rook) rookSquare.getPiece()).hasMoved())) {
					int colIncrement = (destCol > sourceCol) ? 1 : -1;

					for (int col = sourceCol + colIncrement; col != destCol; col += colIncrement)
						if (board[destRow][col].getPiece() != null)
							return false;

					this.hasMoved = true;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns whether the {@code King} has moved.
	 *
	 * @return {@code true} if the king has moved, {@code false} otherwise
	 */
	public boolean hasMoved() {
		return hasMoved;
	}

	/**
	 * Sets the moved status of the {@code King}.
	 *
	 * @param hasMoved {@code true} if the king has moved, {@code false} otherwise
	 */
	public void setMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
}
