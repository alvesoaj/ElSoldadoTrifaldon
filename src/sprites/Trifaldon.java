package sprites;

/**
 * @author ZeRoKoL
 */
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utils.Constants;
import utils.SpriteAnimation;
import utils.Sprite;

public class Trifaldon extends Sprite {

	/**
	 *
	 */
	private long tempTotAnim;
	private final int TAM_MOV = 7;
	private int linhaY, movement, gravidade, contador;
	private SpriteAnimation animation, stopAnimation, rightAnimation,
			leftAnimation;
	private boolean pulando = false;

	public Trifaldon(ArrayList<BufferedImage> sprites, int xPos, int yPos,
			int period) {
		super(sprites, xPos, yPos, period);

		ArrayList<BufferedImage> subImageList = null;
		for (int i = 0; i < 1; i++) {
			subImageList.add(sprites.get(i));
			stopAnimation = new SpriteAnimation(subImageList, period, 2, false);
		}

		subImageList.clear();
		for (int i = 4; i < 8; i++) {
			subImageList.add(sprites.get(i));
			rightAnimation = new SpriteAnimation(subImageList, period, 2, true);
		}

		subImageList.clear();
		for (int i = 0; i < 4; i++) {
			subImageList.add(sprites.get(i));
			leftAnimation = new SpriteAnimation(subImageList, period, 2, true);
		}

		animation = stopAnimation;

		contador = 0;
	}

	public BufferedImage getImage() {
		return animation.getImage();
	}

	@Override
	public void setMovement(int movement) {
		this.movement = movement;

		switch (this.movement) {
		default:
		case Constants.STOP:
			animation = stopAnimation;
			break;
		case Constants.RIGHT:
			animation = rightAnimation;
			this.xPos += TAM_MOV;
			break;
		case Constants.LEFT:
			animation = leftAnimation;
			this.xPos -= TAM_MOV;
			break;
		case Constants.UP:
			break;
		case Constants.DOWN:
			break;
		case Constants.JUMP:
			if (yPos == linhaY) {
				System.out.print("novo\n");
				gravidade = -10;
				contador = 0;
				pulando = true;
			}
			break;
		case Constants.SHOT:
			break;
		}
		pular();
	}

	private void pular() {
		if (pulando == true) {
			tempTotAnim = (tempTotAnim + period) % (long) (1000 * 5);
			System.out.print("ant: " + yPos + " - " + tempTotAnim + "\n");
			if (contador < 10) {
				yPos += gravidade;
			} else if (contador >= 10 && contador < 15) {
				yPos += -(gravidade * 2);
			} else {
				pulando = false;
			}
			contador++;
			System.out.print("dep: " + yPos + " - " + tempTotAnim + "\n");
		}
	}

	@Override
	public void update() {
		animation.update();
	}
}
