package com.hyper.game.pong;

import static com.hyper.game.pong.Paddle.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;

import com.hyper.game.Game;
import com.hyper.hypergames.Window;
import com.hyper.io.KeyHandler;

public class Pong extends Game {
	public static final int HEIGHT = 1000, WIDTH = 1500,  BALL_SIZE = 10;

	private int score1 = 0, score2 = 0;

	private Ball balls[] = new Ball[20], ball2;

	private Paddle p1, p2;

	public Pong(Window instance) {
		super(instance);
	}

	@Override
	public void resetGame() {

	}

	@Override
	public void init() {
		for(int i = 0; i < 20; i++)
			this.balls[i] = new Ball(new Vector2f(), this);
		this.ball2 = new Ball(new Vector2f(), this);

		this.p1 = new Paddle(new Vector2f(20-WIDTH/2, 0), this);
		this.p2 = new Paddle(new Vector2f(-20+WIDTH/2, 0), this);
	}

	@Override
	public void update() {
		handleInputs();

		this.p1.update();
		this.p2.update();

		for(Ball ball : balls)
			ball.update();
		this.ball2.update();
		handleCollisions();

		if(score1 > 100)
			JOptionPane.showMessageDialog(this, "Game over yeaaaah ! p1 a gagné");
		if(score2 > 100)
			JOptionPane.showMessageDialog(this, "Game over yeaaaah ! p2 a gagné");
	}

	@Override
	public void paint(Graphics g) {
		Rectangle r = g.getClipBounds();
		g.setColor(Color.getHSBColor((float)Math.random()/16, (float)Math.random()/16, (float)Math.random()/16));
		g.fillRect(0, 0, r.width, r.height);
		Vector2i rect = toGraphicalCoords(new Vector2f(-WIDTH/2, HEIGHT/2), g);
		g.setColor(Color.WHITE);
		g.drawRect(rect.x, rect.y, WIDTH, HEIGHT);

		for(Ball ball : balls)
			ball.render(g);
		ball2.render(g);

		Vector2i score = toGraphicalCoords(new Vector2f(-200, 100), g);
		g.setColor(Color.WHITE);
		g.drawString("dab", 20, 20);
		g.drawString("" + score1, score.x, score.y);
		g.drawString("" + score2, score.x+400, score.y);
		p1.render(g);
		p2.render(g);
	}

	private void handleCollisions() {
		//		check paddle 1
		for(Ball ball : balls)
			if(ball.getMotion().x() < 0) {
				if(distSquared(ball.getPosition(), this.p1.getPosition()) < BALL_SIZE*BALL_SIZE/4) {
					ball.bounce();
					this.won(false);
				}
			} else {
				//check paddle 2
				if(distSquared(ball.getPosition(), this.p2.getPosition()) < BALL_SIZE*BALL_SIZE/4) {
					ball.bounce();
					this.won(true);
				}
			}
	}

	private float distSquared(Vector2fc point, Vector2fc rectCentre) {
		float dx = Math.max(Math.abs(point.x()-rectCentre.x()) - PADDLE_WIDTH/2, 0),
				dy = Math.max(Math.abs(point.y()-rectCentre.y()) - PADDLE_HEIGHT/2, 0);
		return dx*dx + dy*dy;
	}

	private void handleInputs() {
		KeyHandler handler = this.instance.getKeyHandler();
		if(handler.isKeyDown(KeyEvent.VK_A))
			this.p1.setPosition(new Vector2f(p1.getPosition()).add(0, PADDLE_SPEED));
		if(handler.isKeyDown(KeyEvent.VK_W))
			this.p1.setPosition(new Vector2f(p1.getPosition()).add(0, -PADDLE_SPEED));
		if(handler.isKeyDown(KeyEvent.VK_UP))
			this.p2.setPosition(new Vector2f(p2.getPosition()).add(0, PADDLE_SPEED));
		if(handler.isKeyDown(KeyEvent.VK_DOWN))
			this.p2.setPosition(new Vector2f(p2.getPosition()).add(0, -PADDLE_SPEED));
	}

	public void won(boolean isP1) {
		if(isP1)
			this.score1++;
		else
			this.score2++;
	}

	@Override
	public void loadResources() throws IOException {

	}

	@Override
	public String getName() {
		return "Pong";
	}
}
