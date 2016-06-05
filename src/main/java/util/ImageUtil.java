package util;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Created by zzq on 16/5/29.
 */
public class ImageUtil {
    public static BufferedImage zoom(java.awt.Image srcImage, Integer width, Integer height,Color backgroundColor) {
        //构造一个预定义的图像类型的BufferedImage对象。
        BufferedImage buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
        //创建Graphics2D对象，用于在BufferedImage对象上绘图。
        Graphics2D g = buffImg.createGraphics();
        //设置图形上下文的当前颜色为白色。
//        g.setBackground(null);
        g.setColor(backgroundColor);
        //用图形上下文的当前颜色填充指定的矩形区域。
        g.fillRect(0, 0, width, height);
        //按照缩放的大小在BufferedImage对象上绘制原始图像。
        g.drawImage(srcImage, 0, 0, width, height, null);
        //释放图形上下文使用的系统资源。
        g.dispose();
        //刷新此 Image 对象正在使用的所有可重构的资源.
        srcImage.flush();
        return buffImg;
    }
}
