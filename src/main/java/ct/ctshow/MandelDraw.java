package ct.ctshow;

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

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;

public class MandelDraw extends JPanel {
    private static final Color DRAWING_RECT_COLOR = new Color(200, 200, 255);
    private static final Color DRAWN_RECT_COLOR = Color.blue;

    public BufferedImage image=null,focus=null;
    private Rectangle rect = null;
    private boolean drawing = false;
    public int x1,y1,x2,y2,x,y,width,height;
    private int serialNum = 0;
    private String saveImgPath;
    private String imagePath;


    public MandelDraw() {
    }
    public void refreshImage(String imagePath){
        try {
            this.imagePath=imagePath;
            image = ImageIO.read(new File(imagePath));
            MyMouseAdapter mouseAdapter = new MyMouseAdapter();
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
            this.setVisible(true);
            this.getParent().getHeight();
            Integer imageX,imageY;
            imageX=(this.getParent().getWidth()-image.getWidth())/2;
            imageY=(this.getParent().getHeight()-image.getHeight())/2;
            setBounds(imageX,imageY,image.getWidth(),image.getHeight());
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void refreshResult(String result){
        removeAll();
        JPanel resultImageJPanel =new JPanel();
        resultImageJPanel.setLayout(null);
        resultImageJPanel.setBackground(Color.BLACK);
        resultImageJPanel.setBounds(0,0,image.getWidth()/6,image.getHeight()/6+40);
        JLabel resultImage = new JLabel();
        resultImage.setBounds(focus.getWidth()>image.getWidth()/6?0:(image.getWidth()/6-focus.getWidth())/2,
                focus.getHeight()>image.getHeight()/6?0:(image.getHeight()/6-focus.getHeight())/2,
               focus.getWidth(),
               focus.getHeight());
        resultImage.setIcon(new ImageIcon(focus));
        resultImageJPanel.add(resultImage);
        JLabel resultText = new JLabel("病症 : "+result);
        resultText.setForeground(Color.RED);
        resultText.setOpaque(false);
        resultText.setFont(new Font("Dialog",0,14));
        resultText.setBounds(0,image.getHeight()/6,image.getWidth()/6,40);
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

    public String getImagePath() {
        return imagePath;
    }

    class MyMouseAdapter extends MouseAdapter {
        private Point mousePress = null;
        @Override
        public void mousePressed(MouseEvent e) {
            mousePress = e.getPoint();
            x1 = e.getX();
            y1 = e.getY();
            System.out.println("x1:"+x1+" y1:"+y1);
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            drawing = true;
            x = Math.min(mousePress.x, e.getPoint().x);
            y = Math.min(mousePress.y, e.getPoint().y);
            width = Math.abs(mousePress.x - e.getPoint().x);
            height = Math.abs(mousePress.y - e.getPoint().y);

            rect = new Rectangle(x, y, width, height);
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            x2 = e.getX();
            y2 = e.getY();
            System.out.println("x2:"+x2+" y2:"+y2);
            drawing = false;
            repaint();
            focus = image.getSubimage(x,y,width,height);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(rect!=null){
                rect = null;
            }
        }
    }

    public double[] getCoordinate(){
        double[] getCoordinate = {x1,y1,x2,y2};
        return getCoordinate;
    }

    public void removeImg(){
        if(image!=null){
            image = null;
        }
    }
    public void saveImage(){
        try{
            serialNum++;
            String name = date()+String.valueOf(serialNum)+"."+"jpg";

            File f = new File("./src/main/resources",name);

            System.out.println(name);
            ImageIO.write(focus, "jpg", f);
            saveImgPath = f.getAbsolutePath();
        }catch (Exception ex) {
            System.out.println(ex);
        }
    }
}