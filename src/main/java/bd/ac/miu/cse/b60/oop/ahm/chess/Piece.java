package bd.ac.miu.cse.b60.oop.ahm.chess;

import bd.ac.miu.cse.b60.oop.ahm.chess.piece.Type;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.Loadable;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SaveData;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.Saveable;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SavedData;
import java.lang.reflect.Constructor;

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
public abstract class Piece implements Saveable, Loadable {

	/** The formatted name of the piece, including its color tag. */
	private String name;

	/**
	 * Returns the type byte representing this piece.
	 * Used for serialization and identification of piece type.
	 *
	 * @return the type byte for this piece
	 */
	protected abstract byte getTypeByte();

	@Override
	public SavedData save() {
		return SavedData.create(
		           new byte[] {
		               getTypeByte(),
		               (byte) color.id,
		               (byte) (isCaptured ? 1 : 0),
		           }
		       );
	}

	@Override
	public void load(SaveData state) {
		byte[] data = state.data();
		setCaptured(data[2] == 1);
	}

	/**
	 * Creates a Piece instance from its serialized byte representation.
	 *
	 * @param typeByte  the type byte representing the piece type
	 * @param colorByte the color byte (1 for white, 0 for black)
	 * @param game      the game instance to associate with the piece
	 * @return the deserialized Piece instance
	 * @throws RuntimeException if instantiation fails
	 */
	public static Piece fromSaveData(SaveData state, Game game) {
		try {
			byte[] data = state.data();
			Class<?> type = Type.fromTag(data[0]).type;
			Constructor<?> ctor = type.getConstructor(Color.class, Game.class);
			Piece p = (Piece) ctor.newInstance(Color.fromId(data[1]), game);
			p.load(state);
			return p;
		} catch (Exception e) {
			throw new RuntimeException(
			    "Failed to instantiate piece from byte",
			    e
			);
		}
	}

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
	 * Notifies the piece that it has moved.
	 * Subclasses may override to implement custom behavior after a move.
	 */
	public void moveNotify() {}

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
