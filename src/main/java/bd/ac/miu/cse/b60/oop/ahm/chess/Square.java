package bd.ac.miu.cse.b60.oop.ahm.chess;

import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;

/**
 * The {@code Square} class represents a square on a game board.
 * Each square may contain a {@link Piece}.
 *
 */
public class Square
{
	private Piece piece;

	/**
	* Sets the piece on this square.
	*
	* @param piece The piece to be placed on the square.
	*/
	public void setPiece(Piece piece)
	{
		this.piece = piece;
	}

	/**
	* Gets the piece currently on this square.
	*
	* @return The piece on the square, or {@code null} if no piece is present.
	*/
	public Piece getPiece()
	{
		return piece;
	}

}
