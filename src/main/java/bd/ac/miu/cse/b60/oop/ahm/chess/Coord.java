package bd.ac.miu.cse.b60.oop.ahm.chess;

public class Coord
{
	public final int col;
	public final int row;

	public Coord(int col, int row)
	{
		if(col > 7 || row > 7 || col < 0 || row < 0)
			throw new IllegalArgumentException("Coordinate not valid!");
		this.col = col;
		this.row = row;
	}
}
