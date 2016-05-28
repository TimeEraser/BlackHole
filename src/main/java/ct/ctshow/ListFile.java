package ct.ctshow;

import actor.CtActor;
import actor.config.CtActorConfig;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;

/**
 * Created by ChaomingGu on 2016/5/28.
 */
public class ListFile  {
    private JList<String> fileList;
    private CtActor ctActor = new CtActor(new CtActorConfig());
    private CTDataRefresher ctDataRefresher = new CTDataRefresher(new MandelDraw());
    private static final String dirPath = "./res";
    private String listName = null;
    private String listPath =null;
    public ListFile() {
        int j, fileNum;
        //String listPath =null;
        DefaultListModel<String> listModel = new DefaultListModel<>();
        File dirFiles = new File(dirPath);
        File[] filesArray = dirFiles.listFiles();
        //String listName = null;
        if (filesArray.length != 0) {
            fileNum = getNumOfFile(dirPath);
            for (j = 0; j < fileNum; j++) {
                listName = filesArray[j].getName();
                listModel.addElement(listName);

            }

            fileList = new JList<>(listModel);
            fileList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    String listValue = fileList.getSelectedValue();
                    File getPathFile = new File(listValue);
                    listPath = getPathFile.getAbsolutePath();
                    ctDataRefresher.refreshCTData(listPath);
                }
            });


        }
    }

    public int getNumOfFile(String path){
        int fileCount=0;
        int i;
        String filter = ctActor.getResult();
        File dirFile = new File(path);
        File[] files = dirFile.listFiles();


        for(i=0; i<files.length;i++) {
            String fileName = files[i].getName();
            if (fileName.indexOf(filter) != -1) {
                fileCount = fileCount + 1;
            }
        }
        return fileCount;

    }

    public JList getList(){
        return fileList;
    }








}
