package bd.ac.miu.cse.b60.oop.ahm.chess.display.swing;

import bd.ac.miu.cse.b60.oop.ahm.chess.Coord;
import bd.ac.miu.cse.b60.oop.ahm.chess.Display;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Player;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import java.awt.*;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * BoardView is a reusable Swing component for rendering and interacting with a chess board
 * in a graphical chess application. It displays the board, captured pieces, player info,
 * and a status line, and is designed to be embedded in a larger window or used standalone.
 *
 * <b>Integration:</b>
 * BoardView is typically managed by {@link bd.ac.miu.cse.b60.oop.ahm.chess.display.SwingDisplay}, which coordinates game state and user interaction.
 * Moves are communicated via registered {@link Display.MoveListener}s, and game-end requests are handled
 * through a callback provided at construction.
 *
 * <b>Public API Overview:</b>
 * <ul>
 *   <li>Move events are handled by a {@link Display.MoveListener} provided in the constructor.</li>
 *   <li>{@link #updateBoard(Game)}: Update the board display from a {@link Game} instance.</li>
 *   <li>{@link #updateCapturedPieces(Game)}: Update captured pieces display.</li>
 *   <li>{@link #updatePlayerInfo(String, java.time.LocalTime)}: Update the player info/status display.</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *   BoardView boardView = new BoardView(
 *       (src, dst) -> {
 *           // Handle move from src to dst
 *       },
 *       () -> {
 *           // Handle game end request
 *       },
 *       display
 *   );
 *   frame.getContentPane().add(boardView, BorderLayout.CENTER);
 * </pre>
 *
 * <b>Event-Driven Design:</b>
 * - User interactions (cell clicks) trigger listener notifications for moves.
 * - Game end requests are handled via the provided callback.
 *
 * <b>Threading:</b>
 * - All public update methods are safe to call from non-EDT threads; UI updates are dispatched via {@code SwingUtilities.invokeLater}.
 *
 * <b>Extension Notes:</b>
 * - BoardView can be extended to customize board appearance, add new UI elements, or modify interaction logic.
 * - For integration, ensure listeners and callbacks are properly registered to handle game logic.
 */
public class BoardView extends JPanel {

	private static final int BOARD_SIZE = 8;

	/** The 8x8 grid of buttons representing the chess board squares. */
	private final JButton[][] cells = new JButton[BOARD_SIZE][BOARD_SIZE];
	/** Panel displaying captured white pieces. */
	private final JPanel whiteCapturedPanel = new JPanel();
	/** Panel displaying captured black pieces. */
	private final JPanel blackCapturedPanel = new JPanel();
	/** Container for bottom UI elements (status, controls). */
	private final JPanel bottomContainer = new JPanel(new BorderLayout(6, 6));
	/** Panel for displaying player and game status. */
	private final JPanel statusPanel = new JPanel(new BorderLayout(6, 6));
	/** Label showing current player info and time. */
	private final JLabel playerInfoLabel = new JLabel(
	    "Player: N/A    Time: 00:00:00"
	);

	/** Callback to be invoked when the user requests to end the game. */
	private final Runnable gameEndRequested;
	/** Listener for move events triggered by user interaction. */
	private final Display.MoveListener moveListener;
	/** Reference to the parent SwingDisplay for coordination. */
	private final Display display;

	/**
	 * Pending selected source coordinate (null when none selected).
	 * Note: Coord stores col,row in this codebase.
	 */
	private Coord pendingSource;

	/**
	 * Constructs the BoardView and builds its internal Swing components.
	 *
	 * @param listener  the move event listener to notify when a move is made
	 * @param callback  the callback to invoke when the user requests to end the game
	 * @param display   the parent Display coordinating the UI
	 */
	public BoardView(
	    Display.MoveListener listener,
	    Runnable callback,
	    Display display
	) {
		setLayout(new BorderLayout(8, 8));
		buildBoardArea();
		buildSidePanels();
		buildStatusArea();
		gameEndRequested = callback;
		moveListener = listener;
		this.display = display;
	}

	private void buildBoardArea() {
		// Wrapper panel to center the board and enforce square shape
		JPanel wrapper = new JPanel(new GridBagLayout());

		// Board panel with dynamic square size
		JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE)) {
			@Override
			public Dimension getPreferredSize() {
				Container parent = getParent();
				if (parent != null) {
					int size = Math.min(parent.getWidth(), parent.getHeight());
					return new Dimension(size, size);
				}
				return super.getPreferredSize();
			}

			@Override
			public void doLayout() {
				super.doLayout();
				int size = Math.min(getWidth(), getHeight()) / BOARD_SIZE;
				Dimension btnDim = new Dimension(size, size);
				int fontSize = (int) (size * 0.60); // 75% of button size
				Font dynamicFont = new Font(
				    Font.MONOSPACED,
				    Font.BOLD,
				    fontSize
				);
				for (int r = 0; r < BOARD_SIZE; r++) {
					for (int c = 0; c < BOARD_SIZE; c++) {
						JButton btn = cells[r][c];
						btn.setPreferredSize(btnDim);
						btn.setMinimumSize(btnDim);
						btn.setMaximumSize(btnDim);
						btn.setFont(dynamicFont);
					}
				}
			}
		};
		boardPanel.setBorder(new LineBorder(Color.DARK_GRAY));

		for (int r = 0; r < BOARD_SIZE; r++) {
			for (int c = 0; c < BOARD_SIZE; c++) {
				final int row = r;
				final int col = c;
				JButton btn = new JButton();
				btn.setFocusable(false);
				btn.setMargin(new Insets(2, 2, 2, 2));
				boolean isLight = ((r + c) % 2 == 0);
				btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
				btn.setBackground(isLight ? Color.LIGHT_GRAY : Color.DARK_GRAY);
				btn.setForeground(isLight ? Color.DARK_GRAY : Color.LIGHT_GRAY);
				btn.setOpaque(true);
				btn.setBorder(new LineBorder(Color.GRAY));
				btn.addActionListener(e -> onCellClicked(row, col));
				btn.setHorizontalAlignment(SwingConstants.CENTER);
				btn.setVerticalAlignment(SwingConstants.CENTER);
				cells[r][c] = btn;
				boardPanel.add(btn);
			}
		}

		wrapper.add(boardPanel);

		add(wrapper, BorderLayout.CENTER);
	}

	private void buildSidePanels() {
		whiteCapturedPanel.setLayout(
		    new BoxLayout(whiteCapturedPanel, BoxLayout.X_AXIS)
		);
		whiteCapturedPanel.setBorder(
		    BorderFactory.createTitledBorder("White Captured")
		);

		blackCapturedPanel.setLayout(
		    new BoxLayout(blackCapturedPanel, BoxLayout.X_AXIS)
		);
		blackCapturedPanel.setBorder(
		    BorderFactory.createTitledBorder("Black Captured")
		);

		bottomContainer.setLayout(new BorderLayout(4, 4));
		bottomContainer.add(blackCapturedPanel, BorderLayout.NORTH);
		bottomContainer.add(statusPanel, BorderLayout.SOUTH);

		add(whiteCapturedPanel, BorderLayout.NORTH);
		add(bottomContainer, BorderLayout.SOUTH);
	}

	private void buildStatusArea() {
		playerInfoLabel.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
		statusPanel.setPreferredSize(new Dimension(0, 36));
		statusPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
		statusPanel.add(playerInfoLabel, BorderLayout.WEST);

		JButton endGameBtn = new JButton("End Current Game");
		endGameBtn.setFocusable(false);
		endGameBtn.setMargin(new Insets(4, 8, 4, 8));
		endGameBtn.addActionListener(e -> {
			resetSelection();
			display.showMessage("Game Ended");
			gameEndRequested.run();
		});
		statusPanel.add(endGameBtn, BorderLayout.EAST);
	}

	private void onCellClicked(int row, int col) {
		Coord clicked = new Coord(col, row);
		if (pendingSource == null) {
			pendingSource = clicked;
			highlightCell(pendingSource.row, pendingSource.col, true);
		} else {
			highlightCell(pendingSource.row, pendingSource.col, false);
			Coord src = pendingSource;
			Coord dst = clicked;
			pendingSource = null;
			// Notify listeners on EDT to avoid threading surprises
			moveListener.onMoveRequested(src, dst);
		}
	}

	private void highlightCell(int row, int col, boolean highlight) {
		if (
		    row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE
		) return;
		JButton btn = cells[row][col];
		if (btn == null) return;
		if (highlight) {
			btn.setBorder(new LineBorder(Color.YELLOW, 3));
		} else {
			btn.setBorder(new LineBorder(Color.GRAY));
		}
	}

	/**
	 * Update the board UI to reflect the {@link Game} state.
	 * This method is safe to call from any thread.
	 *
	 * @param game the current game instance
	 */
	public void updateBoard(final Game game) {
		Objects.requireNonNull(game, "game must not be null");
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(() -> updateBoard(game));
			return;
		}

		Square[][] board = game.getBoard();
		// Defensive checks - only iterate within BOARD_SIZE bounds
		int rows = Math.min(board.length, BOARD_SIZE);
		for (int r = 0; r < rows; r++) {
			int cols = Math.min(board[r].length, BOARD_SIZE);
			for (int c = 0; c < cols; c++) {
				JButton cell = cells[r][c];
				Square sq = board[r][c];
				Piece p = (sq != null) ? sq.getPiece() : null;
				cell.setText(p == null ? "" : p.getSymbol());
			}
		}
	}

	/**
	 * Update the captured pieces UI from {@link Game}.
	 * Safe to call from any thread.
	 *
	 * @param game current game instance
	 */
	public void updateCapturedPieces(final Game game) {
		Objects.requireNonNull(game, "game must not be null");
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(() -> updateCapturedPieces(game));
			return;
		}

		whiteCapturedPanel.removeAll();
		blackCapturedPanel.removeAll();

		Player white = game.getPlayer(0);
		Player black = game.getPlayer(1);

		Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);

		Piece[] whiteCaptured = white.getCapturedPieces();
		if (whiteCaptured.length == 0) whiteCapturedPanel.add(
			    new JLabel("None")
			);
		else for (Piece p : whiteCaptured) {
				JLabel label = new JLabel(p.getSymbol());
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setFont(font);
				whiteCapturedPanel.add(label);
			}

		Piece[] blackCaptured = black.getCapturedPieces();
		if (blackCaptured.length == 0) blackCapturedPanel.add(
			    new JLabel("None")
			);
		else for (Piece p : blackCaptured) {
				JLabel label = new JLabel(p.getSymbol());
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setFont(font);
				blackCapturedPanel.add(label);
			}

		whiteCapturedPanel.revalidate();
		whiteCapturedPanel.repaint();
		blackCapturedPanel.revalidate();
		blackCapturedPanel.repaint();
	}

	/**
	 * Update the player info area.
	 *
	 * @param playerColor color name
	 * @param timeConsumed time consumed
	 */
	public void updatePlayerInfo(
	    final String playerColor,
	    final java.time.LocalTime timeConsumed
	) {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(() ->
			                           updatePlayerInfo(playerColor, timeConsumed)
			                          );
			return;
		}
		playerInfoLabel.setText(
		    String.format(
		        "%s Player's turn. Time: %s",
		        playerColor,
		        formatTime(timeConsumed)
		    )
		);
	}

	/**
	 * Formats a {@code LocalTime} object as a {@code String} in HH:mm:ss format.
	 *
	 * @param time the {@code LocalTime} to format
	 * @return a {@code String} representation of the time in HH:mm:ss
	 */
	private String formatTime(java.time.LocalTime time) {
		java.time.format.DateTimeFormatter formatter =
		    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
		return time.format(formatter);
	}

	/**
	 * Reset any current selection highlight and internal state.
	 */
	public void resetSelection() {
		if (pendingSource != null) {
			highlightCell(pendingSource.row, pendingSource.col, false);
			pendingSource = null;
		}
	}
}
