package bd.ac.miu.cse.b60.oop.ahm.chess.state;

import java.util.Arrays;

/**
 * Represents a serializable state for the chess game.
 * This class is used to store arbitrary byte data representing the state.
 */
public final class SaveData extends Serializer {

	/**
	 * Constructs a new SaveData with the given byte data.
	 *
	 * @param bytes the byte array representing the state
	 */
	protected SaveData(byte... bytes) {
		super(bytes);
	}

	/**
	 * Returns the raw byte data of the state.
	 * @return the byte array representing the state
	 */
	public byte[] data() {
		return data;
	}

	/**
	 * Converts this SaveData to a {@link SavedData} object.
	 * @return the corresponding SavedData object
	 */
	public final SavedData toSavedData() {
		return SavedData.create(data);
	}

	/**
	 * Validates and loads a SaveData object from the given byte array.
	 *
	 * @param saved the byte array to load from
	 * @return the loaded SaveData object
	 * @throws IllegalArgumentException if the data length or checksum is invalid
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
