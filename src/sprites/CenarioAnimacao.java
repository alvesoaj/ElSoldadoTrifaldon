/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

import java.awt.image.BufferedImage;
import utils.CarregadorImagens;

/**
 *
 * @author ZeRoKoL
 */
public class CenarioAnimacao {

    private int posX, posY, tamL;
    private BufferedImage bi;

    public CenarioAnimacao(String nomeImg, int x, int y, CarregadorImagens ci) {
        bi = ci.getImagem(nomeImg);
        tamL = bi.getWidth();
        posX = x;
        posY = y;
    }

    public BufferedImage getImagem() {
        return bi;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int pX) {
        if (pX > 0) {
            if ((posX + pX) < 0) {
                this.posX += pX;
            }
        } else {
            if ((posX + pX) > (500 - tamL)) {
                this.posX += pX;
            }
        }
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int pY) {
        this.posY += posY;
    }
}
