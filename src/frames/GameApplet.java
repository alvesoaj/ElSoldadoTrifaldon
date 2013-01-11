package frames;

/**
 * @author ZeRoKoL
 */
import java.awt.Dimension;
import javax.swing.JApplet;

import panels.GamePanel;

public class GameApplet extends JApplet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Dimension d = new Dimension(700, 500);

	public void init() {
		this.resize(d.width, d.height);
		GamePanel level = new GamePanel(this, d);
		this.add(level);
	}
}
