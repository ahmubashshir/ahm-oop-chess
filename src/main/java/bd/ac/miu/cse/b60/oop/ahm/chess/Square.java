package bd.ac.miu.cse.b60.oop.ahm.chess;

import bd.ac.miu.cse.b60.oop.ahm.chess.state.*;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SaveData;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
	private final Game game;

	/**
	 * Constructs an empty square with no piece.
	 */
	public Square(Game game) {
		this.game = game;
	}

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
	public SavedData save() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (piece == null) {
				baos.write(0); // no piece
			} else {
				baos.write(1); // has piece
				byte[] pdata = piece.save().bytes();
				baos.write(pdata.length); // length
				baos.write(pdata); // data
			}
			return SavedData.create(baos.toByteArray());
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
	public void load(SaveData state) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(state.data());
			int hasPiece = bais.read();
			if (hasPiece == 0) {
				setPiece(null);
			} else {
				int plen = bais.read();
				SaveData pdata = SaveData.load(bais.readNBytes(plen));
				setPiece(Piece.fromSaveData(pdata, game));
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to load square", e);
		}
	}
}
