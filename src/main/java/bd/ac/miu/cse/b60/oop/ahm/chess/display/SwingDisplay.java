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
	private void buildUI() {
		frame = new JFrame("OOPChess: Swing UI");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout(6, 6));

		boardView = new BoardView(this::notifyMoveListeners, this::endGame);
		menuView = new MainMenuView(this::showBoard, this::exitApp);
		frame.add(menuView, BorderLayout.CENTER);

		// Window closing should notify menu listeners with EXIT
		frame.addWindowListener(
		new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitRequested = true;
				notifyMenuListeners(State.EXIT);
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

	@Override
	public void showMainMenu() {
		if (frame == null) buildUI();
		if (menuView == null) menuView = new MainMenuView(
			    this::showBoard,
			    this::exitApp
			);
		SwingUtilities.invokeLater(() -> {
			frame.getContentPane().removeAll();
			frame.getContentPane().add(menuView, BorderLayout.CENTER);
			frame.revalidate();
			frame.repaint();
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
					frame.getContentPane().removeAll();
					frame.getContentPane().add(menuView, BorderLayout.CENTER);
					frame.revalidate();
					frame.repaint();
				});
			});
		} else {
			buildUI();
			if (menuView == null) menuView = new MainMenuView(
				    this::showBoard,
				    this::exitApp
				);
			frame.getContentPane().removeAll();
			frame.getContentPane().add(menuView, BorderLayout.CENTER);
			frame.revalidate();
			frame.repaint();
		}
	}

	@Override
	public void showMessage(String message) {
		if (boardView != null) {
			boardView.showMessage(message);
		}
	}

	@Override
	public void showError(String message) {
		if (boardView != null) {
			boardView.showError(message);
		}
	}

	@Override
	public void showPlayerInfo(String playerColor, LocalTime timeConsumed) {
		if (boardView != null) {
			boardView.showPlayerInfo(playerColor, timeConsumed);
		}
	}

	@Override
	public void showMoveStatus(MoveStatus status) {
		if (boardView != null) {
			if (status == MoveStatus.Ok) {
				boardView.showMessage("Move succeeded");
			} else {
				boardView.showError(status.info);
			}
		}
	}

	@Override
	public void showGameEnd(String message) {
		if (boardView != null) {
			boardView.showGameEnd(message);
		}
		if (frame != null) {
			frame.setTitle(frame.getTitle() + " - Game Over");
		}
	}

	@Override
	public void showCheckWarning() {
		if (boardView != null) {
			boardView.showCheckWarning();
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
			boardView = new BoardView(this::notifyMoveListeners, this::endGame);
		}
		SwingUtilities.invokeLater(() -> {
			frame.getContentPane().removeAll();
			frame.getContentPane().add(boardView, BorderLayout.CENTER);
			frame.revalidate();
			frame.repaint();
		});
	}

	private void endGame() {
		notifyMenuListeners(State.END);
		showMainMenu();
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
