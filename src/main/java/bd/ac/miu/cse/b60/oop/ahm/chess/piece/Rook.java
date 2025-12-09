package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Color;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SaveData;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SavedData;

/**
 * The {@code Rook} class represents a rook chess piece that extends the {@code Piece} class.
 * It keeps track of whether the rook has moved or not.
 *
 */
public class Rook extends Piece {

	private boolean hasMoved; // Tracks whether the rook has moved (important for castling)

	@Override
	protected byte getTypeByte() {
		return 2;
	}

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
		return isWhite() ? "\u2656" : "\u265c";
	}

	/**
	 * Checks if a move from the source square to the destination square is a valid move for the rook.
	 * The rook moves any number of squares horizontally or vertically, but not diagonally.
	 * The path must be clear of other pieces.
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

				// Critical: must check every square between source and destination for blocking pieces
				for (int col = minCol + 1; col < maxCol; col++) {
					if (board[sourceRow][col].getPiece() != null) {
						return false;
					}
				}
			}

			// Check for a clear path in the vertical movement
			if (isVerticalMove) {
				int minRow = Math.min(sourceRow, destRow);
				int maxRow = Math.max(sourceRow, destRow);

				// Critical: must check every square between source and destination for blocking pieces
				for (int row = minRow + 1; row < maxRow; row++) {
					if (board[row][sourceCol].getPiece() != null) {
						return false;
					}
				}
			}

			// The path is clear; mark rook as moved (important for castling logic)
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

	@Override
	public void moveNotify() {
		if (hasMoved) return;
		hasMoved = true;
	}

	/**
	 * Serializes the state of this Rook, including its captured status and whether it has moved.
	 * The output consists of 3 bytes from Piece plus 1 byte for hasMoved.
	 *
	 * @return a SavedData object containing the serialized state
	 */
	@Override
	public SavedData save() {
		// 3 bytes from Piece + 1 byte for hasMoved
		byte[] base = super.save().toSaveData().data();
		byte[] out = new byte[base.length + 1];
		System.arraycopy(base, 0, out, 0, base.length);
		out[base.length] = (byte) (hasMoved ? 1 : 0);
		return SavedData.create(out);
	}

	/**
	 * Loads the state of this Rook from the provided SaveData.
	 * Restores captured status and whether the rook has moved.
	 *
	 * @param state the SaveData containing the serialized state
	 */
	@Override
	public void load(SaveData state) {
		super.load(state);
		byte[] data = state.data();
		if (data.length > 3) {
			this.hasMoved = (data[3] == 1);
		} else {
			this.hasMoved = false;
		}
	}
}
