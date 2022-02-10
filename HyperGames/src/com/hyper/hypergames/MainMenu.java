package com.hyper.hypergames;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.hyper.game.Game;

public class MainMenu extends JPanel implements ActionListener {
	private static final long serialVersionUID = 8583981980544232702L;

	private Window window;
	
	private Game[] games;

	private JButton[] buttons;

	public MainMenu(Window window, Game[] games) {
		this.window = window;
		this.games = games;
		this.buttons = new JButton[games.length];
		for(int i = 0; i < games.length; i++) {
			try {
				games[i].loadResources();
			} catch (IOException e) {
				System.err.println("Error thrown when loading resources");
				e.printStackTrace();
				System.exit(1);
			}
			this.buttons[i] = new JButton(games[i].getName());
			this.buttons[i].setFocusable(false);
			this.add(this.buttons[i]);
			this.buttons[i].addActionListener(this);
		}
		
		FlowLayout layout = new FlowLayout();
		layout.setHgap(10000);
		layout.setVgap(30);
		this.setLayout(layout);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i = 0; i < this.buttons.length; i++) if(e.getSource().equals(buttons[i]))
				window.setCurrentGame(games[i]);
	}
}
