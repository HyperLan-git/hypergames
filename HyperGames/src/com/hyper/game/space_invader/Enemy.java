package com.hyper.game.space_invader;

import java.awt.Graphics;

import org.joml.Vector2f;

import com.hyper.game.Entity;

public class Enemy extends Entity<SpaceInvaders> {
	public static final float SPEED_INCREASE = 1f;

	private boolean headingRight = true;

	private float speed;

	public Enemy(Vector2f position, SpaceInvaders game, float baseSpeed) {
		super(position, game);
		this.speed = baseSpeed;
	}

	@Override
	public void render(Graphics g) {
		
	}

	@Override
	public void update() {
		if(this.headingRight)
			this.position.x += speed;
		else
			this.position.x -= speed;
		if(Math.random() < this.getGame().getShootProbability())
			this.getGame().enemyShot(this);
	}

	public void goBack() {
		this.headingRight = !headingRight;
		this.position.y--;
		this.speed += SPEED_INCREASE;

		if(speed > 500)
			speed = 500;
		if(this.headingRight)
			this.position.x += speed;
		else
			this.position.x -= speed;
	}
}
