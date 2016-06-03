package util;

import java.io.File;

/**
 * Created by zzq on 16/5/29.
 */
public class FileUtil {
    public static boolean makeDirs(String folderName){
        if (folderName == null || folderName.isEmpty()) {
            return false;
        }
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }
}
