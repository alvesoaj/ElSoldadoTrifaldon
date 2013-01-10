package panels;

/**
 * @author ZeRoKoL
 */
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;

import characters.Trifaldon;
import sprites.CenarioAnimacao;
import sprites.NuvensAnimacao;
import utils.ImageLoader;

public class FirstLevel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Trifaldon trifaldon;
	private final int PERIODO = 100;
	private BufferedImage chao, sol;
	private CenarioAnimacao grama, grama2, casa;
	private NuvensAnimacao nuvem1, nuvem2;
	private ImageLoader cdi;
	private boolean justStarted = true;

	public FirstLevel(Container cont, final Dimension d, Trifaldon t) {
		setPreferredSize(d);
		setBackground(Color.CYAN);
		
		trifaldon = t;

		cdi = new ImageLoader("imagensConfig.wga");

		buildScenario();

		setFocusable(true);
		requestFocus();

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				keyDealer(e, "press");
			}
		});

		addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				keyDealer(e, "release");
			}
		});

		new Timer(PERIODO, this).start(); // start the Swing timer
	}

	private void keyDealer(KeyEvent e, String tipo) {
		int key = e.getKeyCode();
		if (tipo.compareTo("press") == 0) {
			switch (key) {
			case KeyEvent.VK_RIGHT:
				trifaldon.setMovimento(2);
				animarCenario(1);
				break;
			case KeyEvent.VK_LEFT:
				trifaldon.setMovimento(3);
				animarCenario(2);
				break;
			case KeyEvent.VK_UP:
				trifaldon.setMovimento(4);
				break;
			}
		} else if (tipo.compareTo("release") == 0) {
			switch (key) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_LEFT:
				trifaldon.setMovimento(1);
				break;
			}
		}
	}

	private void animarCenario(int tipo) {
		if (tipo == 1) {
			grama.setPosX(-3);
			grama2.setPosX(-2);
			casa.setPosX(-1);
		} else {
			grama.setPosX(3);
			grama2.setPosX(2);
			casa.setPosX(1);
		}
	}

	private void buildScenario() {
		chao = cdi.getImagem("chao");

		sol = cdi.getImagem("sol");
		nuvem1 = new NuvensAnimacao("nuvens", 0, 0, PERIODO, 0.2, -1, cdi);
		nuvem2 = new NuvensAnimacao("nuvens", 600, 0, PERIODO, 0.2, -1, cdi);
		grama = new CenarioAnimacao("grama", -100, 223, cdi);
		grama2 = new CenarioAnimacao("grama2", -27, 340, cdi);
		casa = new CenarioAnimacao("casa", -27, 130, cdi);

		trifaldon = new Trifaldon("spriteFormiga", 50, 0.5, 50, 357, cdi);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(sol, 30, 30, this);
		g.drawImage(nuvem1.getImagem(), nuvem1.getPosX(), nuvem1.getPosY(),
				this);
		g.drawImage(nuvem2.getImagem(), nuvem2.getPosX(), nuvem2.getPosY(),
				this);
		g.drawImage(casa.getImagem(), casa.getPosX(), casa.getPosY(), this);
		g.drawImage(grama.getImagem(), grama.getPosX(), grama.getPosY(), this);
		g.drawImage(grama2.getImagem(), grama2.getPosX(), grama2.getPosY(),
				this);

		g.drawImage(chao, 0, 390, this);

		g.drawImage(trifaldon.getImagem(), trifaldon.getPosX(),
				trifaldon.getPosY(), this);
	}

	public void actionPerformed(ActionEvent e) {
		if (justStarted) // don't do updates the first time through
		{
			justStarted = false;
		} else {
			atualizarImagens();
		}

		repaint();
	}

	private void atualizarImagens() {
		trifaldon.atualizar();
		nuvem1.atualizar();
		nuvem2.atualizar();
	}
}
