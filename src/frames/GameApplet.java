package frames;

/**
 * @author ZeRoKoL
 */
import java.awt.Dimension;
import javax.swing.JApplet;

import characters.Trifaldon;
import panels.FirstLevel;

public class GameApplet extends JApplet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Dimension d = new Dimension(500, 400);
	private Trifaldon trifaldon;

	public void init() {
		this.resize(d.width, d.height);
		FirstLevel level = new FirstLevel(this, d, trifaldon);
		this.add(level);
	}
}
