package bd.ac.miu.cse.b60.oop.ahm.chess.display.swing;

import bd.ac.miu.cse.b60.oop.ahm.chess.Display;
import java.awt.*;
import javax.swing.*;

/**
 * Embedded main menu panel for the chess application.
 *
 * <b>Responsibilities:</b>
 * <ul>
 *   <li>Presents Start / Exit options to the user.</li>
 *   <li>Notifies registered {@link Display.StateListener}s and invokes callbacks when an option is selected.</li>
 *   <li>Intended to be placed inside the application's main window, not as a modal dialog.</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *   MainMenuView menu = new MainMenuView(
 *       () -> showBoardView(),      // onStart callback
 *       () -> exitApplication()     // onExit callback
 *   );
 *   frame.getContentPane().add(menu, BorderLayout.CENTER);
 * </pre>
 *
 * <b>Event Flow:</b>
 * <ul>
 *   <li>When "Start" is clicked, the onStart callback is invoked.</li>
 *   <li>When "Exit" is clicked, the onExit callback is invoked.</li>
 * </ul>
 */
public class MainMenuView extends JPanel {

	private final Runnable onStart;
	private final Runnable onExit;

	/**
	 * Create an embedded main menu panel.
	 *
	 * @param onStart callback invoked when Start is chosen
	 * @param onExit callback invoked when Exit is chosen
	 */
	public MainMenuView(Runnable onStart, Runnable onExit) {
		this.onStart = onStart;
		this.onExit = onExit;
		initializeComponents();
	}

	/**
	 * Default constructor for embedding without external callbacks.
	 */
	public MainMenuView() {
		this(null, null);
	}

	private void initializeComponents() {
		setLayout(new BorderLayout(12, 12));
		setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		JLabel header = new JLabel(
		    "AHM CHESS GAME - MAIN MENU",
		    SwingConstants.CENTER
		);
		header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
		add(header, BorderLayout.NORTH);

		JPanel center = new JPanel(new GridLayout(0, 1, 8, 8));
		center.setBorder(BorderFactory.createEmptyBorder(6, 24, 6, 24));

		JButton startBtn = new JButton("Start a new game");
		JButton exitBtn = new JButton("Exit");

		startBtn.addActionListener(e -> {
			if (onStart != null) onStart.run();
		});

		exitBtn.addActionListener(e -> {
			if (onExit != null) onExit.run();
		});

		center.add(startBtn);
		center.add(exitBtn);

		add(center, BorderLayout.CENTER);

		JLabel hint = new JLabel(
		    "Choose an option to continue",
		    SwingConstants.CENTER
		);
		hint.setBorder(BorderFactory.createEmptyBorder(6, 6, 12, 6));
		add(hint, BorderLayout.SOUTH);
	}
}
