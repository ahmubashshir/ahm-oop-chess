package bd.ac.miu.cse.b60.oop.ahm.chess.display.swing;

import bd.ac.miu.cse.b60.oop.ahm.chess.Coord;
import bd.ac.miu.cse.b60.oop.ahm.chess.Display;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.Piece;
import bd.ac.miu.cse.b60.oop.ahm.chess.Player;
import bd.ac.miu.cse.b60.oop.ahm.chess.Square;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * A reusable Swing component that renders the chess board, captured pieces,
 * player info, and a status line. Implemented as a {@code JPanel} for easy embedding
 * in a larger window or standalone use in a {@code JFrame}.
 *
 * <b>Usage Example:</b>
 * <pre>
 *   BoardView boardView = new BoardView(() -> {
 *       // Callback when game end is requested
 *   });
 *   boardView.addMoveListener((src, dst) -> {
 *       // Handle move from src to dst
 *   });
 *   frame.getContentPane().add(boardView, BorderLayout.CENTER);
 * </pre>
 *
 * <b>Responsibilities:</b>
 * - Render an 8x8 board of clickable cells.
 * - Track and highlight pending source selection; notify registered {@link Display.MoveListener}s when a destination is chosen.
 * - Display captured pieces for both players and basic player/time info.
 * - Provide convenience methods for updating from a {@link Game} instance.
 *
 * <b>Event-Driven Design:</b>
 * - User interactions (cell clicks) trigger listener notifications for moves.
 * - Game end requests are handled via a callback.
 *
 * <b>Threading:</b>
 * - Public update methods use {@code SwingUtilities.invokeLater} when necessary, so callers may invoke them from non-EDT threads.
 */
public class BoardView extends JPanel {

	private static final int BOARD_SIZE = 8;

	private final JButton[][] cells = new JButton[BOARD_SIZE][BOARD_SIZE];
	private final JPanel whiteCapturedPanel = new JPanel();
	private final JPanel blackCapturedPanel = new JPanel();
	private final JPanel bottomContainer = new JPanel(new BorderLayout(6, 6));
	private final JPanel statusPanel = new JPanel(new BorderLayout(6, 6));
	private final JLabel statusLabel = new JLabel("Ready");
	private final JLabel playerInfoLabel = new JLabel(
	    "Player: N/A    Time: 00:00:00"
	);

	private final List<Display.MoveListener> moveListeners =
	    new CopyOnWriteArrayList<>();
	private final Runnable gameEndRequested;

	/**
	 * Pending selected source coordinate (null when none selected).
	 * Note: Coord stores col,row in this codebase.
	 */
	private Coord pendingSource;

	/**
	 * Constructs the BoardView and builds its internal Swing components.
	 */
	public BoardView(Runnable callback) {
		setLayout(new BorderLayout(8, 8));
		buildBoardArea();
		buildSidePanels();
		buildStatusArea();
		gameEndRequested = callback;
	}

	private void buildBoardArea() {
		JPanel center = new JPanel(new BorderLayout(6, 6));

		JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
		boardPanel.setBorder(new LineBorder(Color.DARK_GRAY));

		Font cellFont = new Font(Font.MONOSPACED, Font.PLAIN, 20);

		for (int r = 0; r < BOARD_SIZE; r++) {
			for (int c = 0; c < BOARD_SIZE; c++) {
				final int row = r;
				final int col = c;
				JButton btn = new JButton();
				btn.setFocusable(false);
				btn.setFont(cellFont);
				btn.setMargin(new Insets(2, 2, 2, 2));
				boolean isLight = ((r + c) % 2 == 0);
				btn.setBackground(isLight ? Color.LIGHT_GRAY : Color.DARK_GRAY);
				btn.setOpaque(true);
				btn.setBorder(new LineBorder(Color.GRAY));
				btn.addActionListener(e -> onCellClicked(row, col));
				cells[r][c] = btn;
				boardPanel.add(btn);
			}
		}

		center.add(boardPanel, BorderLayout.CENTER);
		add(center, BorderLayout.CENTER);
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
		statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
		statusPanel.setPreferredSize(new Dimension(0, 36));
		statusPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
		statusPanel.add(playerInfoLabel, BorderLayout.WEST);
		statusPanel.add(statusLabel, BorderLayout.CENTER);

		JButton endGameBtn = new JButton("End Current Game");
		endGameBtn.setFocusable(false);
		endGameBtn.setMargin(new Insets(4, 8, 4, 8));
		endGameBtn.addActionListener(e -> {
			resetSelection();
			gameEndRequested.run();
		});
		statusPanel.add(endGameBtn, BorderLayout.EAST);
	}

