package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import game.MapProject;

public class MenuBar extends JMenuBar{

	private JMenu baseMenu;
	private JMenuItem baseItem;
	private JMenuItem item_addGame;
	private JMenuItem item_addLighthouseView;

	public MenuBar() {
		
		baseMenu = new JMenu("Test");
		baseItem = new JMenuItem("Exit");
		baseItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		item_addGame = new JMenuItem("New game");
		item_addGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DisplayFrame().addGame(new MapProject());;
			}
		});

		
		item_addLighthouseView = new JMenuItem("Add Lh-View");
		item_addLighthouseView.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				//MenuBar.this.getParent()
			}
		});
		
		baseMenu.add(baseItem);
		baseMenu.add(item_addGame);
		
		this.add(baseMenu);
	}
	
}

