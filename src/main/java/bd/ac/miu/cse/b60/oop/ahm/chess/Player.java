package bd.ac.miu.cse.b60.oop.ahm.chess;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;

/**
 * Represents a chess player with a unique ID, timer, and methods to manage time and captured pieces.
 */
public class Player
{
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

	/** Number of pieces captured by the player. */
	private int capturedCount;

	/** Array of pieces captured by the player. */
	private Piece[] capturedPieces;

	/** Number of turns taken by the player. */
	private int numOfTurns = 0;

	/** Maximum number of turns allowed, default is 50. */
	private int maxNumOfTurns = 50;

	/**
	 * Constructs a {@code Player} with the specified ID.
	 *
	 * @param id the unique identifier for the player
	 */
	public Player(int id)
	{
		playerID = id;
		capturedPieces = new Piece[16];
		capturedCount = 0;
	}

	/**
	 * Constructs a {@code Player} with the specified ID and time limit.
	 *
	 * @param id        the unique identifier for the player
	 * @param timeLimit the time limit for the player's activities
	 */
	public Player(int id, LocalTime timeLimit)
	{
		playerID = id;
		this.timeLimit = timeLimit;
		capturedPieces = new Piece[16];
		capturedCount = 0;
	}

	/**
	 * Returns the player's unique ID.
	 *
	 * @return the player's ID
	 */
	public int getPlayerID()
	{
		return playerID;
	}

	/**
	 * Adds a captured piece to the player's array.
	 *
	 * @param capturedPiece the {@code Piece} captured by the player
	 */
	public void capturePiece(Piece capturedPiece)
	{
		if (capturedCount < capturedPieces.length)
			{
				capturedPieces[capturedCount++] = capturedPiece;
			}
	}

	/**
	 * Returns the array of captured pieces.
	 *
	 * @return an array of captured {@code Piece} objects
	 */
	public Piece[] getCapturedPieces()
	{
		return capturedPieces;
	}

	/**
	 * Returns the number of pieces captured.
	 *
	 * @return the count of captured pieces
	 */
	public int getCapturedCount()
	{
		return capturedCount;
	}

	/**
	 * Starts the player's timer, incrementing {@code timeConsumed} by one second at regular intervals.
	 */
	public void startTimer()
	{
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			public void run()
			{
				if (!isTimerPaused)
					{
						timeConsumed = timeConsumed.plusSeconds(1);
						if (timeLimit != null && timeConsumed.compareTo(timeLimit) >= 0)
							{
								isTimeFinished = true;
							}
					}
			}
		}, 0, 1000); // Start immediately, repeat every 1000ms
	}

	/**
	 * Returns whether the time limit has been reached.
	 *
	 * @return {@code true} if the time limit is reached, {@code false} otherwise
	 */
	public boolean getIsTimeFinished()
	{
		return isTimeFinished;
	}

	/**
	 * Pauses the player's timer.
	 */
	public void pauseTimer()
	{
		isTimerPaused = true;
	}

	/**
	 * Resumes the player's timer from where it was paused.
	 */
	public void continueTimer()
	{
		isTimerPaused = false;
	}

	/**
	 * Stops the player's timer.
	 */
	public void stopTimer()
	{
		timer.cancel();
	}

	/**
	 * Returns the total time consumed by the player.
	 *
	 * @return the {@code LocalTime} representing time consumed
	 */
	public LocalTime getTimeConsumed()
	{
		return timeConsumed;
	}

	/**
	 * Returns the total time consumed in minutes.
	 *
	 * @return the time consumed in minutes
	 */
	public long getTimePassedInMinutes()
	{
		return timeConsumed.toSecondOfDay() / 60;
	}

	/**
	 * Sets the maximum number of turns allowed.
	 *
	 * @param newMax the new maximum number of turns
	 */
	public void setMaxNumOfTurns(int newMax)
	{
		this.maxNumOfTurns = newMax;
	}

	/**
	 * Returns the maximum number of turns allowed.
	 *
	 * @return the maximum number of turns
	 */
	public int getMaxNumOfTurns()
	{
		return this.maxNumOfTurns;
	}

	/**
	 * Returns the number of turns taken by the player.
	 *
	 * @return the number of turns completed
	 */
	public int getNumOfTurns()
	{
		return this.numOfTurns;
	}

	/**
	 * Sets the number of turns taken by the player.
	 *
	 * @param newNum the new number of turns
	 */
	public void setNumOfTurns(int newNum)
	{
		this.numOfTurns = newNum;
	}
}
