package com.hyper.game.pong;

import java.awt.Color;
import java.awt.Graphics;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;

import com.hyper.game.Entity;
import com.hyper.game.Game;

public class Ball extends Entity<Pong> {
	public static final float BASE_SPEED = -5, SPEED_MUL = -1.1f, MAX_SPEED = 9001;
	private Vector2f motion = null;

	public Ball(Vector2f position, Pong instance) {
		super(position, instance);
	}

	@Override
	public void update() {
		if(motion == null)
			randomSpeed();
		this.position.add(motion);
		if(this.position.y < -Pong.HEIGHT/2 || this.position.y > Pong.HEIGHT/2)
			this.motion.y = -this.motion.y;
		if(this.position.x > Pong.WIDTH/2) {
//			this.getGame().won(true);
			this.position = new Vector2f();
			randomSpeed();
		}
		if(this.position.x < -Pong.WIDTH/2) {
//			this.getGame().won(false);
			this.position = new Vector2f();
			randomSpeed();
		}
	}

	@Override
	public void render(Graphics g) {
		Vector2i coords = Game.toGraphicalCoords(this.position, g);
		g.setColor(Color.WHITE);
		g.fillOval(coords.x-Pong.BALL_SIZE/2, coords.y-Pong.BALL_SIZE/2, Pong.BALL_SIZE, Pong.BALL_SIZE);
	}

	private void randomSpeed() {
		float angle = 0;
		do {
			angle = (float) (Math.random()*Math.PI);
		} while(Math.abs(angle - Math.PI/2) <= Math.PI/8);
		if(Math.random() < 0.5)
			angle = -angle;
		this.motion = new Vector2f((float)Math.cos(angle), (float)Math.sin(angle)).mul(BASE_SPEED);
	}

	public void bounce() {
		boolean xplus = motion.x > 0;
		float newSpeed = -this.motion.length()*SPEED_MUL;
		newSpeed = Math.abs(newSpeed);
		if(newSpeed > MAX_SPEED)
			newSpeed = MAX_SPEED;
		randomSpeed();
		motion.x = Math.abs(motion.x);
		motion.y = Math.abs(motion.y);
		motion.normalize().mul(xplus?-newSpeed:newSpeed);
	}

	public Vector2fc getMotion() {
		return this.motion;
	}
}
