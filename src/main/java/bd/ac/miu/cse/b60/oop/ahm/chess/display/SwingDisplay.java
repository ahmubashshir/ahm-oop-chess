package bd.ac.miu.cse.b60.oop.ahm.chess.display;

import bd.ac.miu.cse.b60.oop.ahm.chess.Coord;
import bd.ac.miu.cse.b60.oop.ahm.chess.Display;
import bd.ac.miu.cse.b60.oop.ahm.chess.Game;
import bd.ac.miu.cse.b60.oop.ahm.chess.MoveStatus;
import bd.ac.miu.cse.b60.oop.ahm.chess.State;
import bd.ac.miu.cse.b60.oop.ahm.chess.display.swing.BoardView;
import bd.ac.miu.cse.b60.oop.ahm.chess.display.swing.MainMenuView;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;

/**
 * AWT/Swing based implementation of {@link Display}.
 *
 * This class provides an event-driven GUI using Swing. It renders the board,
 * captured pieces, player info, and provides controls for starting/exiting the game.
 *
 * <b>Usage:</b>
 * <pre>
 *     Display display = new SwingDisplay();
 *     display.run();
 * </pre>
 *
 * <b>Architecture:</b>
 * - Event-driven: User actions (moves, menu selections) are handled via listeners.
 * - UI components: Uses {@link BoardView} for board display and {@link MainMenuView} for menu.
 * - Threading: All UI updates are performed on the Swing Event Dispatch Thread (EDT).
 *
 * <b>API Notes:</b>
 * - {@link #showMainMenu()} displays a blocking dialog for Start/Exit.
 * - {@link #getCoord(String)} always returns {@code null}; coordinates are acquired via UI clicks.
 * - Use {@link #addMoveListener(MoveListener)} and {@link #addMenuListener(StateListener)} to handle user actions.
 */
public class SwingDisplay implements Display {

	private final List<MoveListener> moveListeners =
	    new CopyOnWriteArrayList<>();
	private final List<StateListener> menuListeners =
	    new CopyOnWriteArrayList<>();

	private JFrame frame;
	private BoardView boardView;
	private MainMenuView menuView;
	private JPanel messagePanel;
	private final java.util.LinkedList<Message> messages =
	    new java.util.LinkedList<>();
	private final int MAX_MESSAGES = 3;

	private enum MessageType {
		INFO,
		ERROR,
	}

	private static class Message {

		final String text;
		final MessageType type;

		Message(String text, MessageType type) {
			this.text = text;
			this.type = type;
		}
	}

	// Keep only the flags we actually need at this level.
	private volatile boolean exitRequested = false;

	public SwingDisplay() {
		// UI is created lazily on run()/EDT
	}

	private void notifyMenuListeners(State result) {
		for (StateListener listener : menuListeners) {
			listener.onStateChange(result);
		}
	}

	private void notifyMoveListeners(Coord src, Coord dst) {
		for (MoveListener listener : moveListeners) {
			listener.onMoveRequested(src, dst);
		}
	}

