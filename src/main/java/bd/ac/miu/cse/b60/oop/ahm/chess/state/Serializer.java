package bd.ac.miu.cse.b60.oop.ahm.chess.state;

import java.util.zip.CRC32;

/**
 * Abstract base class for serializing and deserializing chess game state.
 * Provides utility methods for checksum calculation and byte conversions.
 */
abstract class Serializer {

	/** Raw byte data representing the serialized state. */
	protected final byte[] data;
	/** Checksum for the serialized state. */
	protected final byte[] checksum;

	/**
	 * Constructs a new Serializer with the given byte data.
	 *
	 * @param bytes the byte array representing the state
	 */
	protected Serializer(byte... bytes) {
		data = bytes;
		this.checksum = toLEBytes(checkSum(data));
	}

	/**
	 * Converts an integer to a little-endian byte array.
	 *
	 * @param x the integer to convert
	 * @return the little-endian byte array
	 */
	public static final byte[] toLEBytes(int x) {
		byte[] bytes = new byte[Integer.BYTES];
		for (int i = 0; i < Integer.BYTES; i++) {
			bytes[i] = (byte) (x & 0xFF);
			x >>= 8;
		}
		return bytes;
	}

	/**
	 * Converts a little-endian byte array to an integer.
	 *
	 * @param bytes the byte array to convert
	 * @return the integer value
	 * @throws IllegalArgumentException if the byte array length is invalid
	 */
	public static final int toInt(byte[] bytes) {
		if (bytes.length != Integer.BYTES) {
			throw new IllegalArgumentException("Invalid byte array length");
		}
		int result = 0;
		for (int i = 0; i < Integer.BYTES; i++) {
			result |= (bytes[i] & 0xFF) << (i * 8);
		}
		return result;
	}

	/**
	 * Calculates the CRC32 checksum for the given byte array.
	 *
	 * @param data the byte array to calculate the checksum for
	 * @return the checksum
	 */
	protected static final int checkSum(byte[] data) {
		CRC32 crc = new CRC32();
		crc.update(data);
		return (int) (crc.getValue() & 0xFFFFFFFF);
	}
}
