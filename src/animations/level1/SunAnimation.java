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
	private int animationUpdateCounter = 150;

	public SunAnimation(ArrayList<BufferedImage> images, int xPos, int yPos,
			double duration) {
		super(images, xPos, yPos, duration);
		initialY = yPos;
		movie = 0;
	}

	@Override
	public void update() {
		counter += 1;
		if (counter % (occurrence / animationUpdateCounter) == 0) {
			switch (movie) {
			case Constants.UP:
				yPos -= 1;
				if (yPos < (initialY - 100)) {
					movie = Constants.DOWN;
				}
				break;
			case Constants.DOWN:
				yPos += 1;
				if (yPos > (initialY + 100)) {
					movie = Constants.UP;
				}
				break;
			}
		}
		if (counter > occurrence) {
			counter = 0;
		}
	}
}
