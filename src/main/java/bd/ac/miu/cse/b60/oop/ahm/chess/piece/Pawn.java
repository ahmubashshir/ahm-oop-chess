package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Color;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SaveData;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SavedData;

/**
 * The Pawn class represents a pawn chess piece.
 *
 */
public class Pawn extends Piece {

	private boolean hasMoved; // Tracks whether the pawn has moved (affects two-square advance)

	/**
	 * Constructs a new Pawn object with the specified color.
	 *
	 * @param color  Sets {@code Color} of the Piece.
	 * @param game   Sets {@code Game} of the Piece.
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Color
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game
	 */
	public Pawn(Color color, Game game) {
		super("Pawn", color, game);
		this.hasMoved = false;
	}

	@Override
	protected byte getTypeByte() {
		return Type.PAWN.tag;
	}

	@Override
	public void moveNotify() {
		if (hasMoved) return;
		this.hasMoved = true;
	}

	@Override
	public SavedData save() {
		// 3 bytes from Piece + 1 byte for hasMoved
		byte[] base = super.save().toSaveData().data();
		byte[] out = new byte[base.length + 1];
		System.arraycopy(base, 0, out, 0, base.length);
		out[base.length] = (byte) (hasMoved ? 1 : 0);
		return SavedData.create(out);
	}

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

	/**
	 * Returns the symbol representation of the pawn.
	 *
	 * @return The symbol representation of the pawn.
	 */
	public String getSymbol() {
		return isWhite() ? "\u2659" : "\u265f";
	}

	/**
	 * Checks if a move from the source position to the destination position is a valid move for the pawn.
	 *
	 * The method considers the current state of the chessboard and the specific rules for pawn movement.
	 * - Pawns move forward one square, or two squares if they haven't moved yet.
	 * - Pawns capture diagonally.
	 *
	 * @param sourceRow The row index of the source position.
	 * @param sourceCol The column index of the source position.
	 * @param destRow   The row index of the destination position.
	 * @param destCol   The column index of the destination position.
	 * @return          {@code true} if the move is valid, {@code false} otherwise.
	 */
	public boolean isValidMove(
	    int sourceRow,
	    int sourceCol,
	    int destRow,
	    int destCol
	) {
		final Square[][] board = this.game.getBoard();

		int direction;
		// White pawns move "up" (increasing row), black pawns move "down" (decreasing row)
		direction = isWhite() ? 1 : -1;

		// Check if the destination is within the bounds of the board
		boolean isWithinBounds = ((destRow >= 0) &&
		                          (destRow < board.length) &&
		                          (destCol >= 0) &&
		                          (destCol < board[0].length));

		if (isWithinBounds) {
			// Move forward
			if (
			    (sourceCol == destCol) &&
			    (board[destRow][destCol].getPiece() == null)
			) {
				// Move one square forward
				if (sourceRow + direction == destRow) {
					return true;
				}
				// Move two squares forward on its first move
				else if (
				    (!hasMoved) &&
				    (sourceRow + 2 * direction == destRow) &&
				    (board[sourceRow + direction][destCol].getPiece() == null)
				) {
					return true;
				}
			}
			// Capture diagonally
			else if (
			    (Math.abs(destCol - sourceCol) == 1) &&
			    (sourceRow + direction == destRow)
			) {
				// Only allow capture if there is an opponent's piece
				Piece targetPiece = board[destRow][destCol].getPiece();
				return (
				           (targetPiece != null) &&
				           (targetPiece.isWhite() != isWhite())
				       );
			}
		}

		// Not a valid move for the pawn
		return false;
	}
}
