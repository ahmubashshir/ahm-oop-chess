package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Represents a coordinate on the chessboard.
 * Each coordinate consists of a {@code row} and a {@code col} value.
 */
public class Coord {
	/** The column of the coordinate, in the range {@code 0-7}. */
	public final int col;

	/** The row of the coordinate, in the range {@code 0-7}. */
	public final int row;

	/**
	 * Constructs a {@code Coord} with the specified column and row.
	 *
	 * @param col the column index, must be between {@code 0} and {@code 7}.
	 * @param row the row index, must be between {@code 0} and {@code 7}.
	 * @throws IllegalArgumentException if {@code col} or {@code row} is outside the valid range.
	 */
	public Coord(int col, int row) {
		if (col > 7 || row > 7 || col < 0 || row < 0)
			throw new IllegalArgumentException("Coordinate not valid!");
		this.col = col;
		this.row = row;
	}
}
