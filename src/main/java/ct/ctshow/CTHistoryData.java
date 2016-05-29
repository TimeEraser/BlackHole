package ct.ctshow;

import actor.config.CtActorConfig;
import config.ConfigCenter;
import util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * Created by zzq on 16/5/29.
 */
public class CTHistoryData extends JScrollPane{
    private String ROOT= ConfigCenter.getString("ct.analyse.result.save.root");
    private String filterFormat=ConfigCenter.getString("ct.analyse.result.save.format");
    private Integer imageWidth = 200;
    private Integer imageHeight = 200;
    private JTable showTable;

    public CTHistoryData(){
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        setBounds(0,0,200,300);
    }
    public void refresh(String result){
        removeAll();
        showTable = new JTable();
        File folder = new File(ROOT+"/"+result);
        if(!folder.exists()||!folder.isDirectory())
            return;
        File[] files=folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(filterFormat);
            }
        });
        for (File f:files) {
            try {
                BufferedImage image = ImageIO.read(f);
                BufferedImage zoomImage = ImageUtil.zoom(image,imageWidth,imageHeight);
                ImagePanel show = new ImagePanel(zoomImage);
                show.setBounds(0,0,200,200);
                show.setVisible(true);
                add(show);
                ImagePanel show1 = new ImagePanel(zoomImage);
                //show.setBounds(0,220,200,200);
                show.setVisible(true);
                add(show1);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        //this.add(showTable);
        this.setVisible(true);
        repaint();
    }
    public void addHistory(String path){
        repaint();
    }
}
