package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Represents a single square on the chessboard.
 * <p>
 * Each square may contain a chess piece or be empty.
 * This class provides methods to set or retrieve the piece on the square.
 * </p>
 */
public class Square {

	/** The chess piece currently on this square, or {@code null} if empty. */
	private Piece piece;

	/**
	 * Constructs an empty square with no piece.
	 */
	public Square() {}

	/**
	 * Places a piece on this square.
	 *
	 * @param piece the piece to set, or {@code null} to clear the square
	 */
	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	/**
	 * Gets the piece currently on this square.
	 *
	 * @return the piece on this square, or {@code null} if the square is empty
	 */
	public Piece getPiece() {
		return piece;
	}
}
