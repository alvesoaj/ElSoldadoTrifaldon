/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;

/**
 * 
 * @author ZeRoKoL
 */
public class ImageLoader {

	/**
	 * 
	 */
	private final String IMG_DIR = "/images/";
	private HashMap<String, ArrayList<BufferedImage>> imageMap;
	private GraphicsConfiguration gc;

	public ImageLoader() {
		imageMap = new HashMap<String, ArrayList<BufferedImage>>();

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
	}

	public HashMap<String, ArrayList<BufferedImage>> loadByFile(String fileName) {
		imageMap.clear();

		String path = IMG_DIR + fileName;

		System.out.println("Reading file: " + fileName);
		try {
			InputStream in = this.getClass().getResourceAsStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String linha;
			while ((linha = br.readLine()) != null) {
				if (linha.length() == 0 || linha.startsWith("//")) {
					continue;
				}

				char ch = Character.toLowerCase(linha.charAt(0));

				String imageName = getImageName(linha);

				if (ch == 'e') {
					getStaticImage(imageName);
				} else if (ch == 's') {
					int amount = getAmount(linha);

					getSpriteImage(imageName, amount);
				} else {
					System.out.println("Linha não reconhecida: " + linha);
				}
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Erro na leitura: " + fileName);
			System.exit(1);
		}
		return imageMap;
	}

	public ArrayList<BufferedImage> getStaticImage(String in) {
		if (!imageMap.containsKey(in)) {
			BufferedImage bi = loadImage(in);
			if (bi != null) {
				ArrayList<BufferedImage> imgList = new ArrayList<BufferedImage>();
				imgList.add(bi);
				imageMap.put(in, imgList);
				System.out.println(in + ": Carregada!");
				return imgList;
			}
		} else {
			return imageMap.get(in);
		}
		return null;
	}

	private String getImageName(String line) {
		StringTokenizer tokens = new StringTokenizer(line);

		if (tokens.countTokens() == 2) {
			tokens.nextToken();
			return tokens.nextToken();
		} else {
			System.out.println("Linha inválida " + line);
			return null;
		}
	}

	private BufferedImage loadImage(String name) {
		try {
			BufferedImage im = ImageIO.read(getClass().getResource(
					IMG_DIR + name));

			int transparency = im.getColorModel().getTransparency();
			BufferedImage copy = gc.createCompatibleImage(im.getWidth(),
					im.getHeight(), transparency);

			// create a graphics context
			Graphics2D g2d = copy.createGraphics();

			// reportTransparency(IMAGE_DIR + fnm, transparency);

			// copy image
			g2d.drawImage(im, 0, 0, null);
			g2d.dispose();
			return copy;
		} catch (IOException e) {
			System.out
					.println("Erro ao carregar: " + IMG_DIR + name + "\n" + e);
			return null;
		}
	}

	private ArrayList<BufferedImage> getSpriteImage(String in, int amount) {
		if (!imageMap.containsKey(in)) {
			BufferedImage[] bi = loadSprite(in, amount);

			if (bi != null) {
				ArrayList<BufferedImage> imgList = new ArrayList<BufferedImage>();
				for (int x = 0; x < bi.length; x++) {
					imgList.add(bi[x]);
				}
				imageMap.put(in, imgList);
				System.out.println(in + ": Carregada!");
				return imgList;
			}
		} else {
			return imageMap.get(in);
		}
		return null;
	}

	public ArrayList<BufferedImage> getSprite(String in) {
		if (imageMap.containsKey(in)) {
			return imageMap.get(in);
		}
		return null;
	}

	private int getAmount(String linha) {
		StringTokenizer tokens = new StringTokenizer(linha);

		if (tokens.countTokens() < 2 || tokens.countTokens() > 3) {
			System.out.println("Wrong no. of arguments for " + linha);
			return 0;
		} else {
			tokens.nextToken();
			tokens.nextToken();
			System.out.print("o Line: ");
			return Integer.parseInt(tokens.nextToken());
		}
	}

	public BufferedImage[] loadSprite(String name, int amount) {
		if (amount > 0) {
			BufferedImage image = loadImage(name);
			if (image != null) {
				int imgWidth = (int) image.getWidth() / amount;
				int imgHeight = image.getHeight();
				int imgTransparency = image.getColorModel().getTransparency();
				BufferedImage[] sprite = new BufferedImage[amount];

				for (int i = 0; i < amount; i++) {
					sprite[i] = gc.createCompatibleImage(imgWidth, imgHeight,
							imgTransparency);

					Graphics2D imgGraph = sprite[i].createGraphics();

					// copiando a image
					imgGraph.drawImage(image, 0, 0, imgWidth, imgHeight, i
							* imgWidth, 0, (i * imgWidth) + imgWidth,
							imgHeight, null);

					imgGraph.dispose();
				}
				return sprite;
			} else {
				System.out.println("Erro: Imagem não encontrada");
			}
		} else {
			System.out.println("Erro: Quantidade igual a 0");
		}
		return null;
	}
}
