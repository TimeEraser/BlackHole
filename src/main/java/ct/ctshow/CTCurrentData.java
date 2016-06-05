package ct.ctshow;

/**
 * Created by ChaomingGu on 2016/5/19.
 */
import util.ImageUtil;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CTCurrentData extends JPanel {
    private static final Color DRAWING_RECT_COLOR = Color.BLUE;
    private static final Color DRAWN_RECT_COLOR = Color.RED;

    public BufferedImage image = null;
    private String imagePath;

    private BufferedImage focus = null;
    //mouse event
    private MyMouseAdapter mouseAdapter;
    private Rectangle rect = null;
    private boolean drawing = false;
    public CTCurrentData() {
    }
    public void refreshImage(String imagePath,boolean isHistory) {
        try {
            //reInitialized
            removeAll();
            removeMouseListener(mouseAdapter);
            removeMouseMotionListener(mouseAdapter);
            rect=null;
            focus=null;
            //refresh
            this.imagePath = imagePath;
            image = ImageIO.read(new File(imagePath));
            if(!isHistory){
                mouseAdapter = new MyMouseAdapter();
                addMouseListener(mouseAdapter);
                addMouseMotionListener(mouseAdapter);
            }
            this.setVisible(true);
            this.getParent().getHeight();
            Integer imageX, imageY;
            imageX = (this.getParent().getWidth() - image.getWidth()) / 2;
            imageY = (this.getParent().getHeight() - image.getHeight()) / 2;
            setBounds(imageX, imageY, image.getWidth(), image.getHeight());
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshResult(String result) {
        removeAll();
        Integer ZOOM_WIDTH=image.getWidth() / 6;
        Integer ZOOM_HEIGHT=image.getHeight() /6;

        JPanel resultImageJPanel =new JPanel();
        resultImageJPanel.setLayout(null);
        resultImageJPanel.setBackground(Color.BLACK);
        resultImageJPanel.setBounds(0, 0,ZOOM_WIDTH+40, ZOOM_HEIGHT + 40);

        BufferedImage focusZoom= ImageUtil.zoom(focus,ZOOM_WIDTH,ZOOM_HEIGHT);
        JLabel resultImage = new JLabel();
        resultImage.setBounds(0,0,ZOOM_WIDTH,ZOOM_HEIGHT);
        resultImage.setIcon(new ImageIcon(focusZoom));
        resultImageJPanel.add(resultImage);

        JLabel resultText = new JLabel(result);
        resultText.setLayout(new FlowLayout(FlowLayout.CENTER));
        resultText.setOpaque(false);//背景透明
        resultText.setForeground(Color.RED);//字体颜色
        resultText.setFont(new Font("Dialog",0,14));//字体大小
        resultText.setBounds(20,ZOOM_HEIGHT,ZOOM_WIDTH,40);

        resultImageJPanel.add(resultText);
        add(resultImageJPanel);
        this.repaint();
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
        Graphics2D g2 = (Graphics2D) g;
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

    public String getImagePath() {
        return imagePath;
    }

    class MyMouseAdapter extends MouseAdapter {
        private Point mousePress = null;

        @Override
        public void mousePressed(MouseEvent e) {
            Integer mousePressedX,mousePressedY;
            mousePress = e.getPoint();
            mousePressedX = e.getX();
            mousePressedY = e.getY();
            System.out.println("mousePressed  X = :" + mousePressedX + " Y=:" + mousePressedY);
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
            Integer mouseReleasedX,mouseReleasedY;
            mouseReleasedX = e.getX();
            mouseReleasedY = e.getY();
            System.out.println("mouseReleased X=:" + mouseReleasedX + " Y=:" + mouseReleasedY);
            drawing = false;
            repaint();
            focus = image.getSubimage((int)rect.getX(),(int)rect.getY(),(int)rect.getWidth(), (int)rect.getHeight());
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (rect != null) {
                rect = null;
            }
        }
    }
    public double[] getCoordinate() {
        double[] getCoordinate = {rect.getX(),rect.getY(), rect.getX()+rect.getWidth(), rect.getY()+rect.getHeight()};
        return getCoordinate;
    }
}