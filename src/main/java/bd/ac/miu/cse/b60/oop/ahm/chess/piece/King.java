package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Color;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SavePayload;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SaveFrame;

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
	 * @param game The {@code Game} instance associated with the king
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Color
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game
	 */
	public King(Color color, Game game) {
		super("King", color, game);
		this.hasMoved = false;
	}

	/**
	 * Returns the symbol representation of the king.
	 *
	 * @return The symbol representation of the king
	 */
	public String getSymbol() {
		return isWhite() ? "\u2654" : "\u265a";
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
	 * @return {@code true} if the move is valid, {@code false} otherwise
	 *
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Square
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Rook#hasMoved()
	 */
	public boolean isValidMove(
	    int sourceRow,
	    int sourceCol,
	    int destRow,
	    int destCol
	) {
		final Square[][] board = this.game.getBoard();
		// Check one-square move
		boolean isOneSquareMove =
		    (Math.abs(destRow - sourceRow) <= 1) &&
		    (Math.abs(destCol - sourceCol) <= 1);
		// Check bounds
		boolean isWithinBounds =
		    (destRow >= 0) &&
		    (destRow < board.length) &&
		    (destCol >= 0) &&
		    (destCol < board[0].length);

		if (isWithinBounds) {
			if (isOneSquareMove) {
				return true;
			} else if (
			    !hasMoved &&
			    (Math.abs(destCol - sourceCol) == 2) &&
			    (destRow == sourceRow)
			) {
				// Castling logic: king moves two squares horizontally, rook must not have moved, path must be clear
				int rookCol = (destCol > sourceCol) ? board[0].length - 1 : 0;
				Square rookSquare = board[destRow][rookCol];

				if (
				    (rookSquare.getPiece() instanceof Rook) &&
				    (!((Rook) rookSquare.getPiece()).hasMoved())
				) {
					int colIncrement = (destCol > sourceCol) ? 1 : -1;

					// Check that all squares between king and rook are empty
					for (
					    int col = sourceCol + colIncrement;
					    col != destCol;
					    col += colIncrement
					) {
						if (
						    board[destRow][col].getPiece() != null
						) return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void moveNotify() {
		if (hasMoved) return;
		this.hasMoved = true;
	}

	@Override
	protected byte getTypeByte() {
		return Type.KING.tag;
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
	 * Serializes the King piece state to a SaveFrame object.
	 * Includes base Piece data and the hasMoved flag.
	 *
	 * @return a SaveFrame object representing the King state
	 */
	@Override
	public SaveFrame save() {
		// 3 bytes from Piece + 1 byte for hasMoved
		byte[] base = super.save().toSavePayload().data();
		byte[] out = new byte[base.length + 1];
		System.arraycopy(base, 0, out, 0, base.length);
		out[base.length] = (byte) (hasMoved ? 1 : 0);
		return SaveFrame.create(out);
	}

	/**
	 * Loads the King piece state from the given SavePayload object.
	 * Restores base Piece state and the hasMoved flag.
	 *
	 * @param state the SavePayload object containing serialized King state
	 */
	@Override
	public void load(SavePayload state) {
		super.load(state);
		byte[] data = state.data();
		if (data.length > 3) {
			this.hasMoved = (data[3] == 1);
		} else {
			this.hasMoved = false;
		}
	}
}
