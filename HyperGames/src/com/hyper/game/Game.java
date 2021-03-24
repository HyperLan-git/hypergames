package com.hyper.game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;

import javax.swing.JPanel;

import org.joml.Vector2fc;
import org.joml.Vector2i;

import com.hyper.hypergames.Window;

public abstract class Game extends JPanel {
	protected Window instance;
	
	public Game(Window instance) {
		this.instance = instance;
	}

	public static final Vector2i toGraphicalCoords(Vector2fc vector2fc, Graphics g) {
		Rectangle dim = g.getClipBounds();
		return new Vector2i((int)(vector2fc.x() + dim.width/2), (int)(-vector2fc.y() + dim.height/2));
	}
	
	public final void reset() {
		this.instance.getKeyHandler().resetAll();
		resetGame();
	}
	
	public abstract void resetGame();

	public abstract void init();

	public abstract void update();
	
	@Override
	public abstract String getName();

	@Override
	public abstract void paint(Graphics g);

	public abstract void loadResources() throws IOException;
}
