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
    private Integer imageWidth = 100;
    private Integer imageHeight = 100;
    private JTable showTable;

    public CTHistoryData(){
        setBounds(0,0,200,300);
    }
    public void refresh(String result){
        removeAll();
        showTable = new JTable();
        showTable.setBounds(0,0,200,200);
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
                showTable.add(show);
                showTable.setVisible(true);
                setViewportView(show);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        //showTable.setVisible(true);
        //this.add(showTable);
        //setViewportView(showTable);
        //setModel(showTable);
        setVisible(true);
        repaint();
    }
    public void addHistory(String path){
        ;
    }
}
