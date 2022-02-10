package com.hyper.game.space_invader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.hyper.game.Entity;
import com.hyper.game.Game;
import com.hyper.hypergames.Window;
import com.hyper.io.ResourceLocation;

public class SpaceInvaders extends Game {
	private static final long serialVersionUID = 2315300006444781168L;
	public static final int WIDTH = 800, PLAYER_SPEED = 6, PLAYER_Y = -200, SHOOT_TIME = 0;
	public static final ResourceLocation ENEMY_TEXTURE = new ResourceLocation("textures/fighter.png"),
			PLAYER_TEXTURE = new ResourceLocation("textures/spaceship.png");

	private Image enemyTexture, playerTexture;

	private float playerX;

	private int cooldown = 0, level;

	private boolean gameOver;

	private ArrayList<Enemy> enemies;
	private ArrayList<Projectile> projectiles;

	public SpaceInvaders(Window instance) {
		super(instance);
	}

	@Override
	public void init() {
		this.playerX = 0;
		this.level = -1;
		this.gameOver = false;

		this.enemies = new ArrayList<>();
		this.projectiles = new ArrayList<>();

		nextLevel();
	}

	@Override
	public void resetGame() {
		init();
	}

	@Override
	public void update() {
		if(this.cooldown > 0)
			this.cooldown--;

		if(this.gameOver) {
			JOptionPane.showMessageDialog(this, "Game over yeaaaah !");
			this.reset();
		}

		if(this.instance.getKeyHandler().isKeyDown(KeyEvent.VK_RIGHT))
			this.playerX += PLAYER_SPEED;
		if(this.instance.getKeyHandler().isKeyDown(KeyEvent.VK_LEFT))
			this.playerX -= PLAYER_SPEED;
		if(this.playerX > WIDTH/2)
			this.playerX = WIDTH/2;
		if(this.playerX < -WIDTH/2)
			this.playerX = -WIDTH/2;
		if(this.instance.getKeyHandler().isKeyDown(KeyEvent.VK_SPACE) && this.cooldown == 0)
			shoot();
		synchronized(this) {
			if(enemies.isEmpty())
				nextLevel();
			boolean shouldGoBack = false;
			for(Enemy e : enemies) {
				e.update();
				if(!shouldGoBack && e.getPosition().x() >= WIDTH/2 || e.getPosition().x() <= -WIDTH/2)
					shouldGoBack = true;
			}

			if(shouldGoBack) for(Enemy e : enemies)
				e.goBack();

			ArrayList<Entity<SpaceInvaders>> toKill = new ArrayList<>();
			for(Projectile e : projectiles) {
				e.update();
				if(Math.abs(e.getPosition().y()) >= 1000) {
					toKill.add(e);
					continue;
				}
				if(e.isEnemy() && !this.gameOver && Math.abs(e.getPosition().x()-this.playerX) < 5 && Math.abs(e.getPosition().y()-PLAYER_Y) < 5)
					this.gameOver();
				for(Enemy enemy : enemies) if(e.collidesWith(enemy) && !e.isEnemy()) {
					toKill.add(enemy);
					toKill.add(e);
					break;
				}
			}
			for(Entity<SpaceInvaders> e : toKill) {
				if(e instanceof Enemy)
					this.enemies.remove(e);
				else if(e instanceof Projectile)
					this.projectiles.remove(e);
			}
		}
	}

	private void gameOver() {
		this.gameOver = true;
	}

	@Override
	public void paint(Graphics g) {
		Rectangle dim = g.getClipBounds();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, dim.width, dim.height);
		g.setColor(Color.WHITE);
		synchronized(this) {
			for(Enemy e : enemies) {
				Vector2i coords = Game.toGraphicalCoords(e.getPosition(), g);
				g.drawImage(enemyTexture, coords.x-10, coords.y-10, null);
			}
			for(Projectile e : projectiles)
				e.render(g);
		}
		Vector2i playerPos = toGraphicalCoords(new Vector2f(this.playerX, PLAYER_Y), g);
		if(!this.gameOver)
			g.drawImage(playerTexture, playerPos.x-10, playerPos.y-10, null);
	}

	private void shoot() {
		this.cooldown = SHOOT_TIME;
		if(!this.gameOver) synchronized(this) {
			this.projectiles.add(new Projectile(this, new Vector2f(this.playerX, PLAYER_Y), false));
		}
	}

	public double getShootProbability() {
		return 0.005;
	}

	public void enemyShot(Enemy enemy) {
		this.projectiles.add(new Projectile(this, new Vector2f(enemy.getPosition()), true));
	}

	private void nextLevel() {
		level++;
		for(int x = 0; x < 10; x++)
			for(int y = 0; y < level + 10; y++)
				this.enemies.add(new Enemy(new Vector2f((x-2.5f)*WIDTH/20, -y*30 + 300), this, 1 + level*0.2f));
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	@Override
	public void loadResources() throws IOException {
		enemyTexture = ImageIO.read(ENEMY_TEXTURE.getAsStream()).getScaledInstance(20, 20, Image.SCALE_FAST);
		playerTexture = ImageIO.read(PLAYER_TEXTURE.getAsStream()).getScaledInstance(20, 20, Image.SCALE_FAST);
	}

	@Override
	public String getName() {
		return "Space invaders";
	}
}
