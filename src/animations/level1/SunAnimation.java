package animations.level1;

/**
 * @author ZeRoKoL
 */
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utils.Animation;
import utils.Constants;

public class SunAnimation extends Animation {

	/**
	 *
	 */
	private int initialY;
	private int movie;
	private int animationUpdateCounter = 100;

	public SunAnimation(ArrayList<BufferedImage> images, int xPos, int yPos,
			double duration) {
		super(images, xPos, yPos, duration);
		initialY = yPos;
		movie = Constants.UP;
	}

	@Override
	public void update() {
		if (counter % (occurrence / animationUpdateCounter) == 0) {
			switch (movie) {
			case Constants.UP:
				yPos -= 1;
				if (yPos < (initialY - 25)) {
					movie = Constants.DOWN;
				}
				break;
			case Constants.DOWN:
				yPos += 1;
				if (yPos > (initialY + 25)) {
					movie = Constants.UP;
				}
				break;
			}
		}
		counter += 1;
		if (counter > occurrence) {
			counter = 0;
		}
	}
}
