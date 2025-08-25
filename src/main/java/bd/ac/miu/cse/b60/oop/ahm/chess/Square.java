package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Represents a single square on the chess board, which may contain a piece.
 */
public class Square {

	private Piece piece;

	/** Default Constructor */
	public Square() {}

	/**
	 * Places a piece on this square.
	 *
	 * @param piece the piece to set, or {@code null} to clear
	 */
	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	/**
	 * Returns the piece on this square.
	 *
	 * @return the piece, or {@code null} if empty
	 */
	public Piece getPiece() {
		return piece;
	}
}
