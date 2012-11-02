/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sprites;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import utils.AnimadorImagens;
import utils.CarregadorImagens;

/**
 *
 * @author ZeRoKoL
 */
public class TrifaldonSprite {

    private final double TMP_DUR = 1;
    private double duracaoSeq;
    private long tempTotAnim;
    private final int TAM_MOV = 7;
    private int posX, posY, periodo, linhaY, movimento, gravidade, contador;
    private ArrayList direita, esquerda;
    private AnimadorImagens animacao, animD, animE;
    private boolean pulando = false;

    public TrifaldonSprite(String nomeImg, int p, double d, int x, int y, CarregadorImagens ci) {
        periodo = p;
        direita = ci.getLista(nomeImg, 4, 7);
        esquerda = ci.getLista(nomeImg, 0, 3);
        duracaoSeq = d;

        animD = new AnimadorImagens(direita, periodo, TMP_DUR, true);
        animE = new AnimadorImagens(esquerda, periodo, TMP_DUR, true);

        animacao = animD;
        animacao.stop();
        posX = x;
        posY = linhaY = y;
        contador = 0;
    }

    private void processarMovimento(int mov) {
        switch (mov) {
            case 1:
                animacao.stop();
                break;
            case 2:
                animacao = animD;
                animacao.resume();
                this.setPosX(+TAM_MOV);
                break;
            case 3:
                animacao = animE;
                animacao.resume();
                this.setPosX(-TAM_MOV);
                break;
            case 4:
                if (posY == linhaY) {
                    System.out.print("novo\n");
                    gravidade = -10;
                    contador = 0;
                    pulando = true;
                }
                break;
            default:
                animacao = animD;
                animacao.resume();
        }
    }

    public BufferedImage getImagem() {
        return animacao.getCurrentImage();
    }

    public void atualizar() {
        animacao.updateTick();
        pular();
    }

    public int getMovimento() {
        return movimento;
    }

    public void setMovimento(int movimento) {
        this.movimento = movimento;
        processarMovimento(this.movimento);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX += posX;
        controlarPerimetro();
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY += posY;
    }

    private void controlarPerimetro() {
        if (posX > 500) {
            posX = -50;
        } else if (posX < -70) {
            posX = 480;
        }
    }

    private void pular() {
        if (pulando == true) {
            tempTotAnim = (tempTotAnim + periodo) % (long) (1000 * duracaoSeq);
            System.out.print("ant: " + posY + " - " + tempTotAnim + "\n");
            if (contador < 10) {
                posY += gravidade;
            } else if (contador >= 10 && contador < 15) {
                posY += -(gravidade * 2);
            } else {
                pulando = false;
            }
            contador++;
            System.out.print("dep: " + posY + " - " + tempTotAnim + "\n");
        }
    }
}
