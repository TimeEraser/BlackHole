package actor.guard;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by adminstrator on 2016/5/29.
 */
public class AlarmShow extends JPanel implements Observer{
    private JTextArea jTextArea;
    public AlarmShow(){
        jTextArea=new JTextArea();
        jTextArea.setOpaque(true);
        jTextArea.setBackground(Color.WHITE);
        jTextArea.setLayout(new BorderLayout());
        JScrollPane jsp=new JScrollPane(jTextArea);//新建一个滚动条界面，将文本框传入
        jsp.setLayout(new ScrollPaneLayout());
        jsp.getVerticalScrollBar().setValue(jsp.getHorizontalScrollBar().getMaximum());
        this.setLayout(new BorderLayout());
        this.add(jsp);
        this.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        String temp=(String)arg;
        jTextArea.setText(jTextArea.getText()+temp);
        jTextArea.selectAll();
    }
}
