package com.hyper.game.pong;

import java.awt.Color;
import java.awt.Graphics;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.hyper.game.Entity;
import com.hyper.game.Game;

public class Paddle extends Entity<Pong> {
	public static final int PADDLE_WIDTH = 10, PADDLE_HEIGHT = 200;
	public static final float PADDLE_SPEED = 10f;

	public Paddle(Vector2f position, Pong instance) {
		super(position, instance);
	}

	@Override
	public void update() {
		if(this.position.y > Pong.HEIGHT/2 - PADDLE_HEIGHT/2)
			this.position.y = Pong.HEIGHT/2 - PADDLE_HEIGHT/2;
		if(this.position.y < -Pong.HEIGHT/2 + PADDLE_HEIGHT/2)
			this.position.y = -Pong.HEIGHT/2 + PADDLE_HEIGHT/2;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		Vector2i coords = Game.toGraphicalCoords(position, g);
		g.fillRect(coords.x-PADDLE_WIDTH/2, coords.y-PADDLE_HEIGHT/2, PADDLE_WIDTH, PADDLE_HEIGHT);
	}

}
