package com.embededcontest.ctgui;

/**
 * Created by ChaomingGu on 2016/5/19.
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MandelDraw extends JPanel {
    public String IMAGE_ADDR;
    private static final Color DRAWING_RECT_COLOR = new Color(200, 200, 255);
    private static final Color DRAWN_RECT_COLOR = Color.blue;

    private BufferedImage image;
    private Rectangle rect = null;
    private boolean drawing = false;
    public int x1,y1,x2,y2;

    public MandelDraw() {
//        try {
//            image = ImageIO.read(new File(getIMAGE_ADDR()));
//            MyMouseAdapter mouseAdapter = new MyMouseAdapter();
//            addMouseListener(mouseAdapter);
//            addMouseMotionListener(mouseAdapter);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            System.exit(-1);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(-1);
//        }
    }

    public void makeDraw(){
        try {
            image = ImageIO.read(new File(getIMAGE_ADDR()));
            MyMouseAdapter mouseAdapter = new MyMouseAdapter();
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (image != null) {
            return new Dimension(image.getWidth(), image.getHeight());
        }
        return super.getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
        if (rect == null) {
            return;
        } else if (drawing) {
            g2.setColor(DRAWING_RECT_COLOR);
            g2.draw(rect);
        } else {
            g2.setColor(DRAWN_RECT_COLOR);
            g2.draw(rect);
        }
    }

    public void setIMAGE_ADDR(String IMAGE_ADDR){
        this.IMAGE_ADDR = IMAGE_ADDR;
    }

    public String getIMAGE_ADDR(){
        return IMAGE_ADDR;
    }

    public class MyMouseAdapter extends MouseAdapter {
        private Point mousePress = null;
        @Override
        public void mousePressed(MouseEvent e) {

            mousePress = e.getPoint();
            x1 = e.getX();
            y1 = e.getY();
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            drawing = true;
            int x = Math.min(mousePress.x, e.getPoint().x);
            int y = Math.min(mousePress.y, e.getPoint().y);
            int width = Math.abs(mousePress.x - e.getPoint().x);
            int height = Math.abs(mousePress.y - e.getPoint().y);

            rect = new Rectangle(x, y, width, height);
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            x2 = e.getX();
            y2 = e.getY();
            drawing = false;
            repaint();
        }

    }

    public double[] getCoordinate(){
        double[] getCoordinate = {x1,y1,x2,y2};
        return getCoordinate;

    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("MandelDraw");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MandelDraw());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }
}