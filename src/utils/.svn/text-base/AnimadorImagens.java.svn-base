/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author ZeRoKoL
 */
public class AnimadorImagens {

    private boolean estaRepetindo, controladorAnim;
    private ArrayList listaImg;
    private int periodoAnim;
    private long tempTotAnim;
    private int periodoExibicao;
    private double duracaoSeq;
    private int numImagens;
    private int posImg;

    public AnimadorImagens(ArrayList al, int ap, double d, boolean isr) {
        periodoAnim = ap;
        duracaoSeq = d;
        estaRepetindo = isr;
        listaImg = al;

        tempTotAnim = 0L;

        if (duracaoSeq < 0.5) {
            System.out.println("Warning: minimum sequence duration is 0.5 sec.");
            duracaoSeq = 0.5;
        }

        numImagens = listaImg.size();
        posImg = 0;
        controladorAnim = true;
        periodoExibicao = (int) (1000 * duracaoSeq / numImagens);
    }

    public void updateTick() /* We assume that this method is called every animPeriod ms */ {
        if (controladorAnim) {
            // update total animation time, modulo the animation sequence duration
            tempTotAnim = (tempTotAnim + periodoAnim) % (long) (1000 * duracaoSeq);

            // calculate current displayable image position
            posImg = (int) (tempTotAnim / periodoExibicao) % numImagens;  // in range 0 to num-1

            if ((posImg == numImagens - 1) && (estaRepetindo)) {  // at end of sequence
                tempTotAnim = 0L;
            }
        }
    }  // end of updateTick()

    public BufferedImage getCurrentImage() {
        if (numImagens != 0) {
            return (BufferedImage) listaImg.get(posImg);
        } else {
            return null;
        }
    } // end of getCurrentImage()

    public int getCurrentPosition() {
        return posImg;
    }

    public void stop() /* updateTick() calls will no longer update the
    total animation time or imPosition. */ {
        controladorAnim = false;
    }

    public boolean isStopped() {
        return controladorAnim;
    }

    public boolean atSequenceEnd() // are we at the last image and not cycling through them?
    {
        return ((posImg == numImagens - 1) && (!estaRepetindo));
    }

    public void restartAt(int imPosn) /* Start showing the images again, starting with image number
    imPosn. This requires a resetting of the animation time as
    well. */ {
        if (numImagens != 0) {
            if ((imPosn < 0) || (imPosn > numImagens - 1)) {
                System.out.println("Out of range restart, starting at 0");
                imPosn = 0;
            }

            posImg = imPosn;
            // calculate a suitable animation time
            tempTotAnim = (long) posImg * periodoExibicao;
            controladorAnim = true;
        }
    }  // end of restartAt()

    public void resume() // start at previous image position
    {
        if (numImagens != 0) {
            controladorAnim = true;
        }
    }
}
