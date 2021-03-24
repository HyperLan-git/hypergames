package com.hyper.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.JFrame;

public class KeyHandler implements KeyListener {
	private HashMap<Integer, KeyState> keys = new HashMap<>();

	public KeyHandler(JFrame toListen) {
		toListen.addKeyListener(this);
		for(int i = KeyEvent.VK_SPACE; i < KeyEvent.VK_Z+1; i++)
			keys.put(i, null);
	}

	public void resetAll() {
		for(Integer i : keys.keySet())
			keys.replace(i, null);
	}

	public void update() {
		for(Integer i : keys.keySet()) {
			if(keys.get(i) == null)
				continue;
			switch(keys.get(i)) {
			case PRESSED:
				keys.replace(i, KeyState.DOWN);
				break;
			case RELEASED:
				keys.replace(i, null);
			default:
			}
		}		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.keys.replace(e.getKeyCode(), KeyState.PRESSED);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.keys.replace(e.getKeyCode(), KeyState.RELEASED);
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	public boolean isKeyPressed(int keyCode) {
		return KeyState.PRESSED.equals(keys.get(new Integer(keyCode)));
	}

	public boolean isKeyDown(int keyCode) {
		return KeyState.DOWN.equals(keys.get(new Integer(keyCode))) || isKeyPressed(keyCode);
	}

	public boolean isKeyReleased(int keyCode) {
		return KeyState.RELEASED.equals(keys.get(new Integer(keyCode)));
	}
}
