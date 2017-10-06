package org.awesky.core.common;

import sun.util.logging.PlatformLogger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.logging.Logger;
import javax.swing.*;

public class DataRain extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    int height = (int)this.size.getHeight();
    int width = (int)this.size.getWidth();
    private int col = this.width / 20;
    private Random random = new Random();
    private MyPanel pan;
    private int[] arr;
    int a = 100;
    int b = 0;
    int c = 0;
  
    public DataRain() {
        setSize(width,height);
        setResizable(false);
        pan = new MyPanel();
        add(pan);
        this.arr = new int[col + 1];
        new Timer(120, this).start();
        addKeyListener(pan);
        setUndecorated(true);
        setVisible(true);
    }

    public class MyPanel extends JPanel implements KeyListener {
	  
        private static final long serialVersionUID = 1L;

        public MyPanel() {}

        public void paint(Graphics paramGraphicss) {

            Graphics2D paramGraphics = (Graphics2D) paramGraphicss;
            int i = 0;
            if (c == 17) {
               c = 0;
            }
            c += 1;
            paramGraphics.fillRect(0, 0, width, height);
            for(int j = 0; j < width; j += 20) {
                int k = arr[i];
                int m = 0;
                for (int n = k - 16; n < k; n++) {
                    m += 14;
                    if (m > 255) {
                        m = 255;
                    }
                    if (m == c * 14) {
                        paramGraphics.setColor(new Color(m, m, 255));
                        paramGraphics.setFont(new Font("Serif", 1, 20));
                    } else {
                        paramGraphics.setColor(new Color(0, m, 0));
                        paramGraphics.setFont(new Font("Serif", 1, 20));
                    }
                    paramGraphics.drawString(String.valueOf((char)(int)(Math.random() * 2+48)), j, n * 20);
               }
               arr[i] += random.nextInt(5);
               if (arr[i] > col-30) {
                   arr[i] = random.nextInt(100);
               }
               i++;
            }
        }

        public void keyPressed(KeyEvent paramKeyEvent) {
            int i = paramKeyEvent.getKeyCode();
            if (i == 27) {
                System.exit(1);
            }
        }

        public void keyReleased(KeyEvent paramKeyEvent) {}
        public void keyTyped(KeyEvent paramKeyEvent) {}
    
    }
  
    public void actionPerformed(ActionEvent paramActionEvent) {
	  this.pan.repaint();
  }
  
    public static void main(String[] paramArrayOfString) {
        new DataRain();
    }
  
}

