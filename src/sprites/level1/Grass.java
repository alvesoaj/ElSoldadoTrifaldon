package sprites.level1;

/**
 * @author ZeRoKoL
 */
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utils.Constants;
import utils.Sprite;

public class Grass extends Sprite {

	/**
	 *
	 */
	private int movement, threshold, occurrence;

	public Grass(ArrayList<BufferedImage> sprites, int xPos, int yPos,
			int period, int width, int duration) {
		super(sprites, xPos, yPos, period);
		this.threshold = (sprites.get(0).getWidth() - width) / 2;
		this.occurrence = duration * period;
	}

	@Override
	public void setMovement(int movement) {
		this.movement = movement;
	}

	public void update() {
		counter += 1;
		if (counter % (occurrence / threshold) == 0) {
			switch (this.movement) {
			default:
			case Constants.STOP:
				break;
			case Constants.RIGHT:
				if (this.xPos > -threshold) {
					this.xPos -= 1;
				}
				break;
			case Constants.LEFT:
				if (this.xPos > threshold) {
					this.xPos += 1;
				}
				break;
			}
		}
		if (counter > occurrence) {
			counter = 0;
		}
	}
}
