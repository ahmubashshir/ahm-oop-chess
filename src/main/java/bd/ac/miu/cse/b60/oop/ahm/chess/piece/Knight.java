package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Color;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;

/**
 * The {@code Knight} class represents a knight chess piece.
 * It extends the {@link Piece} class and implements specific
 * rules for the knight's movement on a chessboard.
 *
 */
public class Knight extends Piece {

	/**
	 * Constructs a new {@code Knight} object with the specified color.
	 *
	 * @param color  Sets {@code Color} of the Piece.
	 * @param game   Sets {@code Game} of the Piece.
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Color
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game
	 */
	public Knight(Color color, Game game) {
		super("Knight", color, game);
	}

	/**
	 * Returns the symbol representation of the knight.
	 *
	 * @return The symbol representation of the knight
	 */
	public String getSymbol() {
		return isWhite() ? "\u2658" : "\u265e";
	}

	/**
	 * Checks if the proposed move for the knight is valid based on the rules of chess.
	 * The knight moves in an L-shaped pattern, two squares in one direction and one square
	 * perpendicular to that direction, or vice versa.
	 *
	 * This logic is critical because the knight is the only piece that can "jump" over others.
	 *
	 * @param sourceRow  The current row of the knight.
	 * @param sourceCol  The current column of the knight.
	 * @param destRow    The destination row for the move.
	 * @param destCol    The destination column for the move.
	 * @return {@code true} if the move is valid, {@code false} otherwise.
	 */
	public boolean isValidMove(
	    int sourceRow,
	    int sourceCol,
	    int destRow,
	    int destCol
	) {
		final Square[][] board = this.game.getBoard();
		// Calculate absolute difference between current piece pos. and destination pos.
		int rowDiff = Math.abs(destRow - sourceRow);
		int colDiff = Math.abs(destCol - sourceCol);

		// Knight moves in an L-shape: two squares in one direction and one in the other.
		boolean isLShapedMove =
		    (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);

		// Ensure destination is within board bounds.
		boolean isWithinBounds =
		    destRow >= 0 &&
		    destRow < board.length &&
		    destCol >= 0 &&
		    destCol < board[0].length;

		return isLShapedMove && isWithinBounds;
	}
}
