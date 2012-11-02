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
public class CarregadorImagens {

    private final String IMG_DIR = "/imagens/";
    private HashMap imagesMap;
    private GraphicsConfiguration gc;

    public CarregadorImagens(String arqNome) {
        imagesMap = new HashMap();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        String imsFNm = IMG_DIR + arqNome;
        System.out.println("Reading file: " + arqNome);
        try {
            InputStream in = this.getClass().getResourceAsStream(imsFNm);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String linha;
            char ch;
            while ((linha = br.readLine()) != null) {
                if (linha.length() == 0) {
                    continue;
                }
                if (linha.startsWith("//")) {
                    continue;
                }
                ch = Character.toLowerCase(linha.charAt(0));
                if (ch == 'e') {
                    getImagensEstaticas(linha);
                } else if (ch == 's') {
                    getImagensSprite(linha);
                } else {
                    System.out.println("Do not recognize line: " + linha);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + arqNome);
            System.exit(1);
        }
    }

    private boolean getImagensEstaticas(String linha) {
        String nomeImg = getNomeImagem(linha);
        String nome = getPrefixo(nomeImg);
        System.out.print(nomeImg + "   " + nome);

        if (imagesMap.containsKey(nome)) {
            System.out.println("Error: " + nome + "already used");
            return false;
        }

        BufferedImage bi = loadImagem(nomeImg);
        if (bi != null) {
            ArrayList imsList = new ArrayList();
            imsList.add(bi);
            imagesMap.put(nome, imsList);
            System.out.println("  Stored " + nome + "/" + linha);
            return true;
        } else {
            return false;
        }
    }

    private String getNomeImagem(String linha) {
        StringTokenizer tokens = new StringTokenizer(linha);

        if (tokens.countTokens() < 2 || tokens.countTokens() > 3) {
            System.out.println("Wrong no. of arguments for " + linha);
            return linha;
        } else {
            tokens.nextToken();
            return tokens.nextToken();
        }
    }

    private String getPrefixo(String linha) {
        int posn;
        if ((posn = linha.lastIndexOf(".")) == -1) {
            System.out.println("No prefix found for filename: " + linha);
            return linha;
        } else {
            return linha.substring(0, posn);
        }
    }

    private BufferedImage loadImagem(String linha) {
        try {
            BufferedImage im = ImageIO.read(getClass().getResource(IMG_DIR + linha));

            int transparency = im.getColorModel().getTransparency();
            BufferedImage copy = gc.createCompatibleImage(
                    im.getWidth(), im.getHeight(),
                    transparency);

            // create a graphics context
            Graphics2D g2d = copy.createGraphics();


            // reportTransparency(IMAGE_DIR + fnm, transparency);

            // copy image
            g2d.drawImage(im, 0, 0, null);
            g2d.dispose();
            return copy;
        } catch (IOException e) {
            System.out.println("Load Image error for " + IMG_DIR + "/" + linha + ":\n" + e);
            return null;
        }
    }

    private boolean getImagensSprite(String linha) {
        String nomeImg = getNomeImagem(linha);
        String nome = getPrefixo(nomeImg);
        int quantidade = getQuantia(linha);

        if (imagesMap.containsKey(nome)) {
            System.out.println("Error: " + nome + "already used");
            return false;
        }

        BufferedImage[] bi = loadImagensSprite(nomeImg, quantidade);
        if (bi != null) {
            ArrayList imsList = new ArrayList();
            for (int x = 0; x < bi.length; x++) {
                imsList.add(bi[x]);
            }
            imagesMap.put(nome, imsList);
            System.out.println("  Stored " + nome + "/" + linha);
            return true;
        } else {
            return false;
        }

    }

    private int getQuantia(String linha) {
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

    public BufferedImage[] loadImagensSprite(String linha, int qta) {
        if (qta <= 0) {
            System.out.println("number <= 0; returning null");
            return null;
        }

        BufferedImage stripIm;
        if ((stripIm = loadImagem(linha)) == null) {
            System.out.println("Returning null");
            return null;
        }

        int imWidth = (int) stripIm.getWidth() / qta;
        int height = stripIm.getHeight();
        int transparency = stripIm.getColorModel().getTransparency();

        BufferedImage[] strip = new BufferedImage[qta];
        Graphics2D stripGC;

        // each BufferedImage from the strip file is stored in strip[]
        for (int i = 0; i < qta; i++) {
            strip[i] = gc.createCompatibleImage(imWidth, height, transparency);
            // create a graphics context
            stripGC = strip[i].createGraphics();

            // copy image
            stripGC.drawImage(stripIm, 0, 0, imWidth, height, i * imWidth, 0, (i * imWidth) + imWidth, height, null);
            stripGC.dispose();
        }
        return strip;
    }

    public BufferedImage getImagem(String name) {
        ArrayList imsList = (ArrayList) imagesMap.get(name);
        if (imsList == null) {
            System.out.println("No image(s) stored under " + name);
            return null;
        }

        return (BufferedImage) imsList.get(0);
    }

    public ArrayList getLista(String name, int i, int f) {
        ArrayList imsList = (ArrayList) imagesMap.get(name);
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
        ArrayList imsList = (ArrayList) imagesMap.get(name);
        if (imsList == null) {
            return false;
        }
        return true;
    }

    public int numImagens(String name) {
        ArrayList imsList = (ArrayList) imagesMap.get(name);
        if (imsList == null) {
            System.out.println("No image(s) stored under " + name);
            return 0;
        }
        return imsList.size();
    }

    public BufferedImage getImagem(String name, int posn) {
        ArrayList imsList = (ArrayList) imagesMap.get(name);
        if (imsList == null) {
            System.out.println("No image(s) stored under " + name);
            return null;
        }

        int size = imsList.size();
        if (posn < 0) {
            return (BufferedImage) imsList.get(0);   // return first image
        } else if (posn >= size) {
            // System.out.println("No " + name + " image at position " + posn);
            int newPosn = posn % size;   // modulo
            // System.out.println("Return image at position " + newPosn);
            return (BufferedImage) imsList.get(newPosn);
        }
        return (BufferedImage) imsList.get(posn);
    }
}
