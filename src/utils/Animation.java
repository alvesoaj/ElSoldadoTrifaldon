package utils;

/**
 * @author ZeRoKoL
 */
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Animation {

	/**
	 *
	 */
	protected int xPos, yPos, counter, imageIndex;
	protected double duration, occurrence;
	protected ArrayList<BufferedImage> images;

	public Animation(ArrayList<BufferedImage> images, int xPos, int yPos,
			double duration) {
		this.images = images;
		this.xPos = xPos;
		this.yPos = yPos;
		this.duration = duration;
		this.counter = 0;
		this.occurrence = Constants.PERIOD * this.duration;
		this.imageIndex = 0;
	}

	public BufferedImage getImage() {
		return images.get(imageIndex);
	}

	public int getXPos() {
		return xPos;
	}

	public int getYpos() {
		return yPos;
	}

	public abstract void update();
}
