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
public class NuvensAnimacao {

    private int posX, posY, tamL, periodoAnim, variacao;
    private double duracaoSeq;
    private long tempTotAnim;
    private BufferedImage bi;

    public NuvensAnimacao(String nomeImg, int x, int y, int p, double d, int v, CarregadorImagens ci) {
        variacao = v;
        periodoAnim = p;
        duracaoSeq = d;
        bi = ci.getImagem(nomeImg);
        tamL = bi.getWidth();
        posX = x;
        posY = y;
        tempTotAnim = 0L;
    }

    public BufferedImage getImagem() {
        return bi;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void atualizar() {
        tempTotAnim = (tempTotAnim + periodoAnim) % (long) (1000 * duracaoSeq);
        if (tempTotAnim == 0) {
            posX += variacao;
        }

        if (posX < -tamL) {
            posX = tamL;
        }
    }
}
