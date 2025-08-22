package bd.ac.miu.cse.b60.oop.ahm.chess;

import java.time.LocalTime;
import bd.ac.miu.cse.b60.oop.ahm.chess.piece.*;

/**
 * Represents a chess game with a board, players, and methods for moves.
 */
public class Game {

	/**
	 * Default width of the game board.
	 */
	public static int DEFAULT_BOARD_WIDTH = 8;

	/**
	 * Default height of the game board.
	 */
	public static int DEFAULT_BOARD_HEIGHT = 8;

	/**
	 * The game board as a 2D array of squares.
	 */
	private Square[][] board;

	/**
	 * Array of players in the game.
	 */
	private Player[] players;

	/**
	 * The current player making a move.
	 */
	private Player currentPlayer;

	/**
	 * The display for the game.
	 */
	private Display display;

	/**
	 * Prints the captured pieces for each player.
	 */
	public void printCapturedPieces() {
		display.printCapturedPieces(getPlayerArray());
	}

	/**
	 * Prints the current state of the board.
	 */
	public void printBoard() {
		display.printBoard(board);
	}

	/**
	 * Performs castling.
	 *
	 * @param kingsrc Column of the king's source square.
	 * @param kingdst Column of the king's destination square.
	 * @param rooksrc Column of the rook's source square.
	 * @param rookdst Column of the rook's destination square.
	 * @param row	 Row of the castling move.
	 */
	private void performCastling(int kingsrc, int kingdst, int rooksrc, int rookdst, int row) {
		Square kingSourceSquare = board[row][kingsrc];
		Square kingDestSquare = board[row][kingdst];
		Square rookSourceSquare = board[row][rooksrc];
		Square rookDestSquare = board[row][rookdst];

		// Move the king for castling
		kingDestSquare.setPiece(kingSourceSquare.getPiece());
		kingSourceSquare.setPiece(null);

		// Move the rook for castling
		rookDestSquare.setPiece(rookSourceSquare.getPiece());
		rookSourceSquare.setPiece(null);
	}

	/**
	 * Checks if the path is clear between two squares horizontally or vertically.
	 *
	 * @param src Source square coordinates.
	 * @param dst Destination square coordinates.
	 * @return {@code true} if the path is clear, {@code false} otherwise.
	 */
	private boolean isPathClear(Coord src, Coord dst) {
		if (src.row == dst.row) {
			// Check for a clear path horizontally
			int minCol = Math.min(src.col, dst.col);
			int maxCol = Math.max(src.col, dst.col);

			for (int col = minCol + 1; col < maxCol; col++) {
				if (board[src.row][col].getPiece() != null) {
					// There is a piece in the horizontal path
					return false;
				}
			}
		} else if (src.col == dst.col) {
			// Check for a clear path vertically
			int minRow = Math.min(src.row, dst.row);
			int maxRow = Math.max(src.row, dst.row);

			for (int row = minRow + 1; row < maxRow; row++) {
				if (board[row][src.col].getPiece() != null) {
					// There is a piece in the vertical path
					return false;
				}
			}
		}

		// The path is clear
		return true;
	}

	/**
	 * Initializes a new game with a time limit.
	 *
	 * @param timeLimit The time limit for each player.
	 */
	public Game(LocalTime timeLimit) {
		this(timeLimit, new Display());
	}

	/**
	 * Initializes a new game with a time limit.
	 *
	 * @param timeLimit The time limit for each player.
	 * @param display	Display Manager Instance.
	 */
	public Game(LocalTime timeLimit, Display display) {
		board = new Square[DEFAULT_BOARD_WIDTH][DEFAULT_BOARD_HEIGHT];
		this.display = display;
		// Initialize each Square object in the board array
		for (int i = 0; i < DEFAULT_BOARD_WIDTH; i++) {
			for (int j = 0; j < DEFAULT_BOARD_HEIGHT; j++) {
				board[i][j] = new Square();
			}
		}
		// Initialize players array
		players = new Player[2];
		players[0] = new Player(0, timeLimit);
		players[1] = new Player(1, timeLimit);
		setCurrentPlayer(0);
	}

	/**
	 * Gets the current game board.
	 *
	 * @return The current game board as a 2D array.
	 */
	public Square[][] getBoard() {
		return board;
	}

