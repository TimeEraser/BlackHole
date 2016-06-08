package ctshow;

import javax.swing.*;
import java.awt.*;

/**
 * Created by adminstrator on 2016/6/7.
 */
public class CTData extends JPanel{
    public CTData(CTCurrentData ctCurrentData,JPanel resultImagePanel,int WIDTH,int HEIGHT){
        setSize(WIDTH,HEIGHT);
        setLayout(null);
        resultImagePanel.setBounds(0,0,85,125);
        resultImagePanel.repaint();
        setBounds(0,0,512,512);
        add(resultImagePanel);
        add(ctCurrentData);
    }
}
