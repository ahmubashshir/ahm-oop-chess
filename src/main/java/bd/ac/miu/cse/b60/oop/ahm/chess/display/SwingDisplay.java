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
 * captured pieces, player info and provides controls for starting/exiting the game.
 *
 * Note: This implementation is event-driven. Methods like {@link #getCoord(String)}
 * and {@link #showMainMenu()} are implemented to behave in a GUI-appropriate manner:
 * - {@link #showMainMenu()} shows a blocking dialog to select Start/Exit.
 * - {@link #getCoord(String)} returns {@code null} (coordinate acquisition is done via UI clicks).
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
		// Lazily create UI on run()/EDT
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
		frame = new JFrame("AHM Chess - Swing Display");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout(6, 6));

		// Create BoardView and embedded MainMenuView. Menu is shown first.
		boardView = new BoardView(() -> {
			notifyMenuListeners(State.END);
			showMainMenu();
		});
		// forward board move events to registered display listeners
		boardView.addMoveListener(this::notifyMoveListeners);

		// Create menu view with callbacks to swap to board or exit
		menuView = new MainMenuView(this::showBoard, this::exitApp);
		// Show menu initially
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

	// User interactions on the board are handled by BoardView. No per-cell logic is required here.
	// BoardView forwards move events to this display via the registered listeners.

	@Override
	public void updateBoard(final Game game) {
		if (boardView != null) {
			boardView.updateBoard(game);
		}
	}

	// Symbol mapping is implemented inside BoardView. Left here only for compatibility comment.

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
		// Ensure boardView exists and forwards its move events to registered listeners.
		if (boardView == null) {
			boardView = new BoardView(() -> {
				notifyMenuListeners(State.END);
				showMainMenu();
			});
			boardView.addMoveListener(this::notifyMoveListeners);
		}
		SwingUtilities.invokeLater(() -> {
			frame.getContentPane().removeAll();
			frame.getContentPane().add(boardView, BorderLayout.CENTER);
			frame.revalidate();
			frame.repaint();
		});
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
