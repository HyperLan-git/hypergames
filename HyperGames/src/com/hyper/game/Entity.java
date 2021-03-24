package com.hyper.game;

import java.awt.Graphics;

import org.joml.Vector2f;
import org.joml.Vector2fc;

public abstract class Entity <T extends Game> {
	protected Vector2f position;
	
	private T instance;
	
	public Entity(Vector2f position, T instance) {
		this.position = position;
		this.instance = instance;
	}

	public abstract void update();

	public abstract void render(Graphics g);

	public final T getGame() {
		return this.instance;
	}

	public final Vector2fc getPosition() {
		return position;
	}

	public final void setPosition(Vector2f pos) {
		position = pos;
	}
}
