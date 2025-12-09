package bd.ac.miu.cse.b60.oop.ahm.chess.state;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/**
 * Input stream for reading chess game state from a byte array.
 * Extends DataInputStream for deserialization purposes.
 */
public class BDInStream extends DataInputStream {

	/**
	 * Constructs a new BDInStream from the given byte array.
	 * @param data the byte array to read from
	 */
	public BDInStream(byte... data) {
		super(new ByteArrayInputStream(data));
	}
}
