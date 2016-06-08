package ctshow;

import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by adminstrator on 2016/6/7.
 */
public class CTResultData extends JPanel{
    private JLabel resultImage;
    private  JLabel resultText;
    public CTResultData(){
            resultImage = new JLabel();
            add(resultImage);
            resultText = new JLabel();
            resultText.setLayout(new FlowLayout(FlowLayout.CENTER));
            resultText.setOpaque(false);//背景透明
            resultText.setForeground(Color.RED);//字体颜色
            resultText.setFont(new Font("Dialog",0,14));//字体大小
            add(resultText);
            setVisible(false);
    }
    public void ctResultRefresh(int ImageWIDTH,int ImageHEIGHT,BufferedImage focus,String result){
//        removeAll();
        int ZOOM_WIDTH=ImageWIDTH/ 6;
        int ZOOM_HEIGHT=ImageHEIGHT/6;
        setLayout(null);
        setBackground(Color.BLACK);
        resultImage.setIcon(new ImageIcon(ImageUtil.zoom(focus,ZOOM_WIDTH,ZOOM_HEIGHT,Color.BLACK)));
        resultImage.setBounds(0,0,ZOOM_WIDTH,ZOOM_HEIGHT);
        resultText.setText(result);
        resultText.setBounds(20,ZOOM_HEIGHT,ZOOM_WIDTH,40);
        setVisible(true);
        addNotify();
        repaint();
    }
}
