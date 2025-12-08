package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Represents the current state of the chess game or menu selection.
 * <p>
 * This enum is used to track the user's navigation or game session status,
 * such as starting a game, ending a session, or exiting the application.
 * </p>
 */
public enum State {
	/** No selection has been made. */
	NONE,

	/** The user selected to start a new game. */
	START,

	/** The user requested to end the current game session. */
	END,

	/** The user selected to exit the application. */
	EXIT;

	/**
	 * Returns the {@code State} corresponding to a menu index.
	 *
	 * @param id the menu index (1 for START, 2 for EXIT, others for NONE)
	 * @return the corresponding {@code State}
	 * @throws IllegalArgumentException if {@code id} is outside the valid range
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
