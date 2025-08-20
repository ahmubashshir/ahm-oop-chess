package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * The {@code Piece} class represents a generic chess piece that acts as the base class of all other chess pieces.
 *
 */

public abstract class Piece
{

	private String name;
	private final Color color;
	private boolean isCaptured;

	protected Piece(String name, Color color)
	{
		this.color = color;
		this.name  = String.format("%s<%s>", name, color.tag);
	}

	public String getName()
	{
		return name;
	}

	public void setCaptured(boolean isCaptured)
	{
		this.isCaptured = isCaptured;
	}

	public boolean getCaptured()
	{
		return isCaptured;
	}

	public boolean getIsWhite()
	{
		return color == Color.WHITE;
	}

	/**
	 * Checks if a move from the source square to the destination square is a valid for this piece.
	 *
	 * @param sourceRow the row index of the source square.
	 * @param sourceCol the column index of the source square.
	 * @param destRow   the row index of the destination square.
	 * @param destCol   the column index of the destination square.
	 * @param board     the chessboard represented by a 2D array of {@code Square}s.
	 * @return {@code true} if the move is valid, {@code false} otherwise.
	 */
	public abstract boolean isValidMove(int sourceRow, int sourceCol, int destRow, int destCol, Square[][] board);
}
