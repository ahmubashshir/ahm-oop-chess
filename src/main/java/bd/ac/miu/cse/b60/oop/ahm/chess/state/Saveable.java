package bd.ac.miu.cse.b60.oop.ahm.chess.state;

/**
 * Interface for serializing the state of a chess game.
 * Implementations should provide a way to save the current state
 * of the game for persistence or transfer.
 */
public interface Saveable {
	/**
	 * Saves the current state of the game or component.
	 *
	 * @return a {@link SavedData} object representing the saved state,
	 *         including checksum and raw byte data.
	 */
	SavedData save();
}
