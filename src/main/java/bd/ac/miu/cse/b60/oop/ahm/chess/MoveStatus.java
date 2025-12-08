package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Represents the result of a chess move attempt.
 * <p>
 * This enum provides detailed status codes for move validation, including
 * successful moves, rule violations, and error conditions.
 * </p>
 */
public enum MoveStatus {
	/** The move is valid and was executed successfully. */
	Ok("Ok"),

	/** The selected piece does not belong to the current player. */
	PlayerError("Not your piece"),

	/** The path between source and destination is obstructed by another piece. */
	PathError("Path is not clear"),

	/** No piece exists at the source position. */
	SourceError("No piece at source"),

	/** The move is not valid for the selected piece according to chess rules. */
	MoveError("Invalid move for this piece"),

	/** The castling move is invalid due to rule violation or board state. */
	CastleError("Invalid castling movement"),

	/** The destination square contains a piece of the same color (cannot capture own piece). */
	DestError("Can't capture your own piece");

	/**
	 * A human-readable message describing the move status.
	 */
	public final String info;

	/**
	 * Constructs a {@code MoveStatus} with the specified descriptive message.
	 *
	 * @param info the message describing the move status
	 */
	private MoveStatus(String info) {
		this.info = info;
	}
}
