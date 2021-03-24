package com.hyper.game;

public enum Rotation {
	UP,
	RIGHT,
	DOWN,
	LEFT;

	private Rotation() {}

	public Rotation rotate(boolean clockwise) {
		if(this == LEFT && clockwise)
			return UP;
		if(this == UP && !clockwise)
			return LEFT;
		int i = this.ordinal();
		if(clockwise)
			return values()[i+1];
		return values()[i-1];
	}
}
