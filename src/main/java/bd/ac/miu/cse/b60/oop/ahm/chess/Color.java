package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Enum representing the {@code Color} of chess pieces.
 */
public enum Color {
	/** White color, represented by {@code "W"}. */
	WHITE("W"),
	/** Black color, represented by {@code "B"}. */
	BLACK("B");

	/** The single-character tag representing the {@code Color}. */
	public final String tag;

	/**
	 * Constructs a {@code Color} with the specified tag.
	 *
	 * @param tag The single-character tag for the {@code Color} (e.g., {@code "W"} for {@code WHITE})
	 */
	private Color(String tag) {
		this.tag = tag;
	}
}
