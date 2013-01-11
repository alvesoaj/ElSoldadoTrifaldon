package panels;

/**
 * @author ZeRoKoL
 */
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.Timer;

import animations.level1.CloudsAnimation;
import animations.level1.SunAnimation;

import sprites.Trifaldon;

import utils.Constants;
import utils.ImageLoader;

public class GamePanel extends JPanel implements Runnable {

	/**
	 * 
	 */
	private int PWIDTH; // size of panel
	private int PHEIGHT;

	private static long MAX_STATS_INTERVAL = 1000000000L;
	// private static long MAX_STATS_INTERVAL = 1000L;
	// record stats every 1 second (roughly)

	private static final int NO_DELAYS_PER_YIELD = 16;
	/*
	 * Number of frames with a delay of 0 ms before the animation thread yields
	 * to other running threads.
	 */

	private static int MAX_FRAME_SKIPS = 5; // was 2;
	// no. of frames that can be skipped in any one animation loop
	// i.e the games state is updated but not rendered

	private static int NUM_FPS = 10;
	// number of FPS values stored to get an average

	// used for gathering statistics
	private long statsInterval = 0L; // in ns
	private long prevStatsTime;
	private long totalElapsedTime = 0L;
	private long gameStartTime;
	private int timeSpentInGame = 0; // in seconds

	private long frameCount = 0;
	private double fpsStore[];
	private long statsCount = 0;
	private double averageFPS = 0.0;

	private long framesSkipped = 0L;
	private long totalFramesSkipped = 0L;
	private double upsStore[];
	private double averageUPS = 0.0;

	private DecimalFormat df = new DecimalFormat("0.##"); // 2 dp
	private DecimalFormat timedf = new DecimalFormat("0.####"); // 4 dp

	private Thread animator; // the thread that performs the animation
	private volatile boolean running = false; // used to stop the animation
												// thread
	private volatile boolean paused = false;

	private int period = 60; // period between drawing in _nanosecs_

	// used at game termination
	private volatile boolean gameOver = false;
	private int score = 0;
	private Font font;
	private FontMetrics metrics;
	private boolean finishedOff = false;

	// off screen rendering
	private Graphics dbg;
	private Image dbImage = null;

	private static final long serialVersionUID = 1L;
	private Trifaldon trifaldon;
	private final int PERIODO = 100;
	private ArrayList<BufferedImage> bImage;
	private SunAnimation sun;
	private CloudsAnimation clouds;
	private CenarioAnimacao grama, grama2, casa;
	private BufferedImage ground;
	private ImageLoader imageLoader;
	private boolean justStarted = true;
	private HashMap<String, ArrayList<BufferedImage>> imageMap;

	private Graphics g;

