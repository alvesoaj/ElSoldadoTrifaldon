package utils;

/**
 * @author ZeRoKoL
 */
import java.awt.image.BufferedImage;

public abstract class Animation {

	/**
	 *
	 */
	protected int xPos, yPos, period, duration, counter, occurrence;
	protected BufferedImage image;

	public Animation(BufferedImage image, int xPos, int yPos, int period,
			int duration) {
		this.image = image;
		this.xPos = xPos;
		this.yPos = yPos;
		this.period = period;
		this.duration = duration;
		this.counter = 0;
		this.occurrence = this.period * this.duration;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYpos() {
		return yPos;
	}

	public abstract void update();
}
