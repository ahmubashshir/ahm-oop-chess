package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Color;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;

/**
 * The {@code Bishop} class represents a bishop chess piece that extends the {@code Piece} class.
 * It is capable of diagonal movements on the chessboard.
 */
public class Bishop extends Piece {

	@Override
	protected byte getTypeByte() {
		return 3;
	}

	/**
	 * Constructs a new bishop with the specified color.
	 *
	 * @param color  Sets {@code Color} of the Piece.
	 * @param game   Sets {@code Game} of the Piece.
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Color
	 */
	public Bishop(Color color, Game game) {
		super("Bishop", color, game);
	}

	/**
	 * Returns the symbol representing the piece.
	 *
	 * @return the symbol of the piece
	 */
	public String getSymbol() {
		return isWhite() ? "\u2657" : "\u265d";
	}

	/**
	 * Checks if a move from the source square to the destination square is a valid diagonal move for the bishop.
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
		final Square[][] board = this.game.getBoard();
		// Bishop moves diagonally: absolute difference between rows and columns must be equal.
		if (Math.abs(destRow - sourceRow) == Math.abs(destCol - sourceCol)) {
			// Check if there are no pieces in the diagonal path
			int rowIncrement = (destRow > sourceRow) ? 1 : -1;
			int colIncrement = (destCol > sourceCol) ? 1 : -1;

			// Start from the next square in the diagonal direction
			int currentRow = sourceRow + rowIncrement;
			int currentCol = sourceCol + colIncrement;

			// Traverse the diagonal path, stop before reaching destination
			while (currentRow != destRow && currentCol != destCol) {
				// If any square in the path contains a piece, the move is invalid
				if (board[currentRow][currentCol].getPiece() != null) {
					return false;
				}
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
