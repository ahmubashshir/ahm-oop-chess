package bd.ac.miu.cse.b60.oop.ahm.chess.state;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

/**
 * Utility class for state serialization, deserialization, and checksum calculation.
 */
public class StateUtils {

	/**
	 * Calculates the CRC32 checksum for the given byte array.
	 *
	 * @param data the byte array to calculate the checksum for
	 * @return the checksum as a 4-byte array (big-endian)
	 */
	public static byte[] calculateChecksum(byte[] data) {
		CRC32 crc = new CRC32();
		crc.update(data);
		long checksum = crc.getValue();
		return ByteBuffer.allocate(4).putInt((int) checksum).array();
	}

	/**
	 * Verifies the CRC32 checksum for the given byte array.
	 *
	 * @param data the byte array containing the data
	 * @param checksum the expected checksum as a 4-byte array (big-endian)
	 * @return true if the checksum matches, false otherwise
	 */
	public static boolean verifyChecksum(byte[] data, byte[] checksum) {
		byte[] actual = calculateChecksum(data);
		for (int i = 0; i < 4; i++) {
			if (actual[i] != checksum[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Combines a checksum and data into a single byte array.
	 * Format: [4 bytes checksum][data...]
	 *
	 * @param data the data to prepend with checksum
	 * @return the combined byte array
	 */
	public static byte[] prependChecksum(byte[] data) {
		byte[] checksum = calculateChecksum(data);
		ByteBuffer buffer = ByteBuffer.allocate(checksum.length + data.length);
		buffer.put(checksum);
		buffer.put(data);
		return buffer.array();
	}

	/**
	 * Extracts the checksum and data from a combined byte array.
	 * Format: [4 bytes checksum][data...]
	 *
	 * @param combined the combined byte array
	 * @return a StateParts object containing checksum and data
	 */
	public static StateParts extractChecksumAndData(byte[] combined) {
		if (combined.length < 4) {
			throw new IllegalArgumentException("Combined array too short for checksum.");
		}
		byte[] checksum = new byte[4];
		System.arraycopy(combined, 0, checksum, 0, 4);
		byte[] data = new byte[combined.length - 4];
		System.arraycopy(combined, 4, data, 0, data.length);
		return new StateParts(checksum, data);
	}

	/**
	 * Helper class to hold checksum and data parts.
	 */
	public static class StateParts {
		public final byte[] checksum;
		public final byte[] data;

		public StateParts(byte[] checksum, byte[] data) {
			this.checksum = checksum;
			this.data = data;
		}
	}
}
