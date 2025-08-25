package bd.ac.miu.cse.b60.oop.ahm.chess;

/**
 * The {@code Display} abstract class is responsible for displaying the current state of
 * the chessboard and the captured pieces of each player to the display device.
 *
 * <p>This class defines the interface for rendering the chess game state.
 * Different implementations can provide various user interfaces (e.g., command line, GUI).</p>
 *
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.display.CLIDisplay
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Square
 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Player
 */
public interface Display {
	/**
	 * Prints the current state of the chessboard to the display device.
	 *
	 * @param board a 2D array of {@code Square} representing the chessboard.
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Square
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Piece
	 */
	public void printBoard(Square[][] board);

	/**
	 * Prints the captured pieces for each player.
	 *
	 * @param players an array of {@code Player} objects representing the players in the game.
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Player
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Piece#getCaptured()
	 */
	public void printCapturedPieces(Player[] players);

	/**
	 * Asks Player to enter co-ordinate for an action
	 *
	 * <p>This method gets input coordinates for piece movement or selection.
	 * It can be implemented as a no-op in event-driven UI implementations.</p>
	 *
	 * @param query info about the wanted co-ordinate.
	 * @return a {@code Coord} object containing the column and row, or {@code null}
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Coord
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.Game#move
	 */
	public Coord getCoord(String query);

	/**
	 * Displays the main menu and reads the user's choice.
	 *
	 * <p>This method renders the game's main menu and handles user selection.
	 * It can be implemented as a no-op in event-driven UI implementations.</p>
	 *
	 * @return the {@code MenuResult} corresponding to the user's menu selection
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.MenuResult
	 * @see bd.ac.miu.cse.b60.oop.ahm.Chess
	 */
	public MenuResult mainMenu();
}
