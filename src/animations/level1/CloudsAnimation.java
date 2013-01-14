package animations.level1;

/**
 * @author ZeRoKoL
 */
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utils.Animation;

public class CloudsAnimation extends Animation {

	/**
	 *
	 */
	private int cloudAmount;
	private int PWIDTH, PHEIGHT, threshold;
	private ArrayList<int[]> cloudsPositions;
	private int animationUpdateCounter = 150;

	public CloudsAnimation(BufferedImage image, int period, int duration,
			int w, int h, int ca) {
		super(image, 0, 0, period, duration);

		PWIDTH = w;
		PHEIGHT = h;
		cloudAmount = ca;

		threshold = (image.getWidth() + 1) * -1;

		for (int i = 0; i < cloudAmount; i++) {
			int[] cloud = new int[2];
			cloud[0] = (int) (Math.random() * PWIDTH);
			cloud[1] = (int) (Math.random() * PHEIGHT);
			// cloudsPositions.add(cloud);
		}
	}

	@Override
	public void update() {
		counter += 1;
		if (counter % (occurrence / animationUpdateCounter) == 0) {
			for (int i = 0; i < cloudAmount; i++) {
				cloudsPositions.get(i)[0] -= 1;
				if (cloudsPositions.get(i)[0] <= threshold) {
					cloudsPositions.get(i)[0] = PWIDTH + 1;
				}
			}
		}
		if (counter > occurrence) {
			counter = 0;
		}
	}

	public ArrayList<int[]> getCloudsPositions() {
		return cloudsPositions;
	}
}
