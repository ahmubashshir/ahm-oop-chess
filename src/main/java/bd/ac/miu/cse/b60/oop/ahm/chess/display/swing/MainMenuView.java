package bd.ac.miu.cse.b60.oop.ahm.chess.display.swing;

import java.awt.*;
import javax.swing.*;

/**
 * MainMenuView is a reusable Swing {@code JPanel} that provides the main menu interface
 * for the chess application, allowing users to start a new game or exit.
 *
 * <b>Overview:</b>
 * <ul>
 *   <li>Displays "Start" and "Exit" options in a simple, embeddable panel.</li>
 *   <li>Integrates with {@link SwingDisplay} or any JFrame-based chess UI.</li>
 *   <li>Uses callbacks to notify the application when an option is selected.</li>
 * </ul>
 *
 * <b>Integration:</b>
 * <ul>
 *   <li>Designed to be placed inside the main application window (not as a modal dialog).</li>
 *   <li>Works seamlessly with event-driven architectures; callbacks allow flexible control flow.</li>
 * </ul>
 *
 * <b>Public API:</b>
 * <ul>
 *   <li>{@link MainMenuView#MainMenuView(Runnable, Runnable)}: Constructor with callbacks for Start and Exit.</li>
 *   <li>{@link MainMenuView#MainMenuView()}: Default constructor (no callbacks).</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *   MainMenuView menu = new MainMenuView(
 *       () -> showBoardView(),      // Called when Start is selected
 *       () -> exitApplication()     // Called when Exit is selected
 *   );
 *   frame.getContentPane().add(menu, BorderLayout.CENTER);
 * </pre>
 *
 * <b>Extension Notes:</b>
 * <ul>
 *   <li>Override or extend this panel to add more menu options or customize appearance.</li>
 *   <li>Callbacks can be chained or replaced to integrate with custom application logic.</li>
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
		    "OOPChess: Main Menu",
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
