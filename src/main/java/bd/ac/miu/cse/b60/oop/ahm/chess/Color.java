package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Enum representing the {@code Color} of chess pieces.
 */
public enum Color {
	/** White color, represented by {@code "W" / 0}. */
	WHITE("White", 0),
	/** Black color, represented by {@code "B" / 1}. */
	BLACK("Black", 1);

	/** The single-character tag representing the {@code Color}. */
	public final String tag;

	/** The color name for the {@code Color}. */
	public final String name;

	/** The integer player id for {@code Color}. */
	public final int id;

	/**
	 * Constructs a {@code Color} with the specified tag and id.
	 *
	 * @param name The color name (e.g., "White" or "Black")
	 * @param id The integer player id (0 for white, 1 for black)
	 */
	private Color(String name, int id) {
		this.name = name;
		this.tag = String.valueOf(name.charAt(0));
		this.id = id;
	}
}
