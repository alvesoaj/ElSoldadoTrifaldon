package utils;

/**
 *
 * @author ZeRoKoL
 */
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpriteAnimation {

	/**
	 *
	 */
	private ArrayList<BufferedImage> imageList;
	private int occurrence;
	private boolean repeat;
	private int imagePosition;
	private int imageAmount;
	private int counter;

	public SpriteAnimation(ArrayList<BufferedImage> imageList, int period,
			int duration, boolean repeat) {
		this.imageList = imageList;
		this.repeat = repeat;
		this.occurrence = period * duration;
		this.imagePosition = 0;
		this.imageAmount = imageList.size();
	}

	public void update() {
		counter += 1;
		if (counter % (occurrence / imageAmount) == 0) {
			if (imagePosition != imageAmount) {
				imagePosition += 1;
			} else if (repeat == true) {
				imagePosition = 0;
			}
		}
		if (counter > occurrence) {
			counter = 0;
		}
	}

	public BufferedImage getImage() {
		return imageAmount != 0 ? (BufferedImage) imageList.get(imagePosition)
				: null;
	}

	public void resetAnimation() {
		this.imagePosition = 0;
	}
}
