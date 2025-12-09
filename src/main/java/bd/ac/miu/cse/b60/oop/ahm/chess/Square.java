package bd.ac.miu.cse.b60.oop.ahm.chess;

import bd.ac.miu.cse.b60.oop.ahm.chess.state.BDInStream;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.BDOutStream;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.Loadable;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SaveData;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.Saveable;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SavedData;

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
	 * @param game the game context this square belongs to
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

	/**
	 * Serializes the state of this Square, including its piece if present.
	 * The output includes a flag for piece presence, and if present,
	 * the serialized piece data.
	 *
	 * @return a SavedData object representing the state of this Square
	 */
	@Override
	public SavedData save() {
		try (BDOutStream bdos = new BDOutStream()) {
			if (piece == null) {
				bdos.writeBoolean(false); // no piece
			} else {
				bdos.writeBoolean(true); // has piece
				byte[] pdata = piece.save().bytes();
				bdos.writeInt(pdata.length); // length
				bdos.write(pdata); // data
			}
			return SavedData.create(bdos.collect());
		} catch (Exception e) {
			throw new RuntimeException("Failed to save square", e);
		}
	}

	/**
	 * Loads the state of this Square from the given SaveData.
	 * If a piece is present in the data, it is reconstructed and loaded.
	 *
	 * @param state the SaveData representing the serialized Square
	 */
	public void load(SaveData state) {
		try (BDInStream bdis = new BDInStream(state.data())) {
			if (!bdis.readBoolean()) {
				setPiece(null);
			} else {
				int plen = bdis.readInt();
				SaveData pdata = SaveData.load(bdis.readNBytes(plen));
				setPiece(Piece.fromSaveData(pdata, game));
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to load square", e);
		}
	}
}
