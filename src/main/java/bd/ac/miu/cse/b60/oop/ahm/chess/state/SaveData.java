package bd.ac.miu.cse.b60.oop.ahm.chess.state;

import java.util.Arrays;

/**
 * Represents a serializable state for the chess game.
 * This class is used to store arbitrary byte data representing the state.
 */
public final class SaveData extends Serializer {

	/**
	 * Constructs a new State with the given byte data.
	 *
	 * @param data the byte array representing the state
	 */
	protected SaveData(byte... bytes) {
		super(bytes);
	}

	public byte[] data() {
		return data;
	}

	public final SavedData toSavedData() {
		return SavedData.create(data);
	}

	/**
	 * Validates and loads a SaveData object from the given byte array.
	 *
	 * @param saveData the byte array to load from
	 * @return the loaded SaveData object, or null if the checksum is invalid
	 */
	public static final SaveData load(byte... saved)
	throws IllegalArgumentException {
		if (saved.length < Integer.BYTES) {
			throw new IllegalArgumentException("Invalid data length");
		}

		int checksum = toInt(Arrays.copyOfRange(saved, 0, Integer.BYTES));
		byte[] data = Arrays.copyOfRange(saved, Integer.BYTES, saved.length);
		if (checkSum(data) == checksum) {
			return new SaveData(data);
		}

		throw new IllegalArgumentException("Invalid checksum");
	}
}
