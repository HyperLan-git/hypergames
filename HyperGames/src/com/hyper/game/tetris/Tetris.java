package com.hyper.game.tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.joml.Vector2f;
import org.joml.Vector2i;

import com.hyper.game.Game;
import com.hyper.hypergames.Window;
import com.hyper.io.KeyHandler;
import com.hyper.io.ResourceLocation;

public class Tetris extends Game {
	private static final long serialVersionUID = 8910691708620850177L;

	static {
		LINE = new Pattern(new Vector2i[] {new Vector2i(), new Vector2i(0, 1), new Vector2i(0, -1), new Vector2i(0, -2)});
		T = new Pattern(new Vector2i[] {new Vector2i(), new Vector2i(0, 1), new Vector2i(-1, 0), new Vector2i(1, 0)});
		SQUARE = new Pattern(new Vector2i[] {new Vector2i(), new Vector2i(0, 1), new Vector2i(1, 0), new Vector2i(1, 1)});
		L_RIGHT = new Pattern(new Vector2i[] {new Vector2i(), new Vector2i(1, 0), new Vector2i(0, 1), new Vector2i(0, 2)});
		L_LEFT = new Pattern(new Vector2i[] {new Vector2i(), new Vector2i(-1, 0), new Vector2i(0, 1), new Vector2i(0, 2)});
		S_RIGHT = new Pattern(new Vector2i[] {new Vector2i(), new Vector2i(0, 1), new Vector2i(1, 1), new Vector2i(-1, 0)});
		S_LEFT = new Pattern(new Vector2i[] {new Vector2i(), new Vector2i(0, 1), new Vector2i(-1, 1), new Vector2i(1, 0)});
	}

	public static final int WIDTH = 10, HEIGHT = 20;

	public static final float BASE_SPEED = 0.3f, SPEED_INCREASE = 0.3f;

	public static final Pattern LINE, T, SQUARE, L_RIGHT, L_LEFT, S_RIGHT, S_LEFT,
	PATTERNS[] = new Pattern[] {LINE, T, SQUARE, L_RIGHT, L_LEFT, S_RIGHT, S_LEFT};

	public static final ResourceLocation BLOCK_TEXTURE = new ResourceLocation("textures/tetris_block.png");

	private BufferedImage blockTexture;

	private int[][] world;

	private boolean discrete;

	private Piece p;

	private Pattern next;

	private double score;

	private int level, nextColor, linesCleared;

	public Tetris(Window instance) {
		super(instance);
	}

	@Override
	public void resetGame() {
		init();
	}

	@Override
	public void loadResources() throws IOException {
		blockTexture = ImageIO.read(BLOCK_TEXTURE.getAsStream());
	}

	@Override
	public void init() {
		this.world = new int[WIDTH][HEIGHT];
		for(int x = 0; x < WIDTH; x++)
			for(int y = 0; y < HEIGHT; y++)
				this.world[x][y] = 0;
		this.level = 0;
		this.p = null;
		this.score = 0;
		this.next = randomPattern();
		this.nextColor = (int) (Math.random()*Integer.MAX_VALUE);
		int off = JOptionPane.showConfirmDialog(instance, "Tetris", "Jouer en d�placements continus ?", JOptionPane.YES_NO_OPTION);
		this.discrete = off == 1;
	}

	@Override
	public void update() {
		this.linesCleared = 0;
		KeyHandler keyHandler = instance.getKeyHandler();
		if(this.p == null) {
			this.p = new Piece(new Vector2f(WIDTH*10, HEIGHT * 20), this, this.next,
					new Color(nextColor).brighter());
			while(hasCollided()) {
				this.p.update();
				if(this.p.getPosition().y() < (HEIGHT-4) * 20) {
					gameOver();
					return;
				}
			}
			this.next = randomPattern();
			this.nextColor = (int) (Math.random()*Integer.MAX_VALUE);
		}

		if(keyHandler.isKeyPressed(KeyEvent.VK_RIGHT) && this.p.getPosition().x() < WIDTH * 18) {
			this.p.move(true);
			if(this.hasCollided())
				this.p.move(false);
		}

		if(keyHandler.isKeyPressed(KeyEvent.VK_LEFT) && this.p.getPosition().x() > 0) {
			this.p.move(false);
			if(this.hasCollided())
				this.p.move(true);
		}

		if(keyHandler.isKeyPressed(KeyEvent.VK_UP)) {
			this.p.rotate(true);
			if(hasCollided())
				this.p.rotate(false);
		}

		if(keyHandler.isKeyDown(KeyEvent.VK_DOWN))
			for(int i = 0; i < 10; i++) if(!hasCollided())
				this.p.update();

		this.p.update();

		if(hasCollided())
			putPiece();
		checkScore();
		checkLevel();
	}

	private void gameOver() {
		JOptionPane.showMessageDialog(instance, "You lost !", "Game Over", JOptionPane.PLAIN_MESSAGE);
		this.reset();
	}

