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

	private final String IMG_DIR = "/images/";
	private HashMap<String, ArrayList<BufferedImage>> imageMap;
	private HashMap<String, String> imageList;
	private GraphicsConfiguration gc;

	public ImageLoader() {
	}

	public boolean loadByFile(String fileName) {
		imageMap = new HashMap<String, ArrayList<BufferedImage>>();
		imageList = new HashMap<String, String>();

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

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
		return true;
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
					sprite[i] = gc.createCompatibleImage(imgWidth, imgHeight, imgTransparency);
					
					Graphics2D imgGraph = sprite[i].createGraphics();

					// copiando a image
					imgGraph.drawImage(image, 0, 0, imgWidth, imgHeight, i * imgWidth, 0,
							(i * imgWidth) + imgWidth, imgHeight, null);
					
					imgGraph.dispose();
				}
				return sprite;
			}else{
				System.out.println("Erro: Imagem não encontrada");
			}
		}else{
			System.out.println("Erro: Quantidade igual a 0");
		}
		return null;
	}

	public BufferedImage getImage(String name) {
		ArrayList<?> imsList = (ArrayList<?>) imageMap.get(name);
		if (imsList == null) {
			System.out.println("No image(s) stored under " + name);
			return null;
		}

		return (BufferedImage) imsList.get(0);
	}

	public ArrayList<?> getLista(String name, int i, int f) {
		ArrayList imsList = (ArrayList) imageMap.get(name);
		ArrayList temp = new ArrayList();

		if (imsList == null) {
			System.out.println("No image(s) stored under " + name);
			return null;
		}

		for (int x = 0; x < (f - i); x++) {
			temp.add(imsList.get(i + x));
		}

		return temp;
	}

	public boolean estaCarregado(String name) {
		ArrayList imsList = (ArrayList) imageMap.get(name);
		if (imsList == null) {
			return false;
		}
		return true;
	}

	public int numImagens(String name) {
		ArrayList imsList = (ArrayList) imageMap.get(name);
		if (imsList == null) {
			System.out.println("No image(s) stored under " + name);
			return 0;
		}
		return imsList.size();
	}

	public BufferedImage getImagem(String name, int posn) {
		ArrayList imsList = (ArrayList) imageMap.get(name);
		if (imsList == null) {
			System.out.println("No image(s) stored under " + name);
			return null;
		}

		int size = imsList.size();
		if (posn < 0) {
			return (BufferedImage) imsList.get(0); // return first image
		} else if (posn >= size) {
			// System.out.println("No " + name + " image at position " + posn);
			int newPosn = posn % size; // modulo
			// System.out.println("Return image at position " + newPosn);
			return (BufferedImage) imsList.get(newPosn);
		}
		return (BufferedImage) imsList.get(posn);
	}
}
