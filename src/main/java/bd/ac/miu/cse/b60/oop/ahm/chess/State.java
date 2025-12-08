package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Enum representing the status of a chess move attempt.
 * This is used to track the current state of the game or menu selection.
 */
public enum State {
	/** Indicates nothing was selected. */
	NONE,

	/** Indicates Start game entry was selected. */
	START,

	/** Indicates User Requested game session end. */
	END,

	/** Indicates Exit entry was selected. */
	EXIT;

	/**
	 * Constructs a {@code State} from menu id.
	 *
	 * @param id menu index.
	 * @return {@code State}
	 * @throws IllegalArgumentException {@code id} is outside the {@code State}.
	 */
	public static State fromInt(int id) {
		switch (id) {
		case 1:
			return START;
		case 2:
			return EXIT;
		default:
			return NONE;
		}
	}
}
