/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.awt.Container;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author ZeRoKoL
 */
public class FrameDoJogo extends JFrame{

    private static int FPS_PADRAO = 40;
    private TelaJogo telaJogo;

    public FrameDoJogo(long periodo) {
        super("El Soldado Trifaldón");
        Container conteiner = getContentPane();

        telaJogo = new TelaJogo();
        conteiner.add(telaJogo);
        try {
            setIconImage(ImageIO.read(getClass().getResource("/imagens/icone.gif")));
        } catch (IOException ex) {
            Logger.getLogger(FrameDoJogo.class.getName()).log(Level.SEVERE, null, ex);
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(300, 100);
        setSize(500, 450);
        //pack();
        setResizable(false);
        setVisible(true);
    }

    public static void main(String args[]) {
        long period = (long) 1000.0 / FPS_PADRAO;
        // System.out.println("fps: " + DEFAULT_FPS + "; period: " + period + " ms");
        new FrameDoJogo(period * 1000000L);    // ms --> nanosecs
    }
}
