package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Color;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;

/**
 * Represents the {@code King} piece in chess.
 * Extends the {@code Piece} class and tracks whether the king has moved.
 * Handles validation for standard king moves and castling.
 *
 * <p>The King is the most important piece in chess. If it is checkmated, the game is over.
 * The King can move one square in any direction (horizontally, vertically, or diagonally)
 * and can perform a special move called castling with a Rook.</p>
 *
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Piece
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Rook
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game#move
 */
public class King extends Piece {

	/**
	 * Tracks whether the king has moved.
	 * Used to determine eligibility for castling.
	 */
	private boolean hasMoved;

	/**
	 * Constructs a new {@code King} with the specified {@code Color}.
	 *
	 * @param color The {@code Color} of the king ({@code Color.WHITE} or {@code Color.BLACK})
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Color
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
	 * <p>Valid king moves include:</p>
	 * <ul>
	 *   <li>One square in any direction (horizontally, vertically, or diagonally)</li>
	 *   <li>Castling with a rook if neither piece has moved and path is clear</li>
	 * </ul>
	 *
	 * @param sourceRow The row index of the source square
	 * @param sourceCol The column index of the source square
	 * @param destRow   The row index of the destination square
	 * @param destCol   The column index of the destination square
	 * @param board     The chessboard as a 2D array of {@code Square}s
	 * @return {@code true} if the move is valid, {@code false} otherwise
	 *
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Square
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Rook#hasMoved()
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
	 * Used to determine eligibility for castling.
	 *
	 * @return {@code true} if the king has moved, {@code false} otherwise
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game#move
	 */
	public boolean hasMoved() {
		return hasMoved;
	}

	/**
	 * Sets the moved status of the {@code King}.
	 * Used primarily for game state management and castling eligibility.
	 *
	 * @param hasMoved {@code true} if the king has moved, {@code false} otherwise
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game#move
	 */
	public void setMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
}
