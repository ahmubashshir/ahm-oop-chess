package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Represents a generic chess piece, serving as the base class for all specific chess pieces.
 */
public abstract class Piece {
	/** The formatted name of the piece, including its color tag. */
	private String name;

	/** The color of the piece (white or black). */
	private final Color color;

	/** Indicates whether the piece has been captured. */
	private boolean isCaptured;

	/**
	 * Constructs a {@code Piece} with the specified name and color.
	 *
	 * @param name  the base name of the piece (e.g., "King", "Queen")
	 * @param color the {@code Color} of the piece
	 */
	protected Piece(String name, Color color) {
		this.color = color;
		this.name = String.format("%s<%s>", name, color.tag);
	}

	/**
	 * Returns the formatted name of the piece.
	 *
	 * @return the name of the piece, including its color tag
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the captured status of the piece.
	 *
	 * @param isCaptured {@code true} if the piece is captured, {@code false} otherwise
	 */
	public void setCaptured(boolean isCaptured) {
		this.isCaptured = isCaptured;
	}

	/**
	 * Returns whether the piece has been captured.
	 *
	 * @return {@code true} if the piece is captured, {@code false} otherwise
	 */
	public boolean getCaptured() {
		return isCaptured;
	}

	/**
	 * Returns whether the piece is white.
	 *
	 * @return {@code true} if the piece is white, {@code false} if black
	 */
	public boolean getIsWhite() {
		return color == Color.WHITE;
	}

	/**
	 * Checks if a move from the source square to the destination square is valid for this piece.
	 *
	 * @param sourceRow the row index of the source square
	 * @param sourceCol the column index of the source square
	 * @param destRow   the row index of the destination square
	 * @param destCol   the column index of the destination square
	 * @param board     the chessboard as a 2D array of {@code Square} objects
	 * @return {@code true} if the move is valid, {@code false} otherwise
	 */
	public abstract boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, Square[][] board);

	/**
	 * Checks if a move from the source square to the destination square is valid for this piece.
	 *
	 * @param src       Source square coordinates.
	 * @param dst       Destination square coordinates.
	 * @param board     the chessboard as a 2D array of {@code Square} objects
	 * @return {@code true} if the move is valid, {@code false} otherwise
	 */
	public boolean isValidMove(Coord src, Coord dst, Square[][] board) {
		return isValidMove(src.row, src.col, dst.row, dst.col, board);
	}

}