	private void checkScore() {
		switch(linesCleared) {
		case 1:
			score += 1000;
			break;
		case 2:
			score += 2000;
			break;
		case 3:
			score += 4000;
			break;
		case 4:
			score += 10000;
			break;
		default:
		}
	}

	private void checkLevel() {
		level = (int) (score/10000);
	}

	@Override
	public void paint(Graphics g) {
		Rectangle r = g.getClipBounds();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, r.width, r.height);

		for(int x = 0; x < WIDTH; x++) for(int y = 0; y < HEIGHT; y++) {
			Vector2f pos = new Vector2f(20.0f*(x-WIDTH/2.0f), (y-HEIGHT/2.0f)*20);
			Vector2i posOnScreen = toGraphicalCoords(pos, g);
			g.setColor(Color.BLACK);
			g.fillRect(posOnScreen.x, posOnScreen.y, 20, 20);
			if(this.world[x][y] != 0) {
				BufferedImage temp = clone(blockTexture);
				setColor(temp, new Color(world[x][y]));
				g.drawImage(temp, posOnScreen.x, posOnScreen.y, 20, 20, null);
			}
		}

		if(this.p != null)
			this.p.render(g);

		Vector2i v = toGraphicalCoords(new Vector2f(200, -200), g);
		g.setColor(Color.BLACK);
		g.drawString("Score : " + (int)score, v.x, v.y);

		for(int x = 0; x < WIDTH; x++) for(int y = 0; y < HEIGHT; y++) {
			g.setColor(Color.DARK_GRAY);
			Vector2f pos = new Vector2f(20.0f*(x-WIDTH/2.0f), (y-HEIGHT/2.0f)*20);
			Vector2i posOnScreen = toGraphicalCoords(pos, g);
			g.drawRect(posOnScreen.x, posOnScreen.y, 20, 20);
		}
	}

	public float getSpeed() {
		return BASE_SPEED + this.level * SPEED_INCREASE;
	}

	public boolean isDiscrete() {
		return discrete;
	}

	private Pattern randomPattern() {
		int random = (int) (Math.random()*(PATTERNS.length));
		return PATTERNS[random];
	}


	private void putPiece() {
		Vector2i[] blocks = this.p.getBlocks();
		for(Vector2i block : blocks) {
			Vector2f pos = new Vector2f(block.x*20 + this.p.getPosition().x(),
					block.y*20 + this.p.getPosition().y());
			this.world[(int)(pos.x + 10)/20][(int) (pos.y + 10)/20] = this.p.getColor();
		}
		checkLines();
		this.p = null;
	}

	private boolean hasCollided() {
		Vector2i[] blocks = this.p.getBlocks();
		for(Vector2i block : blocks) {
			float y = (float) (discrete?Math.ceil(this.p.getPosition().y()*20)/20:this.p.getPosition().y());
			Vector2f pos = new Vector2f(block.x*20 + this.p.getPosition().x(),
					block.y*20 + y);
			if(pos.x < 0 || pos.x > WIDTH*18 || pos.y < 1 || pos.y > HEIGHT*19)
				return true;
			if(this.world[(int)pos.x/20][(int) pos.y/20] != 0 || this.world[(int)pos.x/20][(int) (pos.y+16)/20] != 0)
				return true;
		}
		return false;
	}

	private void checkLines() {
		for(int y = 0; y < HEIGHT; y++) {
			boolean isFull = true;
			for(int x = 0; x < WIDTH; x++)
				if(this.world[x][y] == 0) {
					isFull = false;
					break;
				}
			if(isFull) {
				this.linesCleared++;
				destroyLine(y);
				checkLines();
			}
		}
	}

	private void destroyLine(int height) {
		for(height++; height < HEIGHT; height++)
			for(int x = 0; x < WIDTH; x++)
				this.world[x][height-1] = this.world[x][height];
	}

	public BufferedImage getBlockTexture() {
		return this.blockTexture;
	}

	public void setColor(BufferedImage image, Color c) {
		for(int x = 0; x < image.getWidth(); x++)
			for(int y = 0; y < image.getHeight(); y++) {
				int pix = image.getRGB(x, y);
				int r = (pix>>16) & 0xff,
						g = (pix>>8) & 0xff,
						b = pix & 0xff;
				r *= c.getRed()/256.0f;
				g *= c.getGreen()/256.0f;
				b *= c.getBlue()/256.0f;
				int	newPix = ((int)r<<16) | ((int)g<<8) | (int)b;
				image.setRGB(x, y, newPix);
			}
	}
	
	public BufferedImage clone(BufferedImage img) {
		ColorModel c = this.blockTexture.getColorModel();
		WritableRaster raster = this.blockTexture.copyData(null);
		boolean alpha = img.isAlphaPremultiplied();
		BufferedImage temp = new BufferedImage(c, raster, alpha, null);
		return temp;
	}
	
	@Override
	public String getName() {
		return "Tetris";
	}
}
