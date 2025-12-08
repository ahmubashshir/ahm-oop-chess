package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Represents a generic chess piece, serving as the base class for all specific chess pieces.
 *
 * <p>This abstract class defines the common properties and behaviors that all chess pieces share.
 * Specific piece types (King, Queen, Rook, etc.) extend this class to implement their unique movement patterns.</p>
 *
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.King
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Queen
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Rook
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Bishop
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Knight
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Pawn
 */
public abstract class Piece {

	/** The formatted name of the piece, including its color tag. */
	private String name;

	/** Game Instance owning this piece */
	protected final Game game;

	/**
	 * The color of the piece (white or black).
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Color
	 */
	private final Color color;

	/** Indicates whether the piece has been captured. */
	private boolean isCaptured;

	/**
	 * Constructs a {@code Piece} with the specified name and color.
	 *
	 * @param name  the base name of the piece (e.g., "King", "Queen")
	 * @param color the {@code Color} of the piece
	 * @param game  the {@code Game} instance owning this piece
	 */
	protected Piece(String name, Color color, Game game) {
		this.color = color;
		this.name = String.format("%s<%s>", name, color.tag);
		this.game = game;
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
	 * Returns the symbol representing the piece.
	 *
	 * @return the symbol of the piece
	 */
	public abstract String getSymbol();

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
	public boolean isWhite() {
		return color == Color.WHITE;
	}

	/**
	 * Checks if a move from the source square to the destination square is valid for this piece.
	 *
	 * @param sourceRow the row index of the source square
	 * @param sourceCol the column index of the source square
	 * @param destRow   the row index of the destination square
	 * @param destCol   the column index of the destination square
	 * @return {@code true} if the move is valid, {@code false} otherwise
	 */
	public abstract boolean isValidMove(
	    int sourceRow,
	    int sourceCol,
	    int destRow,
	    int destCol
	);

	/**
	 * Checks if a move from the source square to the destination square is valid for this piece.
	 * This overload allows using Coord objects for convenience.
	 *
	 * @param src       Source square coordinates.
	 * @param dst       Destination square coordinates.
	 * @return {@code true} if the move is valid, {@code false} otherwise
	 *
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Coord
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Square
	 */
	public boolean isValidMove(Coord src, Coord dst) {
		// Delegates to the main isValidMove implementation.
		return isValidMove(src.row, src.col, dst.row, dst.col);
	}
}
