package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import game.RunningBronto;

/**
 * Menubar that offers functionality like creating a new
 * lighthouse connection.
 * @author PurifyPioneer
 * @version 1.0
 * @since 1.0
 */
public class MenuBar extends JMenuBar{

	private JMenu baseMenu;
	private JMenuItem baseItem;
	private JMenuItem item_addGame;
	private JMenuItem item_toggleInfo;
	private JMenuItem item_toggleGrid;
	private JMenuItem item_toggleBounding;
	private JMenuItem item_addLighthouseView;
	private JMenuItem item_addLighthouseViewDemo;

	/**
	 * COnstructs the menu bar
	 * @param g game to interact with
	 */
	public MenuBar(Game g) {
		
		baseMenu = new JMenu("Start");
		baseItem = new JMenuItem("Exit Game");
		baseItem.setToolTipText("Close game");
		baseItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		item_toggleGrid = new JMenuItem("Toggle info");
		item_toggleGrid.setToolTipText("Toggles the info.");
		item_toggleGrid.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				((RunningBronto) g).toggleInfo();				
			}
		});
		
		item_toggleInfo = new JMenuItem("Toggle grid");
		item_toggleInfo.setToolTipText("Toggles the grid.");
		item_toggleInfo.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				((RunningBronto) g).toggleGrid();				
			}
		});
		
		item_toggleBounding = new JMenuItem("Toggle bounding-boxes");
		item_toggleBounding.setToolTipText("Toggles the bounding-boxes.");
		item_toggleBounding.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				((RunningBronto) g).toggleBounding();				
			}
		});
		
		item_addGame = new JMenuItem("New game");
		item_addGame.setToolTipText("Open a new game.");
		item_addGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DisplayFrame().addGame(new RunningBronto());;
			}
		});
		
		item_addLighthouseView = new JMenuItem("Add Lh-View");
		item_addLighthouseView.setToolTipText("Create a new connection to remote lh-display.");
		item_addLighthouseView.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
					RunningBronto.setPaused(true);
					RemoteInfoDialog d = new RemoteInfoDialog();
					((RunningBronto) g).createLighthouseView(d.getHost(), d.getPort());
			}
		});
		
		item_addLighthouseViewDemo = new JMenuItem("Add Lh-View DEMO");
		item_addLighthouseViewDemo.setToolTipText("<html>Opens a new browser windows with lh-demo.<br>"
													+ "Works only on windows currently.</html>");
		item_addLighthouseViewDemo.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {

					try {
						((RunningBronto) g).createLighthouseViewDemo();
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}

			}
		});
		
		baseMenu.add(baseItem);
		baseMenu.add(item_toggleInfo);
		baseMenu.add(item_toggleGrid);
		baseMenu.add(item_toggleBounding);
		baseMenu.add(item_addGame);
		baseMenu.add(item_addLighthouseView);
		baseMenu.add(item_addLighthouseViewDemo);
		
		this.add(baseMenu);
	}
	
}

