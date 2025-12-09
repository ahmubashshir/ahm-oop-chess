package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;

public enum Type {
	KING(King.class),
	QUEEN(Queen.class),
	ROOK(Rook.class),
	BISHOP(Bishop.class),
	KNIGHT(Knight.class),
	PAWN(Pawn.class);

	public final byte tag;
	public final Class<?> type;

	Type(Class<? extends Piece> type) {
		this.tag = (byte) ordinal();
		this.type = type;
	}

	public static Type fromType(Class<? extends Piece> type) {
		for (Type typeEnum : values()) {
			if (typeEnum.type == type) {
				return typeEnum;
			}
		}
		throw new IllegalArgumentException("Invalid type");
	}

	public static Type fromTag(byte tag) {
		for (Type typeEnum : values()) {
			if (typeEnum.tag == tag) {
				return typeEnum;
			}
		}
		throw new IllegalArgumentException(
		    String.format("Invalid tag: %d", tag)
		);
	}
}
