package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Enum representing the status of a chess move attempt.
 */
public enum MenuResult {
	/** Indicates nothing was selected. */
	NONE,

	/** Indicates Start game entry was selected. */
	START,

	/** Indicates Exit entry was selected. */
	EXIT;

	/**
	 * Constructs a {@code MenuResult} from menu id.
	 *
	 * @param id menu index.
	 * @throws IllegalArgumentException {@code id} is outside the {@code MenuResult}.
	 */
	public static MenuResult fromInt(int id) {
		switch(id) {
		case 1:
			return START;
		case 2:
			return EXIT;
		default:
			return NONE;
		}
	}
}