	public GamePanel(Container cont, final Dimension d) {
		PWIDTH = d.width;
		PHEIGHT = d.height;

		g = this.getGraphics();
		super.paintComponent(g);

		setPreferredSize(d);
		setBackground(Color.CYAN);

		imageLoader = new ImageLoader();

		imageMap = imageLoader.loadByFile("level-1.wga");

		buildScenario();

		setFocusable(true);
		requestFocus();

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				keyAnalyzer(e, "press");
			}
		});

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				keyAnalyzer(e, "release");
			}
		});

		startGame();
	}

	// ------------- game life cycle methods ------------
	// called from the applet's life cycle methods

	// initialise and start the thread
	public void startGame() {
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	}

	// start game /resume a paused game
	public void resumeGame() {
		paused = false;
	}

	public void pauseGame() {
		paused = true;
	}

	// stop the thread by flag setting
	public void stopGame() {
		running = false;
		finishOff();
	}

	// ----------------------------------------------

	private void keyAnalyzer(KeyEvent e, String tipo) {
		int key = e.getKeyCode();
		if (tipo.compareTo("press") == 0) {
			switch (key) {
			case KeyEvent.VK_RIGHT:
				trifaldon.setMovement(Constants.RIGHT);
				break;
			case KeyEvent.VK_LEFT:
				trifaldon.setMovement(Constants.LEFT);
				break;
			case KeyEvent.VK_UP:
				trifaldon.setMovement(Constants.UP);
				break;
			}
		} else if (tipo.compareTo("release") == 0) {
			switch (key) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_LEFT:
				trifaldon.setMovement(Constants.STOP);
				break;
			}
		}
	}

	private void buildScenario() {
		bImage = imageLoader.getStaticImage("sun-1.png");
		sun = new SunAnimation(bImage.get(0), 50, 150, period, 5);

		bImage = imageLoader.getStaticImage("cloud-1.png");
		clouds = new CloudsAnimation(bImage.get(0), PERIODO, 5, PWIDTH,
				PHEIGHT, 8);

		casa = new CenarioAnimacao("casa", -27, 130, imageLoader);

		grama = new CenarioAnimacao("grama", -100, 223, imageLoader);

		grama2 = new CenarioAnimacao("grama2", -27, 340, imageLoader);

		bImage = imageLoader.getStaticImage("ground-1.png");
		ground = bImage.get(0);
		
		bImage = imageLoader.getSprite("trifaldon-sprites.png");
		trifaldon = new Trifaldon(bImage, 350, 250, period);
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

	@Override
	public void run() {
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;

		gameStartTime = System.nanoTime();
		prevStatsTime = gameStartTime;
		beforeTime = gameStartTime;

		running = true;

		while (running) {
			gameUpdate();
			gameRender(); // render the game to a buffer
			paintScreen(); // draw the buffer on-screen

			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;

			if (sleepTime > 0) { // some time left in this cycle
				try {
					Thread.sleep(sleepTime / 1000000L); // nano -> ms
				} catch (InterruptedException ex) {
				}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			} else { // sleepTime <= 0; the frame took longer than the period
				excess -= sleepTime; // store excess time value
				overSleepTime = 0L;

				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield(); // give another thread a chance to run
					noDelays = 0;
				}
			}

			beforeTime = System.nanoTime();

			/*
			 * If frame animation is taking too long, update the game state
			 * without rendering it, to get the updates/sec nearer to the
			 * required FPS.
			 */
			int skips = 0;
			while ((excess > period) && (skips < MAX_FRAME_SKIPS)) {
				excess -= period;
				gameUpdate(); // update state but don't render
				skips++;
			}
			framesSkipped += skips;

			storeStats();
		}
		finishOff();
	}

	private void gameUpdate() {
		if (!paused && !gameOver) {
			// fred.move();
		}
	}

	private void gameRender() {
		if (dbImage == null) {
			dbImage = createImage(PWIDTH, PHEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			} else
				dbg = dbImage.getGraphics();
		}

		// clear the background
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, PWIDTH, PHEIGHT);

		dbg.setColor(Color.blue);
		dbg.setFont(font);

		g.drawImage(sol, 30, 30, this);
		g.drawImage(nuvem1.getImagem(), nuvem1.getPosX(), nuvem1.getPosY(),
				this);
		g.drawImage(nuvem2.getImagem(), nuvem2.getPosX(), nuvem2.getPosY(),
				this);
		g.drawImage(casa.getImagem(), casa.getPosX(), casa.getPosY(), this);
		g.drawImage(grama.getImagem(), grama.getPosX(), grama.getPosY(), this);
		g.drawImage(grama2.getImagem(), grama2.getPosX(), grama2.getPosY(),
				this);

		g.drawImage(ground, 0, 565, this);
		g.drawImage(ground, 500, 565, this);

		g.drawImage(trifaldon.getImagem(), trifaldon.getPosX(),
				trifaldon.getPosY(), this);

		// report frame count & average FPS and UPS at top left
		// dbg.drawString("Frame Count " + frameCount, 10, 25);
		dbg.drawString(
				"Average FPS/UPS: " + df.format(averageFPS) + ", "
						+ df.format(averageUPS), 20, 25); // was (10,55)

		dbg.setColor(Color.black);

		// draw game elements: the obstacles and the worm
		// obs.draw(dbg);
		// fred.draw(dbg);

		if (gameOver) {
			// gameOverMessage(dbg);
		}
	}

	// use active rendering to put the buffered image on-screen
	private void paintScreen() {
		try {
			if ((g != null) && (dbImage != null))
				g.drawImage(dbImage, 0, 0, null);
			Toolkit.getDefaultToolkit().sync(); // sync the display on some
												// systems
			g.dispose();
		} catch (Exception e) {
			// quite commonly seen at applet destruction
			System.out.println("Graphics Context error: " + e);
		}
	}
}
