package bd.ac.miu.cse.b60.oop.ahm.chess;

import bd.ac.miu.cse.b60.oop.ahm.chess.state.BDInStream;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.BDOutStream;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SaveData;
import bd.ac.miu.cse.b60.oop.ahm.chess.state.SavedData;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Represents a player in a chess game, managing their identity, time control, captured pieces, and turn tracking.
 * <p>
 * Each player has a unique ID, can be assigned a time limit, and keeps track of captured pieces and the number of turns taken.
 * Provides methods for timer management and turn counting.
 * </p>
 */
public class Player
	implements
	bd.ac.miu.cse.b60.oop.ahm.chess.state.Saveable,
	bd.ac.miu.cse.b60.oop.ahm.chess.state.Loadable {

	private Game game;

	/** Unique identifier for the player. */
	private int playerID;

	/** Time consumed by the player, initialized to {@code 00:00}. */
	private LocalTime timeConsumed = LocalTime.of(0, 0);

	/** Time limit for the player's activities, or {@code null} if not set. */
	private LocalTime timeLimit;

	/** Timer to track the player's time. */
	private Timer timer;

	/** Indicates whether the timer is paused. */
	private boolean isTimerPaused = false;

	/** Indicates whether the time limit has been reached. */
	private boolean isTimeFinished = false;

	/** Collection of pieces captured by this player. */
	private Vector<Piece> capturedPieces;

	/** Number of turns taken by this player. */
	private int numOfTurns = 0;

	/** Maximum number of turns allowed for this player (default is 50). */
	private int maxNumOfTurns = 50;

	/**
	 * Constructs a Player with the specified ID.
	 *
	 * @param id the unique identifier for the player
	 */
	public Player(int id, Game game) {
		playerID = id;
		this.game = game;
		capturedPieces = new Vector<>();
	}

	/**
	 * Constructs a Player with the specified ID and time limit.
	 *
	 * @param id        the unique identifier for the player
	 * @param timeLimit the time limit for the player's activities
	 */
	public Player(int id, LocalTime timeLimit, Game game) {
		playerID = id;
		this.timeLimit = timeLimit;
		this.game = game;
		capturedPieces = new Vector<>();
	}

	/**
	 * Gets the player's unique ID.
	 *
	 * @return the player's ID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Adds a captured piece to this player's collection.
	 *
	 * @param capturedPiece the Piece captured by the player
	 */
	public void capturePiece(Piece capturedPiece) {
		capturedPieces.add(capturedPiece);
	}

	/**
	 * Gets all pieces captured by this player.
	 *
	 * @return an array of captured Piece objects
	 */
	public Piece[] getCapturedPieces() {
		return capturedPieces.toArray(new Piece[0]);
	}

	/**
	 * Starts the player's timer, incrementing {@code timeConsumed} by one second at regular intervals.
	 * If a time limit is set and reached, marks the player's time as finished.
	 */
	public void startTimer() {
		timer = new Timer();
		timer.scheduleAtFixedRate(
		new TimerTask() {
			public void run() {
				if (!isTimerPaused) {
					timeConsumed = timeConsumed.plusSeconds(1);
					if (
					    timeLimit != null &&
					    timeConsumed.compareTo(timeLimit) >= 0
					) {
						isTimeFinished = true;
					}
				}
			}
		},
		0,
		1000
		); // Start immediately, repeat every 1000ms
	}

	/**
	 * Checks if the player's time limit has been reached.
	 *
	 * @return {@code true} if the time limit is reached, {@code false} otherwise
	 */
	public boolean getIsTimeFinished() {
		return isTimeFinished;
	}

	/**
	 * Pauses the player's timer.
	 */
	public void pauseTimer() {
		isTimerPaused = true;
	}

	/**
	 * Resumes the player's timer from where it was paused.
	 */
	public void continueTimer() {
		isTimerPaused = false;
	}

	/**
	 * Stops the player's timer.
	 */
	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	/**
	 * Gets the total time consumed by the player.
	 *
	 * @return the LocalTime representing time consumed
	 */
	public LocalTime getTimeConsumed() {
		return timeConsumed;
	}

	/**
	 * Gets the total time consumed in minutes.
	 *
	 * @return the time consumed in minutes
	 */
	public long getTimePassedInMinutes() {
		return timeConsumed.toSecondOfDay() / 60;
	}

	/**
	 * Sets the maximum number of turns allowed for this player.
	 *
	 * @param newMax the new maximum number of turns
	 */
	public void setMaxNumOfTurns(int newMax) {
		this.maxNumOfTurns = newMax;
	}

	@Override
	public SavedData save() {
		try (BDOutStream bdos = new BDOutStream()) {
			bdos.writeInt(playerID);
			bdos.writeInt(numOfTurns);
			bdos.writeInt(maxNumOfTurns);
			bdos.writeLong(timeConsumed.toSecondOfDay());
			bdos.writeBoolean(timeLimit != null);
			if (timeLimit != null) bdos.writeLong(timeLimit.toSecondOfDay());
			bdos.writeInt(capturedPieces.size());
			for (Piece p : capturedPieces) {
				byte[] pdata = p.save().bytes();
				bdos.writeInt(pdata.length);
				bdos.write(pdata);
			}
			return SavedData.create(bdos.collect());
		} catch (IOException e) {
			throw new RuntimeException(
			    String.format(
			        "Failed to save %s player's state",
			        Color.fromId(playerID).name
			    ),
			    e
			);
		}
	}

	/**
	 * Loads the player's state from the provided {@link bd.ac.miu.cse.b60.oop.ahm.chess.state.SaveData} object and game context.
	 * <p>
	 * This method restores the player's ID, turn counts, time consumed, and captured pieces.
	 * The {@code game} parameter is required to properly instantiate captured pieces.
	 * </p>
	 *
	 * @param state the {@link bd.ac.miu.cse.b60.oop.ahm.chess.state.SaveData} containing the serialized player state
	 * @param game  the {@link Game} instance for context when reconstructing pieces
	 */
	public void load(SaveData state) {
		byte[] data = state.data();
		try (BDInStream bdis = new BDInStream(data)) {
			playerID = bdis.readInt();
			numOfTurns = bdis.readInt();
			maxNumOfTurns = bdis.readInt();
			timeConsumed = LocalTime.ofSecondOfDay(bdis.readLong());
			if (bdis.readBoolean()) timeLimit = LocalTime.ofSecondOfDay(
				                                        bdis.readLong()
				                                    );
			else timeLimit = null;
			int capCount = bdis.readInt();
			capturedPieces.clear();
			while (capturedPieces.size() < capCount) {
				int len = bdis.readInt();
				byte[] dat = bdis.readNBytes(len);
				SaveData save = SaveData.load(dat);
				Piece p = Piece.fromSaveData(save, game);
				capturedPieces.add(p);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load players state", e);
		}
	}

	/**
	 * Gets the maximum number of turns allowed for this player.
	 *
	 * @return the maximum number of turns
	 */
	public int getMaxNumOfTurns() {
		return this.maxNumOfTurns;
	}

	/**
	 * Gets the number of turns taken by this player.
	 *
	 * @return the number of turns completed
	 */
	public int getNumOfTurns() {
		return this.numOfTurns;
	}

	/**
	 * Sets the number of turns taken by this player.
	 *
	 * @param newNum the new number of turns
	 */
	public void setNumOfTurns(int newNum) {
		this.numOfTurns = newNum;
	}
}
