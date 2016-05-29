import actor.Listener.NoticeListener;
import ct.ctshow.ImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by zzq on 16/5/29.
 */
public class Test extends JFrame {
    public Test(){

    }
    public static void main(String[] args) {
        //JScrollPane jScrollPane =new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        try {
            ImagePanel imagePanel=new ImagePanel(ImageIO.read(new File("/Users/macbook/Documents/IdeaProject/BlackHole/res/肝   癌/肝   癌20160529141030.png")));
            Test kk=new Test();
            kk.add(imagePanel);
            kk.repaint();
            kk.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //test1.add(jScrollPane);

    }
}
