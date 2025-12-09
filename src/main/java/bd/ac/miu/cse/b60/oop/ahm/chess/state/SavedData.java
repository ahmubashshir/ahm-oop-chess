package bd.ac.miu.cse.b60.oop.ahm.chess.state;

/**
 * Represents a saved state of the chess game, including checksum.
 * Provides methods to retrieve the raw bytes and convert to SaveData.
 */
public final class SavedData extends Serializer {

	/**
	 * Constructs a new SavedData with the given byte data.
	 *
	 * @param data the byte array representing the state
	 */
	protected SavedData(byte... data) {
		super(data);
	}

	/**
	 * Returns the combined checksum and data as a byte array.
	 * @return the byte array containing checksum and data
	 */
	public byte[] bytes() {
		byte[] ret = new byte[checksum.length + data.length];
		System.arraycopy(checksum, 0, ret, 0, checksum.length);
		System.arraycopy(data, 0, ret, checksum.length, data.length);
		return ret;
	}

	/**
	 * Converts this SavedData to a SaveData object.
	 * @return the corresponding SaveData object
	 */
	public SaveData toSaveData() {
		return SaveData.load(bytes());
	}

	/**
	 * Factory method to create a SavedData instance.
	 * @param data the byte array representing the state
	 * @return a new SavedData instance
	 */
	public static final SavedData create(byte... data) {
		return new SavedData(data);
	}
}
