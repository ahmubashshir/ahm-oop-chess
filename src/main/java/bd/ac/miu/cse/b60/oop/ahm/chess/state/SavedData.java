package bd.ac.miu.cse.b60.oop.ahm.chess.state;

public final class SavedData extends Serializer {

	protected SavedData(byte... data) {
		super(data);
	}

	public byte[] bytes() {
		byte[] ret = new byte[checksum.length + data.length];
		System.arraycopy(checksum, 0, ret, 0, checksum.length);
		System.arraycopy(data, 0, ret, checksum.length, data.length);
		return ret;
	}

	public SaveData toSaveData() {
		return SaveData.load(bytes());
	}

	public static final SavedData create(byte... data) {
		return new SavedData(data);
	}
}
