package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Abstract base class for all chess pieces.
 * <p>
 * This class defines the common properties and behaviors shared by all chess pieces,
 * such as color, name, capture state, and movement validation. Specific piece types
 * (King, Queen, Rook, Bishop, Knight, Pawn) should extend this class and implement
 * their unique movement logic.
 * </p>
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

	/** The game instance that owns this piece. */
	protected final Game game;

	/**
	 * The color of the piece (white or black).
	 *
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Color
	 */
	private final Color color;

	/** Indicates whether the piece has been captured. */
	private boolean isCaptured;

	/**
	 * Constructs a new chess piece.
	 *
	 * @param name  the base name of the piece (e.g., "King", "Queen")
	 * @param color the color of the piece
	 * @param game  the game instance that owns this piece
	 */
	protected Piece(String name, Color color, Game game) {
		this.color = color;
		this.name = String.format("%s<%s>", name, color.tag);
		this.game = game;
	}

	/**
	 * Gets the formatted name of the piece, including its color tag.
	 *
	 * @return the name of the piece (e.g., "King&lt;WHITE&gt;")
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the symbol representing this piece (e.g., "K" for King).
	 *
	 * @return the symbol of the piece
	 */
	public abstract String getSymbol();

	/**
	 * Sets whether this piece has been captured.
	 *
	 * @param isCaptured {@code true} if the piece is captured, {@code false} otherwise
	 */
	public void setCaptured(boolean isCaptured) {
		this.isCaptured = isCaptured;
	}

	/**
	 * Checks if this piece has been captured.
	 *
	 * @return {@code true} if the piece is captured, {@code false} otherwise
	 */
	public boolean getCaptured() {
		return isCaptured;
	}

	/**
	 * Checks if this piece is white.
	 *
	 * @return {@code true} if the piece is white, {@code false} if black
	 */
	public boolean isWhite() {
		return color == Color.WHITE;
	}

	/**
	 * Determines if a move from the source square to the destination square is valid for this piece.
	 * This method must be implemented by each specific piece type.
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
	 * Determines if a move from the source square to the destination square is valid for this piece,
	 * using {@link Coord} objects for convenience.
	 *
	 * @param src the source square coordinates
	 * @param dst the destination square coordinates
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
