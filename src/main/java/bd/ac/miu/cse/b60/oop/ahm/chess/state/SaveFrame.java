package bd.ac.miu.cse.b60.oop.ahm.chess.state;

/**
 * Represents a saved state of the chess game, including checksum.
 * Provides methods to retrieve the raw bytes and convert to SavePayload.
 */
public final class SaveFrame extends Serializer {

	/**
	 * Constructs a new SaveFrame with the given byte data.
	 *
	 * @param data the byte array representing the state
	 */
	protected SaveFrame(byte... data) {
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
	 * Converts this SaveFrame to a SavePayload object.
	 * @return the corresponding SavePayload object
	 */
	public SavePayload toSavePayload() {
		return SavePayload.load(bytes());
	}

	/**
	 * Factory method to create a SaveFrame instance.
	 * @param data the byte array representing the state
	 * @return a new SaveFrame instance
	 */
	public static final SaveFrame create(byte... data) {
		return new SaveFrame(data);
	}
}
