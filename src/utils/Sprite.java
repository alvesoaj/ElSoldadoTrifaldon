package utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author ZeRoKoL
 */

public abstract class Sprite {

	/**
	 *
	 */
	protected int xPos, yPos, period, counter, imagePosition;
	protected ArrayList<BufferedImage> sprites;

	public Sprite(ArrayList<BufferedImage> sprites, int xPos, int yPos,
			int period) {
		this.sprites = sprites;
		this.xPos = xPos;
		this.yPos = yPos;
		this.period = period;
		this.counter = 0;
		this.imagePosition = 0;
	}

	public BufferedImage getImage() {
		return sprites.get(imagePosition);
	}

	public int getXPos() {
		return xPos;
	}

	public int getYpos() {
		return yPos;
	}

	public abstract void update();

	public abstract void setMovement(int movement);
}