	/**
	 * Build the main GUI components on the Swing EDT.
	 */
	/**
	 * Updates the message panel to show the last MAX_MESSAGES messages.
	 */
	private void updateMessagePanel() {
		if (messagePanel == null) return;
		messagePanel.removeAll();
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 13);
		for (Message msg : messages) {
			JLabel label = new JLabel(msg.text);
			label.setFont(font);
			label.setAlignmentX(Component.LEFT_ALIGNMENT);
			if (msg.type == MessageType.ERROR) label.setForeground(Color.RED);
			messagePanel.add(label);
		}
		messagePanel.revalidate();
		messagePanel.repaint();
	}

	/**
	 * Adds a message to the message list and updates the panel.
	 */
	private void addMessage(String text, MessageType type) {
		if (messages.size() == MAX_MESSAGES) {
			messages.removeFirst();
		}
		messages.add(new Message(text, type));
		updateMessagePanel();
	}

	private void buildUI() {
		frame = new JFrame("OOPChess: Swing UI");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout(6, 6));

		// Message panel at the top, always visible
		messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messagePanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		frame.add(messagePanel, BorderLayout.NORTH);
		updateMessagePanel();

		boardView = new BoardView(
		    this::notifyMoveListeners,
		    this::endGame,
		    this
		);
		menuView = new MainMenuView(this::showBoard, this::exitApp);

		frame.add(menuView, BorderLayout.CENTER);

		// Window closing should notify menu listeners with EXIT
		frame.addWindowListener(
		new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				endGame();
				exitApp();
			}
		}
		);

		frame.setSize(900, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	// User interactions on the board are handled by BoardView.

	@Override
	public void updateBoard(final Game game) {
		if (boardView != null) {
			boardView.updateBoard(game);
		}
	}

	// Symbol mapping is implemented inside BoardView.

	@Override
	public void updateCapturedPieces(final Game game) {
		if (boardView != null) {
			boardView.updateCapturedPieces(game);
		}
	}

	@Override
	public Coord getCoord(String query) {
		// Event-driven GUI: coordinates are obtained via clicks. For API compatibility return null.
		// If caller expects blocking coordinate input (CLI), they should use CLI display.
		return null;
	}

	/**
	 * Replaces the CENTER component of the frame's content pane with the given new component,
	 * preserving other components (such as the status label).
	 *
	 * @param frame the JFrame whose content pane should be updated
	 * @param newCenter the new component to place in BorderLayout.CENTER
	 */
	private void swapCenterComponent(JFrame frame, Component newCenter) {
		Container contentPane = frame.getContentPane();
		LayoutManager layout = contentPane.getLayout();
		if (layout instanceof BorderLayout) {
			Component center = ((BorderLayout) layout).getLayoutComponent(
			                       BorderLayout.CENTER
			                   );
			if (center != null) {
				contentPane.remove(center);
			}
			contentPane.add(newCenter, BorderLayout.CENTER);
			contentPane.revalidate();
			contentPane.repaint();
		}
	}

	@Override
	public void showMainMenu() {
		if (frame == null) buildUI();
		if (menuView == null) menuView = new MainMenuView(
			    this::showBoard,
			    this::exitApp
			);
		SwingUtilities.invokeLater(() -> {
			swapCenterComponent(frame, menuView);
			updateMessagePanel();
		});
	}

	@Override
	public void run() {
		if (exitRequested) return;
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(() -> {
				buildUI();
				if (menuView == null) menuView = new MainMenuView(
					    this::showBoard,
					    this::exitApp
					);
				SwingUtilities.invokeLater(() -> {
					swapCenterComponent(frame, menuView);
				});
			});
		} else {
			buildUI();
			if (menuView == null) menuView = new MainMenuView(
				    this::showBoard,
				    this::exitApp
				);
			swapCenterComponent(frame, menuView);
		}
	}

	@Override
	public void showMessage(String message) {
		addMessage(message, MessageType.INFO);
	}

	@Override
	public void showError(String message) {
		addMessage(message, MessageType.ERROR);
		if (frame != null) {
			JOptionPane.showMessageDialog(
			    frame,
			    message,
			    "Error",
			    JOptionPane.ERROR_MESSAGE
			);
		}
	}

	@Override
	public void showPlayerInfo(String playerColor, LocalTime timeConsumed) {
		if (boardView != null) {
			boardView.updatePlayerInfo(playerColor, timeConsumed);
		}
	}

	@Override
	public void showMoveStatus(MoveStatus status) {
		if (messagePanel != null) {
			if (status != MoveStatus.Ok) {
				showError(status.info);
			}
		}
	}

	@Override
	public void showGameEnd(String message) {
		showMessage("Game over: " + message);
		if (frame != null) {
			frame.setTitle(frame.getTitle() + " - Game Over");
			JOptionPane.showMessageDialog(
			    frame,
			    message,
			    "Game Over",
			    JOptionPane.INFORMATION_MESSAGE
			);
		}
	}

	@Override
	public void showCheckWarning() {
		showMessage("Check!");
		if (frame != null) {
			JOptionPane.showMessageDialog(
			    frame,
			    "You are in check!",
			    "Check",
			    JOptionPane.WARNING_MESSAGE
			);
		}
	}

	@Override
	public void addMoveListener(MoveListener listener) {
		moveListeners.add(listener);
	}

	@Override
	public void addMenuListener(StateListener listener) {
		menuListeners.add(listener);
	}

	/**
	 * Switch the main content to the board view on the EDT.
	 */
	private void showBoard() {
		notifyMenuListeners(State.START);

		if (frame == null) buildUI();
		if (boardView == null) {
			boardView = new BoardView(
			    this::notifyMoveListeners,
			    this::endGame,
			    this
			);
		}
		SwingUtilities.invokeLater(() -> {
			swapCenterComponent(frame, boardView);
			updateMessagePanel();
		});
	}

	private void endGame() {
		notifyMenuListeners(State.END);
		showMainMenu();
		updateMessagePanel();
	}

	/**
	 * Request application exit: notify listeners and close the UI.
	 */
	private void exitApp() {
		exitRequested = true;
		notifyMenuListeners(State.EXIT);
		if (frame != null) {
			SwingUtilities.invokeLater(() -> {
				frame.dispose();
			});
		}
	}
}
