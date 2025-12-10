package bd.ac.miu.cse.b60.oop.ahm.chess;

import bd.ac.miu.cse.b60.oop.ahm.chess.piece.*;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.BDInStream;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.BDOutStream;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.Loadable;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SaveFrame;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SavePayload;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.Saveable;
import java.time.LocalTime;

/**
 * Central controller for a chess game, managing the board, players, moves, and game state.
 * <p>
 * The {@code Game} class is responsible for initializing the board, handling player turns,
 * validating and executing moves, enforcing chess rules (including castling and check),
 * and managing timers and draw conditions.
 * </p>
 *
 * @see Player
 * @see Square
 * @see Piece
 */
public class Game implements Saveable, Loadable {

	/** Default width of the chess board (number of columns). */
	public static int DEFAULT_BOARD_WIDTH = 8;

	/** Default height of the chess board (number of rows). */
	public static int DEFAULT_BOARD_HEIGHT = 8;

	/** The chess board, represented as a 2D array of squares. */
	private Square[][] board;

	/** The two players participating in the game. */
	private Player[] players;

	/** The player whose turn it is to move. */
	private Player currentPlayer;

	/**
	 * Performs the castling move by moving both the king and rook to their new positions.
	 *
	 * @param kingsrc the column of the king's source square
	 * @param kingdst the column of the king's destination square
	 * @param rooksrc the column of the rook's source square
	 * @param rookdst the column of the rook's destination square
	 * @param row     the row where castling occurs (same for king and rook)
	 */
	private void performCastling(
	    int kingsrc,
	    int kingdst,
	    int rooksrc,
	    int rookdst,
	    int row
	) {
		Square kingSourceSquare = board[row][kingsrc];
		Square kingDestSquare = board[row][kingdst];
		Square rookSourceSquare = board[row][rooksrc];
		Square rookDestSquare = board[row][rookdst];

		kingDestSquare.setPiece(kingSourceSquare.getPiece());
		kingSourceSquare.setPiece(null);
		kingDestSquare.getPiece().moveNotify();

		rookDestSquare.setPiece(rookSourceSquare.getPiece());
		rookSourceSquare.setPiece(null);
		rookDestSquare.getPiece().moveNotify();
	}

