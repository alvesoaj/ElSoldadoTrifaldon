package animations.level1;

/**
 * @author ZeRoKoL
 */
import java.awt.image.BufferedImage;

import utils.Animation;

public class SunAnimation extends Animation {

	/**
	 *
	 */
	private int initialY;
	private int movie;
	private int animationUpdateCounter = 150;

	public SunAnimation(BufferedImage image, int xPos, int yPos, int period,
			int duration) {
		super(image, xPos, yPos, period, duration);
		initialY = yPos;
		movie = 0;
	}

	@Override
	public void update() {
		counter += 1;
		if (counter % (occurrence / animationUpdateCounter) == 0) {
			switch (movie) {
			case 0:
				yPos -= 1;
				if (yPos < (initialY - 100)) {
					movie = 1;
				}
				break;
			case 1:
				yPos += 1;
				if (yPos < (initialY + 100)) {
					movie = 0;
				}
				break;
			}
		}
		if (counter > occurrence) {
			counter = 0;
		}
	}
}
