package com.hyper.game.tetris;

import org.joml.Vector2i;

public class Pattern {
	private Vector2i[] blocks;

	public Pattern(Vector2i[] blocks) {
		this.blocks = blocks;
	}

	public Vector2i[] getBlocks() {
		return this.blocks;
	}

	public boolean isInside(Vector2i relativePos) {
		for(Vector2i v : blocks) if(relativePos.distance(v) == 0)
			return true;
		return false;
	}
}