	/**
	 * Checks if the path is clear (no pieces in between) between two squares horizontally or vertically.
	 *
	 * @param src the source square coordinates
	 * @param dst the destination square coordinates
	 * @return {@code true} if the path is clear, {@code false} otherwise
	 */
	private boolean isPathClear(Coord src, Coord dst) {
		if (src.row == dst.row) {
			int minCol = Math.min(src.col, dst.col);
			int maxCol = Math.max(src.col, dst.col);

			for (int col = minCol + 1; col < maxCol; col++) {
				if (board[src.row][col].getPiece() != null) {
					return false;
				}
			}
		} else if (src.col == dst.col) {
			int minRow = Math.min(src.row, dst.row);
			int maxRow = Math.max(src.row, dst.row);

			for (int row = minRow + 1; row < maxRow; row++) {
				if (board[row][src.col].getPiece() != null) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Constructs a new chess game and initializes the board and players.
	 *
	 * @param timeLimit the time limit for each player (may be {@code null} for unlimited)
	 */
	public Game(LocalTime timeLimit) {
		board = new Square[DEFAULT_BOARD_WIDTH][DEFAULT_BOARD_HEIGHT];
		for (int i = 0; i < DEFAULT_BOARD_WIDTH; i++) {
			for (int j = 0; j < DEFAULT_BOARD_HEIGHT; j++) {
				board[i][j] = new Square(this);
			}
		}
		players = new Player[2];
		players[0] = new Player(0, timeLimit, this);
		players[1] = new Player(1, timeLimit, this);
		setCurrentPlayer(0);
	}

	/**
	 * Returns the current chess board.
	 *
	 * @return the current game board as a 2D array of squares
	 */
	public Square[][] getBoard() {
		return board;
	}

	/**
	 * Gets the piece located at the specified coordinates on the board.
	 *
	 * @param coord the coordinates of the square
	 * @return the piece at the given coordinates, or {@code null} if the square is empty
	 */
	public Piece getPiece(Coord coord) {
		return board[coord.row][coord.col].getPiece();
	}

	/**
	 * Initializes the board with all chess pieces in their standard starting positions.
	 * Fills empty squares with new {@code Square} instances.
	 *
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Pawn
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Rook
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Knight
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Bishop
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.Queen
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.King
	 */
	public void initializePiecePositions() {
		// Initial setup for white pieces
		board[0][0].setPiece(new Rook(Color.WHITE, this));
		board[0][1].setPiece(new Knight(Color.WHITE, this));
		board[0][2].setPiece(new Bishop(Color.WHITE, this));
		board[0][3].setPiece(new Queen(Color.WHITE, this));
		board[0][4].setPiece(new King(Color.WHITE, this));
		board[0][5].setPiece(new Bishop(Color.WHITE, this));
		board[0][6].setPiece(new Knight(Color.WHITE, this));
		board[0][7].setPiece(new Rook(Color.WHITE, this));
		for (int i = 0; i < DEFAULT_BOARD_WIDTH; i++) {
			board[1][i].setPiece(new Pawn(Color.WHITE, this));
		}

		// Initial setup for black pieces
		board[7][0].setPiece(new Rook(Color.BLACK, this));
		board[7][1].setPiece(new Knight(Color.BLACK, this));
		board[7][2].setPiece(new Bishop(Color.BLACK, this));
		board[7][3].setPiece(new Queen(Color.BLACK, this));
		board[7][4].setPiece(new King(Color.BLACK, this));
		board[7][5].setPiece(new Bishop(Color.BLACK, this));
		board[7][6].setPiece(new Knight(Color.BLACK, this));
		board[7][7].setPiece(new Rook(Color.BLACK, this));
		for (int i = 0; i < DEFAULT_BOARD_WIDTH; i++) {
			board[6][i].setPiece(new Pawn(Color.BLACK, this));
		}

		// Fill remaining squares with empty squares
		for (int i = 2; i < 6; i++) {
			for (int j = 0; j < DEFAULT_BOARD_WIDTH; j++) {
				board[i][j] = new Square(this);
			}
		}
	}

	/**
	 * Sets the current player by their player ID.
	 *
	 * @param playerID the ID of the player to set as current (0 or 1)
	 */
	public void setCurrentPlayer(int playerID) {
		currentPlayer = players[playerID];
	}

	/**
	 * Returns the player whose turn it is to move.
	 *
	 * @return the current player
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Returns the player with the specified ID.
	 *
	 * @param playerID the ID of the player (0 or 1)
	 * @return the player with the specified ID
	 */
	public Player getPlayer(int playerID) {
		return players[playerID];
	}

	/**
	 * Switches the turn to the other player.
	 */
	public void switchPlayer() {
		if (currentPlayer.getPlayerID() == 0) {
			this.currentPlayer = players[1];
		} else {
			this.currentPlayer = players[0];
		}
	}

	/**
	 * Attempts to move a piece from the source to the destination square, enforcing chess rules.
	 * Handles captures, castling, and path validation.
	 *
	 * @param src the source square coordinates
	 * @param dst the destination square coordinates
	 * @return a {@code MoveStatus} indicating the result of the move
	 *
	 * @see Coord
	 * @see MoveStatus
	 * @see bd.ac.miu.cse.b60.oop.ahm.chess.piece.King
	 */
	public MoveStatus move(Coord src, Coord dst) {
		Square sourceSquare = board[src.row][src.col];
		Square destSquare = board[dst.row][dst.col];

		Piece pieceToMove = sourceSquare.getPiece();

		if (pieceToMove == null) return MoveStatus.SourceError;

		// Only allow moving own pieces
		if (
		    !((currentPlayer.getPlayerID() == 0 && pieceToMove.isWhite()) ||
		      (currentPlayer.getPlayerID() == 1 && !pieceToMove.isWhite()))
		) return MoveStatus.PlayerError;

		// Validate move for the piece
		if (pieceToMove.isValidMove(src, dst)) {
			// Destination must be empty or contain opponent's piece
			if (
			    destSquare.getPiece() == null ||
			    destSquare.getPiece().isWhite() != pieceToMove.isWhite()
			) {
				// Handle capture
				if (destSquare.getPiece() != null) {
					destSquare.getPiece().setCaptured(true);
					currentPlayer.capturePiece(destSquare.getPiece());
				}

				// Castling logic for king
				if (
				    pieceToMove instanceof King &&
				    Math.abs(dst.col - src.col) == 2
				) {
					boolean isWhite = pieceToMove.isWhite();
					if (
					    (isWhite &&
					     src.row == 0 &&
					     src.col == 4 &&
					     currentPlayer.getPlayerID() == 0) ||
					    (!isWhite &&
					     src.row == 7 &&
					     src.col == 4 &&
					     currentPlayer.getPlayerID() == 1)
					) {
						int rookSrcCol = (dst.col - src.col == 2) ? 7 : 0;
						int rookDstCol = (dst.col - src.col == 2) ? 5 : 3;
						performCastling(
						    src.col,
						    dst.col,
						    rookSrcCol,
						    rookDstCol,
						    src.row
						);
						return MoveStatus.Ok;
					}
					return MoveStatus.CastleError;
				}

				// For non-castling moves, ensure path is clear
				if (isPathClear(src, dst)) {
					destSquare.setPiece(pieceToMove);
					sourceSquare.setPiece(null);
					pieceToMove.moveNotify();
					return MoveStatus.Ok;
				}
				return MoveStatus.PathError;
			}
			return MoveStatus.DestError;
		}
		return MoveStatus.MoveError;
	}

	/**
	 * Checks if the current player's king is in check (under attack by any opponent piece).
	 *
	 * @return {@code true} if the king is in check, {@code false} otherwise
	 */
	public boolean isCheck() {
		boolean currentKingColor;
		int currentKingRow = 0;
		int currentKingCol = 0;
		Piece pieceToCheck;

		currentKingColor = (currentPlayer == players[0]);

		// Find current player's king position
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (
				    board[row][col] != null &&
				    board[row][col].getPiece() != null
				) {
					if (
					    board[row][col].getPiece() instanceof King &&
					    board[row][col].getPiece().isWhite() == currentKingColor
					) {
						currentKingRow = row;
						currentKingCol = col;
					}
				}
			}
		}

		// Check if any opponent piece can attack the king
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (
				    board[row][col] != null &&
				    board[row][col].getPiece() != null
				) {
					if (
					    board[row][col].getPiece().isWhite() !=
					    board[currentKingRow][currentKingCol].getPiece().isWhite()
					) {
						pieceToCheck = board[row][col].getPiece();
						if (
						    pieceToCheck.isValidMove(
						        row,
						        col,
						        currentKingRow,
						        currentKingCol
						    )
						) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the current player's king is still present on the board.
	 *
	 * @return {@code true} if the king is alive, {@code false} otherwise
	 */
	public boolean isKingAlive() {
		boolean currentKingColor = (currentPlayer == players[0]);
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (
				    board[row][col] != null &&
				    board[row][col].getPiece() != null
				) {
					if (
					    board[row][col].getPiece() instanceof King &&
					    board[row][col].getPiece().isWhite() == currentKingColor
					) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the game is a draw due to both players reaching their maximum number of turns.
	 *
	 * @return {@code true} if the game is a draw, {@code false} otherwise
	 */
	public boolean isDraw() {
		return (
		           players[0].getNumOfTurns() >= players[0].getMaxNumOfTurns() &&
		           players[1].getNumOfTurns() >= players[1].getMaxNumOfTurns()
		       );
	}

	/**
	 * Sets the maximum number of turns allowed for both players.
	 *
	 * @param num the maximum number of turns
	 */
	public void setMaxNumOfTurns(int num) {
		players[0].setMaxNumOfTurns(num);
		players[1].setMaxNumOfTurns(num);
	}

	/**
	 * Returns the number of turns completed by the specified player.
	 *
	 * @param player the player whose turns are counted
	 * @return the number of turns completed by the player
	 */
	public int getNumOfTurns(Player player) {
		return player.getNumOfTurns();
	}

	/**
	 * Increments the number of turns for a player by the specified amount.
	 *
	 * @param player the player whose turns are set
	 * @param num    the number of turns to add
	 */
	public void setNumOfTurns(Player player, int num) {
		player.setNumOfTurns(getNumOfTurns(player) + num);
	}

	/**
	 * Serializes the current state of the game, including the board, players, and game metadata.
	 * Each board square is saved individually, followed by player and game state information.
	 *
	 * @return a {@link SaveFrame} object containing the serialized game state
	 */
	@Override
	public SaveFrame save() {
		try (BDOutStream bdos = new BDOutStream()) {
			// Board
			for (int i = 0; i < DEFAULT_BOARD_WIDTH; i++) {
				for (int j = 0; j < DEFAULT_BOARD_HEIGHT; j++) {
					byte[] sq = board[i][j].save().bytes();
					bdos.writeInt(sq.length); // write length
					bdos.write(sq); // write data
				}
			}
			// Players
			for (Player player : players) {
				byte[] pdata = player.save().bytes();
				bdos.writeInt(pdata.length); // write length
				bdos.write(pdata); // write data
			}
			// Current player
			bdos.writeInt(currentPlayer.getPlayerID());
			return SaveFrame.create(bdos.collect());
		} catch (Exception e) {
			throw new RuntimeException("Failed to save game state", e);
		}
	}

	/**
	 * Loads the game state from the provided {@link SavePayload} object.
	 * Restores the board, players, and game metadata from the serialized data.
	 *
	 * @param state the {@link SavePayload} containing the serialized game state
	 */
	@Override
	public void load(SavePayload state) {
		try (BDInStream bdis = new BDInStream(state.data())) {
			// Board
			// Re-initialize board squares to ensure authoritative overwrite
			for (int i = 0; i < DEFAULT_BOARD_WIDTH; i++) {
				for (int j = 0; j < DEFAULT_BOARD_HEIGHT; j++) {
					board[i][j] = new Square(this);
				}
			}
			for (int i = 0; i < DEFAULT_BOARD_WIDTH; i++) {
				for (int j = 0; j < DEFAULT_BOARD_HEIGHT; j++) {
					int len = bdis.readInt();
					byte[] dat = bdis.readNBytes(len);
					board[i][j].load(SavePayload.load(dat));
				}
			}
			// Players
			for (int idx = 0; idx < 2; idx++) {
				int len = bdis.readInt();
				byte[] dat = bdis.readNBytes(len);
				players[idx].load(SavePayload.load(dat));
			}
			// Current player
			setCurrentPlayer(bdis.readInt());
		} catch (Exception e) {
			throw new RuntimeException("Failed to load game state", e);
		}
	}

	/**
	 * Checks if the current player's time limit has been reached.
	 *
	 * @return {@code true} if the time is finished, {@code false} otherwise
	 */
	public boolean isTimeFinished() {
		return getCurrentPlayer().getIsTimeFinished();
	}

	/**
	 * Returns the array of players in the game.
	 *
	 * @return the array of players
	 */
	public Player[] getPlayerArray() {
		return players;
	}

	/**
	 * Ends the current game by stopping timers for all players.
	 */
	public void end() {
		for (Player player : players) {
			player.stopTimer();
		}
	}
}
