package bd.ac.miu.cse.b60.oop.ahm.chess.state;

/**
 * Interface for loading (deserializing) the state of a chess game or its components.
 * Implementations should provide a way to restore the state from a serialized form.
 */
public interface Loadable {
	/**
	 * Loads the state from the given serialized data.
	 * Implementations should restore the object's state from the provided SaveData.
	 *
	 * @param state the serialized state to load from
	 */
	void load(SaveData state);
}
