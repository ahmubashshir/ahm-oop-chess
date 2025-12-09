package bd.ac.miu.cse.b60.oop.ahm.chess.state;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class BDOutStream extends DataOutputStream {

	private final ByteArrayOutputStream out;

	public BDOutStream() {
		this(new ByteArrayOutputStream());
	}

	private BDOutStream(ByteArrayOutputStream out) {
		super(out);
		this.out = out;
	}

	public final byte[] collect() {
		return out.toByteArray();
	}
}
