package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Enum representing the status of a chess move attempt.
 */
public enum MoveStatus {
	/** Indicates a valid move. */
	Ok("Ok"),

	/** Indicates the selected piece does not belong to the player. */
	PlayerError("Not your piece"),

	/** Indicates the path between source and destination is obstructed. */
	PathError("Path is not clear"),

	/** Indicates no piece exists at the source position. */
	SourceError("No piece at source"),

	/** Indicates the move is invalid for the selected piece. */
	MoveError("Invalid move for this piece"),

	/** Indicates an invalid castling attempt. */
	CastleError("Invalid castling movement"),

	/** Indicates the destination contains a piece of the same color. */
	DestError("Can't capture your own piece"),
	;

	/** The descriptive message associated with the move status. */
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
