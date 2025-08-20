package bd.ac.miu.cse.b60.oop.ahm.chess;
/**
 * The {@code MoveStatus} class represents a generic chess piece that acts as the base class of all other chess pieces.
 *         // Utilizes Movement of the pieces
 *         // Returns -1 if the destination is of the same color
 *         // Returns -2 for invalid combination of movement
 *         // Returns -3 for unclear path
 *         // Returns -4 for invalid castling movement
 *         // Returns -5 for invalid color piece to choose
 *         // Returns 1 for valid movement
 */

public enum MoveStatus
{
	Ok("Ok"), // 1
	PlayerError("Not your piece"),
	PathError("Path is not clear"),
	SourceError("No piece at source"),
	MoveError("Invalid move for this piece"),
	CastleError("Invalid castling movement"),
	DestError("Can't capture your own piece"),
	;

	public final String info;

	private MoveStatus(String info)
	{
		this.info = info;
	}
};
