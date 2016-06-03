package ct.ctshow;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by zzq on 16/5/29.
 */
public class ImagePanel extends JPanel{
    private BufferedImage image;
    public ImagePanel(BufferedImage image){
        this.image=image;
    }
    @Override
    public void paintComponent(Graphics g1) {
        int x = 0;
        int y = 0;
        Graphics g = (Graphics) g1;
        if (null == image) {
            return;
        }
        g.drawImage(image, x, y, image.getWidth(this), image.getHeight(this), this);
        g = null;
    }
}
