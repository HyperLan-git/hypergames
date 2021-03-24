package com.hyper.game.tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import com.hyper.game.Entity;
import com.hyper.game.Game;
import com.hyper.game.Rotation;

public class Piece extends Entity<Tetris> {
	private Pattern pattern;

	private Rotation rotation;

	private Color color;

	public Piece(Vector2f position, Tetris instance, Pattern pattern, Color color) {
		super(position, instance);
		this.pattern = pattern;
		this.color = color;
		this.rotation = Rotation.UP;
	}

	@Override
	public void update() {
		this.position.y -= this.getGame().getSpeed();
	}

	@Override
	public void render(Graphics g) {
		BufferedImage temp = getGame().clone(getGame().getBlockTexture());
		getGame().setColor(temp, color);
		for(Vector2i block : getBlocks()) {
			Vector2i pos = Game.toGraphicalCoords(
					this.getGame().isDiscrete()?
							new Vector2f(this.position.x + (block.x-Tetris.WIDTH/2.0f)*20,
									(float) (Math.ceil((this.position.y-5)/20)*20 + (block.y-Tetris.HEIGHT/2.0f)*20)):
										new Vector2f(this.position.x + (block.x-Tetris.WIDTH/2.0f)*20,
												this.position.y + (block.y-Tetris.HEIGHT/2.0f)*20), g);
			g.drawImage(temp, pos.x, pos.y, null);
		}
	}

	public void rotate(boolean clockwise) {
		this.rotation = this.rotation.rotate(clockwise);
	}

	private Vector2i rotate(Vector2ic v, int quarterTurns) {
		Vector2f result = new Vector2f(v.x(), v.y());
		for(int i = 0; i < quarterTurns; i++)
			result.perpendicular();
		return new Vector2i((int)result.x, (int)result.y);
	}

	public Vector2i[] getBlocks() {
		Vector2i[] result = this.pattern.getBlocks().clone();
		for(int i = 0; i < result.length; i++)
			result[i] = rotate(this.pattern.getBlocks()[i], this.rotation.ordinal());
		return result;
	}

	public int getColor() {
		return this.color.getRGB();
	}

	public void move(boolean right) {
		if(right)
			this.position.x += 20;
		else
			this.position.x -= 20;
	}
}