	/**
	 * Initializes the positions of chess pieces on the board.
	 */
	public void initializePiecePositions() {
		// Initialize pieces for the white player
		board[0][0].setPiece(new Rook(Color.WHITE));
		board[0][1].setPiece(new Knight(Color.WHITE));
		board[0][2].setPiece(new Bishop(Color.WHITE));
		board[0][3].setPiece(new Queen(Color.WHITE));
		board[0][4].setPiece(new King(Color.WHITE));
		board[0][5].setPiece(new Bishop(Color.WHITE));
		board[0][6].setPiece(new Knight(Color.WHITE));
		board[0][7].setPiece(new Rook(Color.WHITE));

		for (int i = 0; i < DEFAULT_BOARD_WIDTH; i++) {
			board[1][i].setPiece(new Pawn(Color.WHITE));
		}

		// Initialize pieces for the black player
		board[7][0].setPiece(new Rook(Color.BLACK));
		board[7][1].setPiece(new Knight(Color.BLACK));
		board[7][2].setPiece(new Bishop(Color.BLACK));
		board[7][3].setPiece(new Queen(Color.BLACK));
		board[7][4].setPiece(new King(Color.BLACK));
		board[7][5].setPiece(new Bishop(Color.BLACK));
		board[7][6].setPiece(new Knight(Color.BLACK));
		board[7][7].setPiece(new Rook(Color.BLACK));

		for (int i = 0; i < DEFAULT_BOARD_WIDTH; i++) {
			board[6][i].setPiece(new Pawn(Color.BLACK));
		}

		// Initialize the rest of the board with empty squares
		for (int i = 2; i < 6; i++) {
			for (int j = 0; j < DEFAULT_BOARD_WIDTH; j++) {
				board[i][j] = new Square();
			}
		}
	}

	/**
	 * Sets the current player by ID.
	 *
	 * @param playerID The ID of the player to set as current.
	 */
	public void setCurrentPlayer(int playerID) {
		currentPlayer = players[playerID];
	}

	/**
	 * Gets the current player.
	 *
	 * @return The current player.
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Gets a player by ID.
	 *
	 * @param playerID The ID of the player.
	 * @return The player with the specified ID.
	 */
	public Player getPlayer(int playerID) {
		return players[playerID];
	}

	/**
	 * Switches the current player.
	 */
	public void switchPlayer() {
		if (currentPlayer.getPlayerID() == 0) {
			this.currentPlayer = players[1];
		} else {
			this.currentPlayer = players[0];
		}
	}

	/**
	 * Moves a piece from source to destination.
	 *
	 * @param src Source square coordinates.
	 * @param dst Destination square coordinates.
	 * @return {@code MoveStatus} indicating the result of the move.
	 */
	public MoveStatus move(Coord src, Coord dst) {
		Square sourceSquare = board[src.row][src.col];
		Square destSquare = board[dst.row][dst.col];

		Piece pieceToMove = sourceSquare.getPiece();

		if (pieceToMove == null)
			return MoveStatus.SourceError;

		// Check if player is moving their own piece
		if (!(currentPlayer.getPlayerID() == 0 && pieceToMove.getIsWhite() ||
		        currentPlayer.getPlayerID() == 1 && !pieceToMove.getIsWhite()))
			return MoveStatus.PlayerError;

		// Check if the move is valid for the specific piece
		if (pieceToMove.isValidMove(src, dst, board)) {
			// Check if the destination square is empty or has an opponent's piece
			if (destSquare.getPiece() == null || destSquare.getPiece().getIsWhite() != pieceToMove.getIsWhite()) {
				// Capture the opponent’s piece if it exists
				if (destSquare.getPiece() != null) {
					destSquare.getPiece().setCaptured(true);
					currentPlayer.capturePiece(destSquare.getPiece());
				}

				// Check for castling (king moving two squares horizontally)
				if (pieceToMove instanceof King && Math.abs(dst.col - src.col) == 2) {
					// Ensure king is in starting position and correct player
					boolean isWhite = pieceToMove.getIsWhite();
					if ((isWhite && src.row == 0 && src.col == 4 && currentPlayer.getPlayerID() == 0) ||
					        (!isWhite && src.row == 7 && src.col == 4 && currentPlayer.getPlayerID() == 1)) {
						// King:Queen side castling
						int rookSrcCol = (dst.col - src.col == 2) ? 7 : 0;
						int rookDstCol = (dst.col - src.col == 2) ? 5 : 3;
						performCastling(src.col, dst.col, rookSrcCol, rookDstCol, src.row);
						return MoveStatus.Ok;
					}
					return MoveStatus.CastleError;
				}

				// Check for a clear path for non-castling moves
				if (isPathClear(src, dst)) {
					// Move the piece to the destination square
					destSquare.setPiece(pieceToMove);
					sourceSquare.setPiece(null);
					return MoveStatus.Ok;
				}
				return MoveStatus.PathError;
			}
			return MoveStatus.DestError;
		}
		return MoveStatus.MoveError;
	}