	/**
	 * Register a {@link Display.MoveListener} that will be notified when the user
	 * selects a source then a destination on the board.
	 *
	 * @param listener listener to register
	 */
	public void addMoveListener(Display.MoveListener listener) {
		if (listener != null) moveListeners.add(listener);
	}

	/**
	 * Remove a previously registered move listener.
	 *
	 * @param listener listener to remove
	 */
	public void removeMoveListener(Display.MoveListener listener) {
		moveListeners.remove(listener);
	}

	private void onCellClicked(int row, int col) {
		Coord clicked = new Coord(col, row);
		if (pendingSource == null) {
			pendingSource = clicked;
			highlightCell(pendingSource.row, pendingSource.col, true);
			setStatus(
			    String.format(
			        "Selected source: %c%d",
			        'a' + pendingSource.col,
			        pendingSource.row + 1
			    )
			);
		} else {
			highlightCell(pendingSource.row, pendingSource.col, false);
			Coord src = pendingSource;
			Coord dst = clicked;
			pendingSource = null;
			// Notify listeners on EDT to avoid threading surprises
			for (Display.MoveListener l : moveListeners) {
				try {
					l.onMoveRequested(src, dst);
				} catch (Throwable ignored) {
					// Listener exceptions must not break the UI; report a status instead
					setStatus("Listener error while requesting move");
				}
			}
			setStatus(
			    String.format(
			        "Move requested: %c%d -> %c%d",
			        (char) ('a' + src.col),
			        src.row + 1,
			        (char) ('a' + dst.col),
			        dst.row + 1
			    )
			);
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
		setStatus("Board updated");
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

		setStatus("Captured pieces updated");
	}

	/**
	 * Show a short status message in the status area.
	 *
	 * @param message message to show
	 */
	public void showMessage(String message) {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(() -> showMessage(message));
			return;
		}
		setStatus(message);
	}

	/**
	 * Show an error to the user (dialog + status).
	 *
	 * @param message error message
	 */
	public void showError(String message) {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(() -> showError(message));
			return;
		}
		statusLabel.setForeground(Color.RED);
		statusLabel.setText("ERROR: " + message);
		JOptionPane.showMessageDialog(
		    SwingUtilities.getWindowAncestor(this),
		    message,
		    "Error",
		    JOptionPane.ERROR_MESSAGE
		);
		// reset status color shortly after
		javax.swing.Timer t = new javax.swing.Timer(1200, e ->
		    statusLabel.setForeground(Color.BLACK)
		                                           );
		t.setRepeats(false);
		t.start();
	}

	/**
	 * Update the player info area.
	 *
	 * @param playerColor color name
	 * @param timeConsumed time consumed
	 */
	public void showPlayerInfo(
	    final String playerColor,
	    final java.time.LocalTime timeConsumed
	) {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(() ->
			                           showPlayerInfo(playerColor, timeConsumed)
			                          );
			return;
		}
		playerInfoLabel.setText(
		    String.format(
		        "%s Player's turn. Time: %02d:%02d:%02d",
		        playerColor,
		        timeConsumed.getHour(),
		        timeConsumed.getMinute(),
		        timeConsumed.getSecond()
		    )
		);
	}

	/**
	 * Present a game-end message to the user.
	 *
	 * @param message end-of-game message
	 */
	public void showGameEnd(String message) {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(() -> showGameEnd(message));
			return;
		}
		JOptionPane.showMessageDialog(
		    SwingUtilities.getWindowAncestor(this),
		    message,
		    "Game Over",
		    JOptionPane.INFORMATION_MESSAGE
		);
		setStatus("Game over");
	}

	/**
	 * Show a check warning to the user.
	 */
	public void showCheckWarning() {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(this::showCheckWarning);
			return;
		}
		JOptionPane.showMessageDialog(
		    SwingUtilities.getWindowAncestor(this),
		    "You are in check!",
		    "Check",
		    JOptionPane.WARNING_MESSAGE
		);
		setStatus("Check!");
	}

	/**
	 * Reset any current selection highlight and internal state.
	 */
	public void resetSelection() {
		if (pendingSource != null) {
			highlightCell(pendingSource.row, pendingSource.col, false);
			pendingSource = null;
			setStatus("Selection cleared");
		}
	}

	private void setStatus(String message) {
		statusLabel.setForeground(Color.BLACK);
		statusLabel.setText(message);
	}
}
