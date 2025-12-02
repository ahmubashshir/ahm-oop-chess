package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Color;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;

/**
 * The Pawn class represents a pawn chess piece.
 *
 */
public class Pawn extends Piece {

	private boolean hasMoved; // Keep track of whether the pawn has moved

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

	/**
	 * Returns the symbol representation of the pawn.
	 *
	 * @return The symbol representation of the pawn.
	 */
	public String getSymbol() {
		return isWhite() ? "♙" : "♟";
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
		// Get direction of pawn reliant on what color
		if (isWhite()) {
			direction = 1;
		} else {
			direction = -1;
		}

		// Check if the destination is within the bounds of the board
		boolean isWithinBounds = ((destRow >= 0) &&
		                          (destRow < board.length) &&
		                          (destCol >= 0) &&
		                          (destCol < board[0].length));

		// Pass only if in bounds
		if (isWithinBounds) {
			// Move forward
			if (
			    (sourceCol == destCol) &&
			    (board[destRow][destCol].getPiece() == null)
			) {
				// Move one square
				if (sourceRow + direction == destRow) {
					hasMoved = true;
					return true;
				}
				// Move two squares on its first move
				else if (
				    (!hasMoved) &&
				    (sourceRow + 2 * direction == destRow) &&
				    (board[sourceRow + direction][destCol].getPiece() == null)
				) {
					hasMoved = true;
					return true;
				}
			}
			// Capture diagonally
			else if (
			    (Math.abs(destCol - sourceCol) == 1) &&
			    (sourceRow + direction == destRow)
			) {
				// Check if there is an opponent's piece to capture
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
