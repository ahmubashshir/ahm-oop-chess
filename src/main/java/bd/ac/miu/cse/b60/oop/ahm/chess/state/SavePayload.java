package bd.ac.miu.cse.b60.oop.ahm.chess.state;

import java.util.Arrays;

/**
 * Represents a serializable state for the chess game.
 * This class is used to store arbitrary byte data representing the state.
 */
public final class SavePayload extends Serializer {

	/**
	 * Constructs a new SavePayload with the given byte data.
	 *
	 * @param bytes the byte array representing the state
	 */
	protected SavePayload(byte... bytes) {
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
	 * Converts this SavePayload to a {@link SaveFrame} object.
	 * @return the corresponding SaveFrame object
	 */
	public final SaveFrame toSaveFrame() {
		return SaveFrame.create(data);
	}

	/**
	 * Validates and loads a SavePayload object from the given byte array.
	 *
	 * @param saved the byte array to load from
	 * @return the loaded SavePayload object
	 * @throws IllegalArgumentException if the data length or checksum is invalid
	 */
	public static final SavePayload load(byte... saved)
	throws IllegalArgumentException {
		if (saved.length < Integer.BYTES) {
			throw new IllegalArgumentException("Invalid data length");
		}

		int checksum = toInt(Arrays.copyOfRange(saved, 0, Integer.BYTES));
		byte[] data = Arrays.copyOfRange(saved, Integer.BYTES, saved.length);
		if (checkSum(data) == checksum) {
			return new SavePayload(data);
		}

		throw new IllegalArgumentException("Invalid checksum");
	}
}
