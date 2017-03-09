package core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Frame that can hold a {@link Game} and
 * offers some predefined behavior.
 * 
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class DisplayFrame {

	// Default properties of the DisplayFrame
	private final String title = "Display Frame";
	private final int width = 800;
	private final int height = 450;
	
	// Not extending JFrame because encapsulation
	private JFrame frame = null;

	private Game game;
	
	/**
	 * Constructs a new {@link DisplayFrame} with
	 * default values for width, height and title.
	 */
	public DisplayFrame() {
		
		// Create new JFrame and set default properties
		frame = new JFrame();
		frame.setTitle(title);
		frame.setSize(width, height);
		frame.getContentPane().setLayout(new BorderLayout());
		
		//--Just some fancy stuff-- (Display text when no game is added)
//		JLabel lblText = new JLabel("Add a game for more fun.");
//		lblText.setHorizontalAlignment(JLabel.CENTER);
//		frame.setMinimumSize(new Dimension(250, 100));
//		frame.getContentPane().add(lblText, BorderLayout.CENTER);
		//-------------------------
		
		// Change window closing behavior
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WindowListener winListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int answer = JOptionPane.showConfirmDialog(frame,
						"Wollen sie das Programm wirklich beenden ?",
						"Wirklich beenden ?", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (answer == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		};
		frame.addWindowListener(winListener);
		frame.setResizable(true);
		//frame.setUndecorated(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	

	/**
	 * Constructs a new {@link DisplayFrame} with default title.
	 * @param width
	 * @param height
	 */
	public DisplayFrame(int width, int height){		
		this();
		frame.setSize(width, height);
	}
	
	/**
	 * Constructs a new {@link DisplayFrame}.
	 * @param width
	 * @param height
	 * @param title
	 */
	public DisplayFrame(int width, int height, String title) {		
		this(width, height);
		frame.setTitle(title);
	}	
	
	/**
	 * Removes all content from the current frame and
	 * add a game to the frame.
	 * @param game
	 */
	public void addGame(Game game) {
		this.game = game;
		frame.setTitle(game.getTitle());
		frame.getContentPane().removeAll();
		frame.getContentPane().add(new MenuBar(game), BorderLayout.PAGE_START);
		frame.getContentPane().add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	public Game getGame()  {
		return this.game;
	}
}
