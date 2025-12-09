package bd.ac.miu.cse.b60.oop.ahm.chess.state;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class BDInStream extends DataInputStream {

	public BDInStream(byte... data) {
		super(new ByteArrayInputStream(data));
	}
}
