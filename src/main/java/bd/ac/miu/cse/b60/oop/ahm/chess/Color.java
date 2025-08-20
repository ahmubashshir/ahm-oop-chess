package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * Enum representing the color of chess pieces.
 */
public enum Color
{
	WHITE("W"),
	BLACK("B"),
	;

	/** The single-character tag representing the color. */
	public final String tag;

	/**
	 * Constructs a {@code Color} with the specified tag.
	 *
	 * @param tag the single-character tag for the color (e.g., {@code "W"} for white)
	 */
	private Color(String tag)
	{
		this.tag = tag;
	}
}
