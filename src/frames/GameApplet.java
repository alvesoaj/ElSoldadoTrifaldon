package frames;

/**
 * @author ZeRoKoL
 */
import java.awt.Dimension;
import javax.swing.JApplet;
import panels.FirstLevel;

public class GameApplet extends JApplet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Dimension d = new Dimension(500, 400);

	public void init() {
		this.resize(d.width, d.height);
		FirstLevel level = new FirstLevel(this, d);
		this.add(level);
	}
}