	/**
	 * Checks if the current player's king is in check.
	 *
	 * @return {@code true} if the king is in check, {@code false} otherwise.
	 */
	public boolean isCheck() {
		boolean currentKingColor;
		int currentKingRow = 0;
		int currentKingCol = 0;
		Piece pieceToCheck;

		if (currentPlayer == players[0]) {
			currentKingColor = true; // Set current king color to white if first player's turn
		} else {
			currentKingColor = false; // Set current king color to black if second player's turn
		}

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] != null && board[row][col].getPiece() != null) { // Check if block is empty
					if (board[row][col].getPiece() instanceof King) { // Check if block is king
						if (board[row][col].getPiece().getIsWhite() == currentKingColor) { // Check if king is same color as
							// current player
							currentKingRow = row;
							currentKingCol = col;
						}
					}
				}
			}
		}
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] != null && board[row][col].getPiece() != null) { // Check if block is empty
					if ((board[row][col].getPiece().getIsWhite() != board[currentKingRow][currentKingCol].getPiece()
					        .getIsWhite())) {
						pieceToCheck = board[row][col].getPiece();
						if (pieceToCheck.isValidMove(row, col, currentKingRow, currentKingCol, board)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the current player's king is alive.
	 *
	 * @return {@code true} if the king is alive, {@code false} otherwise.
	 */
	public boolean isKingAlive() {
		boolean currentKingColor;

		if (currentPlayer == players[0])
			currentKingColor = true; // Set current king color to white if first player's turn
		else
			currentKingColor = false; // Set current king color to black if second player's turn

		for (int row = 0; row < board.length; row++)
			for (int col = 0; col < board[row].length; col++)
				if (board[row][col] != null && board[row][col].getPiece() != null)   // Check if block is empty
					if (board[row][col].getPiece() instanceof King)   // Check if block is king
						if (board[row][col].getPiece().getIsWhite() == currentKingColor)   // Check if king is same color as
							return true;
		return false;
	}

	/**
	 * Checks if the game is a draw based on the number of turns.
	 *
	 * @return {@code true} if the game is a draw, {@code false} otherwise.
	 */
	public boolean isDraw() {
		if (players[0].getNumOfTurns() >= players[0].getMaxNumOfTurns()
		        && players[1].getNumOfTurns() >= players[1].getMaxNumOfTurns()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the maximum number of turns for the game.
	 *
	 * @param num The maximum number of turns.
	 */
	public void setMaxNumOfTurns(int num) {
		players[0].setMaxNumOfTurns(num);
		players[1].setMaxNumOfTurns(num);
	}

	/**
	 * Gets the number of turns a player has completed.
	 *
	 * @param player The player whose turns are counted.
	 * @return The number of turns completed by the player.
	 */
	public int getNumOfTurns(Player player) {
		return player.getNumOfTurns();
	}

	/**
	 * Sets the number of turns for a player.
	 *
	 * @param player The player whose turns are set.
	 * @param num	The number of turns toset.
	 */
	public void setNumOfTurns(Player player, int num) {
		player.setNumOfTurns(getNumOfTurns(player) + num);
	}

	/**
	 * Checks if the time limit for the current player is finished.
	 *
	 * @return {@code true} if the time is finished, {@code false} otherwise.
	 */
	public boolean isTimeFinished() {
		return getCurrentPlayer().getIsTimeFinished();
	}

	/**
	 * Gets the array of players.
	 *
	 * @return The array of players.
	 */
	public Player[] getPlayerArray() {
		return players;
	}

	/**
	 * Ends the current game by stopping timers for all players.
	 */
	public void end() {
		for(int i = 0; i < players.length; i++)
			players[i].stopTimer();
	}
}
