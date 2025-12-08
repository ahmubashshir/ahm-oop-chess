package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Represents the color of a chess piece or player.
 * <p>
 * This enum defines the two possible colors in chess: white and black.
 * Each color has a name, a single-character tag, and an integer player ID.
 * </p>
 */
public enum Color {
	/** White color, represented by {@code "W"} and player ID {@code 0}. */
	WHITE("White", 0),
	/** Black color, represented by {@code "B"} and player ID {@code 1}. */
	BLACK("Black", 1);

	/** The single-character tag representing the color ("W" or "B"). */
	public final String tag;

	/** The full name of the color ("White" or "Black"). */
	public final String name;

	/** The integer player ID for this color (0 for white, 1 for black). */
	public final int id;

	/**
	 * Constructs a {@code Color} with the specified name and player ID.
	 *
	 * @param name the color name (e.g., "White" or "Black")
	 * @param id   the integer player ID (0 for white, 1 for black)
	 */
	private Color(String name, int id) {
		this.name = name;
		this.tag = String.valueOf(name.charAt(0));
		this.id = id;
	}
}
