/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import sprites.TrifaldonSprite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;
import sprites.CenarioAnimacao;
import sprites.NuvensAnimacao;
import utils.CarregadorImagens;

/**
The SimpleScreenManager class manages initializing and
displaying full screen graphics modes.
 */
public class TelaJogo extends JPanel implements ActionListener {

    private final int PERIODO = 100;
    private BufferedImage chao, sol;
    private CenarioAnimacao grama, grama2, casa;
    private NuvensAnimacao nuvem1, nuvem2;
    private TrifaldonSprite trifaldon;
    private CarregadorImagens cdi;
    private boolean justStarted = true;

    public TelaJogo() {
        setBackground(Color.CYAN);
        setPreferredSize(new Dimension(500, 400));

        cdi = new CarregadorImagens("imagensConfig.wga");
        initImgCenario();

        setFocusable(true);
        requestFocus();

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                processarTecla(e, 1);
            }
        });

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                processarTecla(e, 2);
            }
        });

        new Timer(PERIODO, this).start();    // start the Swing timer
    }

    private void processarTecla(KeyEvent e, int tipo) {
        int tecla = e.getKeyCode();
        if (tipo == 1) {
            if (tecla == KeyEvent.VK_RIGHT) {
                trifaldon.setMovimento(2);
                animarCenario(1);
            }
            if (tecla == KeyEvent.VK_LEFT) {
                trifaldon.setMovimento(3);
                animarCenario(2);
            }
            if (tecla == KeyEvent.VK_UP) {
                trifaldon.setMovimento(4);
            }
        } else if (tipo == 2) {
            trifaldon.setMovimento(1);
        }
    }

    private void animarCenario(int tipo) {
        if (tipo == 1) {
            grama.setPosX(-3);
            grama2.setPosX(-2);
            casa.setPosX(-1);
        } else {
            grama.setPosX(3);
            grama2.setPosX(2);
            casa.setPosX(1);
        }
    }

    private void initImgCenario() {
        chao = cdi.getImagem("chao");

        sol = cdi.getImagem("sol");
        nuvem1 = new NuvensAnimacao("nuvens", 0, 0, PERIODO, 0.2, -1, cdi);
        nuvem2 = new NuvensAnimacao("nuvens", 600, 0, PERIODO, 0.2, -1, cdi);
        grama = new CenarioAnimacao("grama", -100, 223, cdi);
        grama2 = new CenarioAnimacao("grama2", -27, 340, cdi);
        casa = new CenarioAnimacao("casa", -27, 130, cdi);

        trifaldon = new TrifaldonSprite("spriteFormiga", 50, 0.5, 50, 357, cdi);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(sol, 30, 30, this);
        g.drawImage(nuvem1.getImagem(), nuvem1.getPosX(), nuvem1.getPosY(), this);
        g.drawImage(nuvem2.getImagem(), nuvem2.getPosX(), nuvem2.getPosY(), this);
        g.drawImage(casa.getImagem(), casa.getPosX(), casa.getPosY(), this);
        g.drawImage(grama.getImagem(), grama.getPosX(), grama.getPosY(), this);
        g.drawImage(grama2.getImagem(), grama2.getPosX(), grama2.getPosY(), this);

        g.drawImage(chao, 0, 390, this);

        g.drawImage(trifaldon.getImagem(), trifaldon.getPosX(), trifaldon.getPosY(), this);
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
}
