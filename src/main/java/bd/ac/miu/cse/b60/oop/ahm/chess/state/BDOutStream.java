package bd.ac.miu.cse.b60.oop.ahm.chess.state;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * Output stream for writing chess game state to a byte array.
 * Extends DataOutputStream and collects written data for serialization.
 */
public class BDOutStream extends DataOutputStream {

	/** The underlying byte array output stream. */
	private final ByteArrayOutputStream out;

	/**
	 * Constructs a new BDOutStream.
	 */
	public BDOutStream() {
		this(new ByteArrayOutputStream());
	}

	/**
	 * Constructs a BDOutStream with a given ByteArrayOutputStream.
	 * @param out the ByteArrayOutputStream to use
	 */
	private BDOutStream(ByteArrayOutputStream out) {
		super(out);
		this.out = out;
	}

	/**
	 * Collects the written data as a byte array.
	 * @return the byte array containing the written data
	 */
	public final byte[] collect() {
		return out.toByteArray();
	}
}
