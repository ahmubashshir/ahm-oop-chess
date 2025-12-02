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
	 * Constructs a {@code Color} with the specified tag.
	 *
	 * @param tag The single-character tag for the {@code Color} (e.g., {@code "W"} for {@code WHITE})
	 */
	private Color(String name, int id) {
		this.name = name;
		this.tag = String.valueOf(name.charAt(0));
		this.id = id;
	}
}
