package bd.ac.miu.cse.b60.oop.ahm.chess.state;

/**
 * Represents a serializable state for the chess game.
 * This class is used to store arbitrary byte data representing the state.
 */
public class SaveData {

	/** Raw byte data representing the serialized state. */
	public final byte[] data;

	/**
	 * Constructs a new State with the given byte data.
	 *
	 * @param data the byte array representing the state
	 */
	public SaveData(byte... data) {
		this.data = data;
	}
}
