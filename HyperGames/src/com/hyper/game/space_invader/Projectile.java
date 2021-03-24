package com.hyper.game.space_invader;

import java.awt.Color;
import java.awt.Graphics;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;

import com.hyper.game.Entity;
import com.hyper.game.Game;

public class Projectile extends Entity<SpaceInvaders> {
	public static final int SPEED = 5, HEIGHT = 20;
	
	private boolean enemy;
	
	public Projectile(SpaceInvaders game, Vector2f position, boolean enemy) {
		super(position, game);
		this.enemy = enemy;
	}

	@Override
	public void update() {
		this.position.y += enemy?-SPEED:SPEED;
	}

	@Override	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		Vector2i graphCoords = Game.toGraphicalCoords(position, g);
		g.drawLine(graphCoords.x, graphCoords.y-HEIGHT/2, graphCoords.x, graphCoords.y+HEIGHT/2);
	}

	public boolean collidesWith(Enemy e) {
		return Math.abs(e.getPosition().x() - this.position.x) <= 10 && Math.abs(this.position.y - e.getPosition().y()) <= 10 + HEIGHT/2;
	}

	public boolean isEnemy() {
		return enemy;
	}

	public boolean collidesWith(Vector2fc position) {
		return (int)position.x() == (int)this.position.x && Math.abs(this.position.y - position.y()) <= HEIGHT/2;
	}
}
