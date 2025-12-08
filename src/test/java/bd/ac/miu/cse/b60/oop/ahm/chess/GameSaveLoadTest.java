package bd.ac.miu.cse.b60.oop.ahm.chess;

import static org.junit.jupiter.api.Assertions.*;

import bd.ac.miu.cse.b60.oop.ahm.chess.state.SaveData;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Game save/load functionality.
 * This test ensures that saving and loading a game preserves the board state,
 * player state, and current player.
 */
public class GameSaveLoadTest {

	@Test
	void testSaveLoadRestoresGameState() {
		// Create and initialize a game
		Game game = new Game(LocalTime.of(0, 10));
		game.initializePiecePositions();
		game.setMaxNumOfTurns(20);

		// Make a move: White pawn e2 to e4
		Coord src = new Coord('e', '2');
		Coord dst = new Coord('e', '4');
		assertEquals(
		    MoveStatus.Ok,
		    game.move(src, dst),
		    "Move should be valid"
		);

		// Switch player (simulate end of turn)
		game.switchPlayer();

		// Save the game state
		SaveData saved = game.save();

		// Create a new game instance and load the saved state
		Game loadedGame = new Game(LocalTime.of(0, 10));
		loadedGame.initializePiecePositions(); // Ensure board is initialized
		loadedGame.setMaxNumOfTurns(20);
		loadedGame.load(saved);

		// Check that the board state is restored
		assertNull(
		    loadedGame.getPiece(new Coord('e', '2')),
		    "e2 should be empty after move"
		);
		assertNotNull(
		    loadedGame.getPiece(new Coord('e', '4')),
		    "e4 should have a piece after move"
		);
		assertEquals(
		    game.getPiece(new Coord('e', '4')).getClass(),
		    loadedGame.getPiece(new Coord('e', '4')).getClass(),
		    "Piece type at e4 should match"
		);
		assertEquals(
		    game.getPiece(new Coord('e', '4')).isWhite(),
		    loadedGame.getPiece(new Coord('e', '4')).isWhite(),
		    "Piece color at e4 should match"
		);

		// Check that the current player is restored
		assertEquals(
		    game.getCurrentPlayer().getPlayerID(),
		    loadedGame.getCurrentPlayer().getPlayerID(),
		    "Current player should be preserved after load"
		);

		// Check that player turn counts are preserved
		assertEquals(
		    game.getPlayer(0).getNumOfTurns(),
		    loadedGame.getPlayer(0).getNumOfTurns(),
		    "White player's turn count should match"
		);
		assertEquals(
		    game.getPlayer(1).getNumOfTurns(),
		    loadedGame.getPlayer(1).getNumOfTurns(),
		    "Black player's turn count should match"
		);
	}

	@Test
	void testSaveLoadWithCapture() {
		Game game = new Game(LocalTime.of(0, 10));
		game.initializePiecePositions();
		game.setMaxNumOfTurns(20);

		// White pawn e2 to e4
		assertEquals(
		    MoveStatus.Ok,
		    game.move(new Coord('e', '2'), new Coord('e', '4'))
		);
		game.switchPlayer();
		// Black pawn d7 to d5
		assertEquals(
		    MoveStatus.Ok,
		    game.move(new Coord('d', '7'), new Coord('d', '5'))
		);
		game.switchPlayer();
		// White pawn e4 takes d5
		assertEquals(
		    MoveStatus.Ok,
		    game.move(new Coord('e', '4'), new Coord('d', '5'))
		);
		game.switchPlayer();

		SaveData saved = game.save();
		Game loadedGame = new Game(LocalTime.of(0, 10));
		loadedGame.initializePiecePositions();
		loadedGame.setMaxNumOfTurns(20);
		loadedGame.load(saved);

		// d5 should have a white pawn, d7 and e4 should be empty
		Piece d5 = loadedGame.getPiece(new Coord('d', '5'));
		assertNotNull(d5);
		assertTrue(d5.isWhite());
		assertNull(loadedGame.getPiece(new Coord('d', '7')));
		assertNull(loadedGame.getPiece(new Coord('e', '4')));
		// Black should have one captured piece
		int capturedCount =
		    loadedGame.getPlayer(0).getCapturedPieces().length +
		    loadedGame.getPlayer(1).getCapturedPieces().length;
		assertEquals(1, capturedCount, "One pawn should be captured");
	}

	@Test
	void testSaveLoadPlayerTimeAndTurns() {
		Game game = new Game(LocalTime.of(0, 5));
		game.initializePiecePositions();
		game.setMaxNumOfTurns(15);
		game.getPlayer(0).setNumOfTurns(7);
		game.getPlayer(1).setNumOfTurns(8);
		SaveData saved = game.save();
		Game loadedGame = new Game(LocalTime.of(0, 5));
		loadedGame.initializePiecePositions();
		loadedGame.setMaxNumOfTurns(15);
		loadedGame.load(saved);
		assertEquals(7, loadedGame.getPlayer(0).getNumOfTurns());
		assertEquals(8, loadedGame.getPlayer(1).getNumOfTurns());
		assertEquals(15, loadedGame.getPlayer(0).getMaxNumOfTurns());
		assertEquals(15, loadedGame.getPlayer(1).getMaxNumOfTurns());
	}

	@Test
	void testSaveLoadEmptyBoard() {
		Game game = new Game(LocalTime.of(0, 10));
		// Do not initialize pieces, board should be empty
		SaveData saved = game.save();
		Game loadedGame = new Game(LocalTime.of(0, 10));
		loadedGame.load(saved);
		for (int i = 0; i < Game.DEFAULT_BOARD_WIDTH; i++) {
			for (int j = 0; j < Game.DEFAULT_BOARD_HEIGHT; j++) {
				assertNull(
				    loadedGame.getBoard()[i][j].getPiece(),
				    "Board should be empty at [" + i + "," + j + "]"
				);
			}
		}
	}

	@Test
	void testSaveLoadAfterManyMoves() {
		Game game = new Game(LocalTime.of(0, 10));
		game.initializePiecePositions();
		// Simulate 5 moves for each player, alternating turns
		char[] whiteRows = { '2', '3', '4', '5', '6' };
		char[] blackRows = { '7', '6', '5', '4', '3' };
		for (int i = 0; i < 4; i++) {
			// White pawn a-file advances
			Coord whiteSrc = new Coord('a', whiteRows[i]);
			Coord whiteDst = new Coord('a', whiteRows[i + 1]);
			assertEquals(MoveStatus.Ok, game.move(whiteSrc, whiteDst));
			game.switchPlayer();

			// Black pawn h-file advances
			Coord blackSrc = new Coord('h', blackRows[i]);
			Coord blackDst = new Coord('h', blackRows[i + 1]);
			assertEquals(MoveStatus.Ok, game.move(blackSrc, blackDst));
			game.switchPlayer();
		}
		SaveData saved = game.save();
		Game loadedGame = new Game(LocalTime.of(0, 10));
		loadedGame.initializePiecePositions();
		loadedGame.load(saved);
		// Check pawn advanced to a7 for White and h2 for Black
		assertNotNull(loadedGame.getPiece(new Coord('a', '7')));
		assertNull(loadedGame.getPiece(new Coord('a', '2')));
		assertNotNull(loadedGame.getPiece(new Coord('h', '2')));
		assertNull(loadedGame.getPiece(new Coord('h', '7')));
	}
}
