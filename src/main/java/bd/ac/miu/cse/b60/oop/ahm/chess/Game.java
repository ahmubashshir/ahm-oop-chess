package bd.ac.miu.cse.b60.oop.ahm.chess;

import java.time.LocalTime;
import bd.ac.miu.cse.b60.oop.ahm.chess.piece.*;

/**
 * The `Game` class represents a chess game with a game board,
 * players, and methods for performing specific chess moves.
 */
public class Game
{
	/**
	 * The default width of the game board.
	 */
	public static int DEFAULT_BOARD_WIDTH = 8;

	/**
	 * The default height of the game board.
	 */
	public static int DEFAULT_BOARD_HEIGHT = 8;

	/**
	 * Represents the game board as a two-dimensional array of squares.
	 */
	private Square[][] board;

	/**
	 * Represents the array of players participating in the game.
	 */
	private Player[] players;

	/**
	 * Represents the current player making a move.
	 */
	private Player currentPlayer;

	/**
	 * Represents the current display making a move.
	 */
	private Display display;

	public void printCapturedPieces()
	{
		display.printCapturedPieces(getPlayerArray());
	}

	public void printBoard()
	{
		display.printBoard(board);
	}

	/**
	 * Private method to perform King-side castling.
	 *
	 * @param kingsrc The column of the king's source square.
	 * @param kingdst   The column of the king's destination square.
	 * @param rooksrc The column of the rook's source square.
	 * @param rookdst   The column of the rook's destination square.
	 * @param row           The row of the castling move.
	 */
	private void performCastling(int kingsrc, int kingdst, int rooksrc, int rookdst,
	                             int row)
	{
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
	 * Private helper method to check if the path is clear between two squares
	 * horizontally or vertically.
	 *
	 * @param src	The co-ordinate of the source square.
	 * @param dst	The co-ordinate of the destination square.
	 */
	private boolean isPathClear(Coord src, Coord dst)
	{
		if (src.row == dst.row)
			{
				// Check for a clear path horizontally
				int minCol = Math.min(src.col, dst.col);
				int maxCol = Math.max(src.col, dst.col);

				for (int col = minCol + 1; col < maxCol; col++)
					{
						if (board[src.row][col].getPiece() != null)
							{
								// There is a piece in the horizontal path
								return false;
							}
					}
			}
		else if (src.col == dst.col)
			{
				// Check for a clear path vertically
				int minRow = Math.min(src.row, dst.row);
				int maxRow = Math.max(src.row, dst.row);

				for (int row = minRow + 1; row < maxRow; row++)
					{
						if (board[row][src.col].getPiece() != null)
							{
								// There is a piece in the vertical path
								return false;
							}
					}
			}

		// The path is clear
		return true;
	}

	// -----------------PUBLIC METHODS/CONSTRUCTOR -----------------

	/**
	 * Default constructor for the `Game` class. Initializes the game board,
	 * players, and sets the current player to the first player.
	 */
	public Game(LocalTime timeLimit)
	{
		board = new Square[DEFAULT_BOARD_WIDTH][DEFAULT_BOARD_HEIGHT];
		display = new Display();
		// Initialize each Square object in the board array
		for (int i = 0; i < DEFAULT_BOARD_WIDTH; i++)
			{
				for (int j = 0; j < DEFAULT_BOARD_HEIGHT; j++)
					{
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
	 * Gets the current state of the game board.
	 *
	 * @return A two-dimensional array representing the current game board.
	 */
	public Square[][] getBoard()
	{
		return board;
	}

	/**
	 * Initializes the positions of chess pieces on the game board at the beginning
	 * of the game.
	 */
	public void initializePiecePositions()
	{
		// Initialize pieces for the white player
		board[0][0].setPiece(new Rook(Color.WHITE));
		board[0][1].setPiece(new Knight(Color.WHITE));
		board[0][2].setPiece(new Bishop(Color.WHITE));
		board[0][3].setPiece(new Queen(Color.WHITE));
		board[0][4].setPiece(new King(Color.WHITE));
		board[0][5].setPiece(new Bishop(Color.WHITE));
		board[0][6].setPiece(new Knight(Color.WHITE));
		board[0][7].setPiece(new Rook(Color.WHITE));

		for (int i = 0; i < DEFAULT_BOARD_WIDTH; i++)
			{
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

		for (int i = 0; i < DEFAULT_BOARD_WIDTH; i++)
			{
				board[6][i].setPiece(new Pawn(Color.BLACK));
			}

		// Initialize the rest of the board with empty squares
		for (int i = 2; i < 6; i++)
			{
				for (int j = 0; j < DEFAULT_BOARD_WIDTH; j++)
					{
						board[i][j] = new Square();
					}
			}
	}

	/**
	 * Sets the current player based on the provided player ID.
	 *
	 * @param playerID The ID of the player to set as the current player.
	 */
	public void setCurrentPlayer(int playerID)
	{
		currentPlayer = players[playerID];
	}

	/**
	 * Gets the current player making a move.
	 *
	 * @return The current player object.
	 */
	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}

	/**
	 * Gets the player object based on the provided player ID.
	 *
	 * @param playerID The ID of the player to retrieve.
	 * @return The player object corresponding to the provided player ID.
	 */
	public Player getPlayer(int playerID)
	{
		return players[playerID];
	}

	/**
	 * Switches the current player between players[0] and players[1].
	 */
	public void switchPlayer()
	{
		if (currentPlayer.getPlayerID() == 0)
			{
				this.currentPlayer = players[1];
			}
		else
			{
				this.currentPlayer = players[0];
			}
	}

	/**
	 * Moves a chess piece from the source square to the destination square on the
	 * game board.
	 * Utilizes movement rules of the pieces and handles various scenarios,
	 * returning
	 * specific codes to indicate the result of the move.
	 *
	 * @param src     The co-ordinate of the source square.
	 * @param dst     The co-ordinate of the destination square.
	 * @return {@code MoveStatus}
	 */
	public MoveStatus move(Coord src, Coord dst)
	{
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
		if (pieceToMove.isValidMove(src.row, src.col, dst.row, dst.col, board))
			{
				// Check if the destination square is empty or has an opponent's piece
				if (destSquare.getPiece() == null || destSquare.getPiece().getIsWhite() != pieceToMove.getIsWhite())
					{
						// Capture the opponent’s piece if it exists
						if (destSquare.getPiece() != null)
							{
								destSquare.getPiece().setCaptured(true);
								currentPlayer.capturePiece(destSquare.getPiece());
							}

						// Check for castling (king moving two squares horizontally)
						if (pieceToMove instanceof King && Math.abs(dst.col - src.col) == 2)
							{
								// Ensure king is in starting position and correct player
								boolean isWhite = pieceToMove.getIsWhite();
								if ((isWhite && src.row == 0 && src.col == 4 && currentPlayer.getPlayerID() == 0) ||
								        (!isWhite && src.row == 7 && src.col == 4 && currentPlayer.getPlayerID() == 1))
									{
										// King:Queen side castling
										int rookSrcCol = (dst.col - src.col == 2) ? 7 : 0;
										int rookDstCol = (dst.col - src.col == 2) ? 5 : 3;
										performCastling(src.col, dst.col, rookSrcCol, rookDstCol, src.row);
										return MoveStatus.Ok;
									}
								return MoveStatus.CastleError;
							}

						// Check for a clear path for non-castling moves
						if (isPathClear(new Coord(src.col, src.row), new Coord(dst.col, dst.row)))
							{
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
	 * Goes through every piece to check if it puts opponent king under threat
	 *
	 */
	public boolean isCheck()
	{
		boolean currentKingColor;
		int currentKingRow = 0;
		int currentKingCol = 0;
		Piece pieceToCheck;

		if (currentPlayer == players[0])
			{
				currentKingColor = true; // Set current king color to white if first player's turn
			}
		else
			{
				currentKingColor = false; // Set current king color to black if second player's turn
			}

		for (int row = 0; row < board.length; row++)
			{
				for (int col = 0; col < board[row].length; col++)
					{
						if (board[row][col] != null && board[row][col].getPiece() != null)   // Check if block is empty
							{
								if (board[row][col].getPiece() instanceof King)   // Check if block is king
									{
										if (board[row][col].getPiece().getIsWhite() == currentKingColor)   // Check if king is same color as
											{
												// current player
												currentKingRow = row;
												currentKingCol = col;
											}
									}
							}
					}
			}
		for (int row = 0; row < board.length; row++)
			{
				for (int col = 0; col < board[row].length; col++)
					{
						if (board[row][col] != null && board[row][col].getPiece() != null)   // Check if block is empty
							{
								if ((board[row][col].getPiece().getIsWhite() != board[currentKingRow][currentKingCol].getPiece()
								        .getIsWhite()))
									{
										pieceToCheck = board[row][col].getPiece();
										if (pieceToCheck.isValidMove(row, col, currentKingRow, currentKingCol, board))
											{
												return true;
											}
									}
							}
					}
			}
		return false;
	}

	/**
	 * Checks whether the king is alive or captured // on the board.
	 *
	 * @return boolean Whether the king is on the board or has been captured.
	 *
	 */
	public boolean isKingAlive()
	{
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
	 * Compares the number of current turns to the user-set limit of turns.
	 *
	 * @return boolean whether the number of turns exceeds limit.
	 *
	 */
	public boolean isDraw()
	{
		if (players[0].getNumOfTurns() >= players[0].getMaxNumOfTurns()
		        && players[1].getNumOfTurns() >= players[1].getMaxNumOfTurns())
			{
				return true;
			}
		else
			{
				return false;
			}
	}

	/**
	 * Set upper limit of turns.
	 *
	 * @param num The limit input by the user.
	 *
	 */
	public void setMaxNumOfTurns(int num)
	{
		players[0].setMaxNumOfTurns(num);
		players[1].setMaxNumOfTurns(num);
	}

	/**
	 * Gets the number of turns a player has completed
	 *
	 * @return number of turns.
	 *
	 */
	public int getNumOfTurns(Player player)
	{
		return player.getNumOfTurns();
	}

	/**
	 * Set the number of terms per specified playerID.
	 *
	 * @param player The ID of the player to set.
	 * @param num    The number to increase turns by.
	 *
	 */
	public void setNumOfTurns(Player player, int num)
	{
		player.setNumOfTurns(getNumOfTurns(player) + num);
	}

	/**
	 * Checks whether the time has been completed.
	 *
	 * @return method from Player that compares each players elapsed time.
	 *
	 */
	public boolean isTimeFinished()
	{
		return getCurrentPlayer().getIsTimeFinished();
	}

	/**
	 * Gets the player array to use in main to display captured pieces.
	 *
	 * @return player array.
	 *
	 */
	public Player[] getPlayerArray()
	{
		return players;
	}

	/**
	 * Ends current game.
	 *
	 */
	public void end()
	{
		for(int i = 0; i < players.length; i++)
			players[i].stopTimer();
	}
}
