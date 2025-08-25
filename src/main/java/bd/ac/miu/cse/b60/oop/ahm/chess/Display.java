package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * The {@code Display} class is responsible for displaying the current state of
 * the chessboard and the captured pieces of each player to the display device.
 */
public abstract class Display {

	/** Default Constructor */
	public Display() {}

	/**
	 * Prints the current state of the chessboard to the display device.
	 *
	 * @param board a 2D array of {@code Square} representing the chessboard.
	 */
	public abstract void printBoard(Square[][] board);

	/**
	 * Prints the captured pieces for each player.
	 *
	 * @param players an array of {@code Player} objects representing the players in the game.
	 */
	public abstract void printCapturedPieces(Player[] players);

	/**
	 * Asks Player to enter co-ordinate for an action
	 *
	 * can be no-op in event driven ui.
	 *
	 * @param query info about the wanted co-ordinate.
	 * @return a {@code Coord} object containing the column and row, or {@code null}
	 */
	public abstract Coord getCoord(String query);

	/**
	 * Displays the main menu and reads the user's choice.
	 *
	 * can be no-op in event driven ui.
	 *
	 * @return the {@code MenuResult} corresponding to the user's menu selection
	 */
	public abstract MenuResult mainMenu();
}
