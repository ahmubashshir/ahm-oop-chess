package bd.ac.miu.cse.b60.oop.ahm.chess.piece;

import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;

/**
 * Enum representing the types of chess pieces.
 * Each type is associated with its class and a unique tag.
 */
public enum Type {
	/** King piece type. */
	KING(King.class),
	/** Queen piece type. */
	QUEEN(Queen.class),
	/** Rook piece type. */
	ROOK(Rook.class),
	/** Bishop piece type. */
	BISHOP(Bishop.class),
	/** Knight piece type. */
	KNIGHT(Knight.class),
	/** Pawn piece type. */
	PAWN(Pawn.class);

	/** Unique tag for this piece type. */
	public final byte tag;
	/** Class representing this piece type. */
	public final Class<?> type;

	/**
	 * Constructs a Type enum value with the associated piece class.
	 * @param type the class representing the piece type
	 */
	Type(Class<? extends Piece> type) {
		this.tag = (byte) ordinal();
		this.type = type;
	}

	/**
	 * Returns the Type corresponding to the given piece class.
	 * @param type the class representing the piece type
	 * @return the Type enum value for the class
	 * @throws IllegalArgumentException if the class does not match any Type
	 */
	public static Type fromType(Class<? extends Piece> type) {
		for (Type typeEnum : values()) {
			if (typeEnum.type == type) {
				return typeEnum;
			}
		}
		throw new IllegalArgumentException("Invalid type");
	}

	/**
	 * Returns the Type corresponding to the given tag.
	 * @param tag the tag representing the piece type
	 * @return the Type enum value for the tag
	 * @throws IllegalArgumentException if the tag does not match any Type
	 */
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
