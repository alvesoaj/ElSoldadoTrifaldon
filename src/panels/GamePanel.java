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
import sprites.level1.Grass;
import sprites.level1.House;

import utils.Constants;
import utils.ImageLoader;

public class GamePanel extends JPanel implements Runnable {

	/**
	 * 
	 */
	private int PWIDTH; // size of panel
	private int PHEIGHT;

	// private static long MAX_STATS_INTERVAL = 1000000000L;
	private static long MAX_STATS_INTERVAL = 1000L;
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
	private ArrayList<BufferedImage> bImage;
	private SunAnimation sun;
	private CloudsAnimation clouds;
	private House house;
	private Grass grass1, grass2;
	private BufferedImage ground;
	private ImageLoader imageLoader;
	private boolean justStarted = true;
	private HashMap<String, ArrayList<BufferedImage>> imageMap;

	public GamePanel(Container cont, final Dimension d) {
		PWIDTH = d.width;
		PHEIGHT = d.height;

		setPreferredSize(d);
		setBackground(Color.CYAN);

		imageLoader = new ImageLoader();

		imageMap = imageLoader.loadByFile("level1.wga");

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

		// set up message font
		font = new Font("SansSerif", Font.BOLD, 15);
		metrics = this.getFontMetrics(font);

		fpsStore = new double[NUM_FPS];
		upsStore = new double[NUM_FPS];
		for (int i = 0; i < NUM_FPS; i++) {
			fpsStore[i] = 0.0;
			upsStore[i] = 0.0;
		}

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
		// finishOff();
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
		sun = new SunAnimation(bImage, 25, 50, 5.0);

		// bImage = imageLoader.getStaticImage("cloud-1.png");
		// clouds = new CloudsAnimation(bImage.get(0), period, 5, PWIDTH,
		// PHEIGHT, 8);

		// bImage = imageLoader.getStaticImage("house-1.png");
		// house = new House(bImage, -50, 0, period, PWIDTH, 6);

		// bImage = imageLoader.getStaticImage("grass-1.png");
		// grass1 = new Grass(bImage, -100, 0, period, PWIDTH, 5);

		// bImage = imageLoader.getStaticImage("grass-2.png");
		// grass2 = new Grass(bImage, -200, 0, period, PWIDTH, 4);

		bImage = imageLoader.getStaticImage("ground-1.png");
		ground = bImage.get(0);

		// bImage = imageLoader.getSprite("trifaldon-sprites.png");
		// trifaldon = new Trifaldon(bImage, 350, 250, period);
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
			sleepTime = (Constants.PERIOD - timeDiff) - overSleepTime;

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
			while ((excess > Constants.PERIOD) && (skips < MAX_FRAME_SKIPS)) {
				excess -= Constants.PERIOD;
				gameUpdate(); // update state but don't render
				skips++;
			}
			framesSkipped += skips;

			storeStats();
		}
		// finishOff();
	}

	private void storeStats() {
		/*
		 * The statistics: - the summed periods for all the iterations in this
		 * interval (period is the amount of time a single frame iteration
		 * should take), the actual elapsed time in this interval, the error
		 * between these two numbers;
		 * 
		 * - the total frame count, which is the total number of calls to run();
		 * 
		 * - the frames skipped in this interval, the total number of frames
		 * skipped. A frame skip is a game update without a corresponding
		 * render;
		 * 
		 * - the FPS (frames/sec) and UPS (updates/sec) for this interval, the
		 * average FPS & UPS over the last NUM_FPSs intervals.
		 * 
		 * The data is collected every MAX_STATS_INTERVAL (1 sec).
		 */
		frameCount++;
		statsInterval += Constants.PERIOD;

		if (statsInterval >= MAX_STATS_INTERVAL) { // record stats every
													// MAX_STATS_INTERVAL
			long timeNow = System.nanoTime();
			timeSpentInGame = (int) ((timeNow - gameStartTime) / 1000000000L); // ns --> secs

			long realElapsedTime = timeNow - prevStatsTime; // time since last
															// stats collection
			totalElapsedTime += realElapsedTime;

			// double timingError = ((double) (realElapsedTime - statsInterval) / statsInterval) * 100.0;

			totalFramesSkipped += framesSkipped;

			double actualFPS = 0.0; // calculate the latest FPS and UPS
			double actualUPS = 0.0;
			if (totalElapsedTime > 0) {
				actualFPS = (((double) frameCount / totalElapsedTime) * 1000000000L);
				actualUPS = (((double) (frameCount + totalFramesSkipped) / totalElapsedTime) * 1000000000L);
			}

			// store the latest FPS and UPS
			fpsStore[(int) statsCount % NUM_FPS] = actualFPS;
			upsStore[(int) statsCount % NUM_FPS] = actualUPS;
			statsCount = statsCount + 1;

			double totalFPS = 0.0; // total the stored FPSs and UPSs
			double totalUPS = 0.0;
			for (int i = 0; i < NUM_FPS; i++) {
				totalFPS += fpsStore[i];
				totalUPS += upsStore[i];
			}

			if (statsCount < NUM_FPS) { // obtain the average FPS and UPS
				averageFPS = totalFPS / statsCount;
				averageUPS = totalUPS / statsCount;
			} else {
				averageFPS = totalFPS / NUM_FPS;
				averageUPS = totalUPS / NUM_FPS;
			}
			/*
			 * System.out.println(timedf.format( (double)
			 * statsInterval/1000000000L) + " " + timedf.format((double)
			 * realElapsedTime/1000000000L) + "s " + df.format(timingError) +
			 * "% " + frameCount + "c " + framesSkipped + "/" +
			 * totalFramesSkipped + " skip; " + df.format(actualFPS) + " " +
			 * df.format(averageFPS) + " afps; " + df.format(actualUPS) + " " +
			 * df.format(averageUPS) + " aups" );
			 */
			framesSkipped = 0;
			prevStatsTime = timeNow;
			statsInterval = 0L; // reset
		}
	}

	private void gameUpdate() {
		if (!paused && !gameOver) {
			sun.update();
			// clouds.update();
			// house.update();
			// grass1.update();
			// grass2.update();
			// trifaldon.update();
		}
	}

	private void gameRender() {
		if (dbImage == null) {
			dbImage = createImage(PWIDTH, PHEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			} else {
				dbg = dbImage.getGraphics();
			}
		}

		// clear the background
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, PWIDTH, PHEIGHT);

		dbg.setColor(Color.blue);
		dbg.setFont(font);

		dbg.drawImage(sun.getImage(), sun.getXPos(), sun.getYpos(), this);
		// dbg.drawImage(clouds.getImage(), clouds.getXPos(),
		// clouds.getYpos(),this);
		// dbg.drawImage(house.getImage(), house.getXPos(), house.getYpos(),
		// this);
		// dbg.drawImage(grass1.getImage(), grass1.getXPos(),
		// grass1.getYpos(),this);
		// dbg.drawImage(grass2.getImage(), grass2.getXPos(),
		// grass2.getYpos(),this);

		dbg.drawImage(ground, 0, 565, this);
		dbg.drawImage(ground, 500, 565, this);

		// g.drawImage(trifaldon.getImage(), trifaldon.getXPos(),
		// trifaldon.getYpos(), this);

		// report frame count & average FPS and UPS at top left
		// dbg.drawString("Frame Count " + frameCount, 10, 25);
		dbg.drawString(
				"Average FPS/UPS: " + df.format(averageFPS) + ", "
						+ df.format(averageUPS), 10, 20); // was (10,55)
		dbg.drawString("Time spent: " + timeSpentInGame + " s", 10, 40); // was
																			// (10,55)

		dbg.setColor(Color.black);

		if (gameOver) {
			// gameOverMessage(dbg);
		}
	}

	// use active rendering to put the buffered image on-screen
	private void paintScreen() {
		Graphics g;
		try {
			g = this.getGraphics();
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
