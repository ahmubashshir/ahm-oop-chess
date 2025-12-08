package bd.ac.miu.cse.b60.oop.ahm.chess;

import bd.ac.miu.cse.b60.oop.ahm.chess.state.*;

/**
 * Represents a square on the chess board.
 * <p>
 * Each square may contain a chess piece or be empty. This class provides methods
 * for saving and loading its state, as well as for setting and retrieving the piece.
 * </p>
 */
public class Square implements Saveable, Loadable {

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

	@Override
	public SaveData save() {
		try {
			java.io.ByteArrayOutputStream baos =
			    new java.io.ByteArrayOutputStream();
			if (piece == null) {
				baos.write(0); // no piece
			} else {
				baos.write(1); // has piece
				byte[] pdata = piece.save().data;
				baos.write(pdata.length); // length
				baos.write(pdata); // data
			}
			return new SaveData(baos.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException("Failed to save square", e);
		}
	}

	/**
	 * Loads the state of this square from the provided SaveData and Game context.
	 *
	 * @param state the SaveData containing the serialized state of the square
	 * @param game  the Game instance for context (used to instantiate pieces)
	 */
	public void load(SaveData state, Game game) {
		try {
			java.io.ByteArrayInputStream bais =
			    new java.io.ByteArrayInputStream(state.data);
			int hasPiece = bais.read();
			if (hasPiece == 0) {
				setPiece(null);
			} else {
				int plen = bais.read();
				byte[] pdata = bais.readNBytes(plen);
				Piece p = Piece.createFromBytes(pdata[0], pdata[1], game);
				p.setCaptured(pdata[2] == 1);
				p.load(new SaveData(pdata));
				setPiece(p);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to load square", e);
		}
	}

	@Override
	public void load(SaveData state) {
		throw new UnsupportedOperationException(
		    "Use load(State, Game) instead."
		);
	}
}
