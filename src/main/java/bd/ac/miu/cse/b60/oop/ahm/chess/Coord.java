package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Represents a coordinate (row and column) on the chessboard.
 * <p>
 * Coordinates are zero-based, where both row and column must be in the range 0-7.
 * This class provides constructors for both integer and character-based chess notation.
 * </p>
 */
public class Coord {

	/** The column of the coordinate, in the range {@code 0-7}. */
	public final int col;

	/** The row of the coordinate, in the range {@code 0-7}. */
	public final int row;

	/**
	 * Constructs a {@code Coord} with the specified zero-based column and row.
	 *
	 * @param col the column index (0-7, where 0 = 'a')
	 * @param row the row index (0-7, where 0 = '1')
	 * @throws IllegalArgumentException if {@code col} or {@code row} is outside the valid range
	 */
	public Coord(int col, int row) {
		if (
		    col > 7 || row > 7 || col < 0 || row < 0
		) throw new IllegalArgumentException(
			    "Coordinate not valid! Must be in range 0-7."
			);
		this.col = col;
		this.row = row;
	}

	/**
	 * Constructs a {@code Coord} from chessboard notation (e.g., 'a'-'h' for columns, '1'-'8' for rows).
	 *
	 * @param col the column character ('a'-'h' or 'A'-'H')
	 * @param row the row character ('1'-'8')
	 * @throws IllegalArgumentException if {@code col} or {@code row} is outside the valid range
	 */
	public Coord(char col, char row) {
		// Validate input chars to ensure only valid chessboard coordinates are accepted.
		if (
		    !(((col <= 'h' && col >= 'a') || (col <= 'H' && col >= 'A')) &&
		      (row <= '8' && row >= '1'))
		) throw new IllegalArgumentException(
			    "Coordinate not valid! Must be in range a-h and 1-8."
			);
		this.col = col - ((col >= 'A' && col <= 'H') ? 'A' : 'a');
		this.row = row - '1';
	}
}
