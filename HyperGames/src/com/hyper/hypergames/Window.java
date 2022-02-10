package com.hyper.hypergames;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.hyper.game.Game;
import com.hyper.game.pong.Pong;
import com.hyper.game.space_invader.SpaceInvaders;
import com.hyper.game.tetris.Tetris;
import com.hyper.io.KeyHandler;

public class Window extends JFrame {
	private static final long serialVersionUID = -7952681873884432960L;

	public final Game[] games = new Game[] {
			new SpaceInvaders(this), new Tetris(this), new Pong(this)
	};

	private final float fps;

	private Game currentGame = null;

	private KeyHandler keyHandler = new KeyHandler(this);

	public Window(float fps) {
		this.fps = fps;
		this.setMinimumSize(new Dimension(700, 500));
		this.setContentPane(new MainMenu(this, games));
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void run() {
		double lastFrameTime = getTimeSeconds();

		while(true) {
			if(lastFrameTime + 1/fps <= getTimeSeconds()) {
				this.update();
				lastFrameTime += 1/fps;
			}
			this.repaint();
		}
	}

	private void update() {
		if(this.currentGame != null)
			this.currentGame.update();
		this.keyHandler.update();
	}

	public void setCurrentGame(Game game) {
		game.init();
		this.currentGame = game;
		this.setContentPane(currentGame);
		this.revalidate();
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

	public static double getTimeSeconds() {
		return (double)System.nanoTime() / 1000000000;
	}
}
