package bd.ac.miu.cse.b60.oop.ahm.chess.state;

/**
 * Interface for serializing the state of a chess game.
 * Implementations should provide a way to save the current state
 * of the game for persistence or transfer.
 */
public interface Saveable {
	/**
	 * Save the current state of the game.
	 * @return a byte array representing the saved state
	 */
	SaveData save();
}
