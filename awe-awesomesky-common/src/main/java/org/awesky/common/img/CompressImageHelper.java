package org.awesky.common.img;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by vme on 2017/8/28.
 */
public class CompressImageHelper {

    private String imgSrc;
    private String imgDist;
    private File srcFile;

    public CompressImageHelper() { }

    public CompressImageHelper(String imgsrc) {
        this(imgsrc, imgsrc);
    }

    public CompressImageHelper(String imgsrc, String imgdist) {
        this.srcFile = new File(imgsrc);
        if (!srcFile.exists()) {
            throw new RuntimeException("原文件不存在!");
        }
        this.imgSrc = imgsrc;
        this.imgDist = imgdist;
    }

    public void setImgSrc(String imgsrc) {
        this.srcFile = new File(imgsrc);
        if (!this.srcFile.exists()) {
            throw new RuntimeException("原文件不存在!");
        }
        this.imgSrc = imgsrc;
    }

    public void setImgDist(String imgdist) {
        this.imgDist = imgdist;
    }

    public void compress(int width, int height) {
        compressImage(width, height, null);
    }

    public void compress(float rate) {
        compressImage(0,0,rate);
    }

    private void compressImage(int widthdist,int heightdist, Float rate) {
        try {
            if (rate != null && rate > 0) {
                // 获取文件高度和宽度
                int[] results = getImageSize(this.srcFile);
                if (results == null || results[0] == 0 || results[1] == 0) {
                    return;
                } else {
                    widthdist = (int) (results[0] * rate);
                    heightdist = (int) (results[1] * rate);
                }
            }
            // 开始读取文件并进行压缩
            Image src = javax.imageio.ImageIO.read(this.srcFile);
            BufferedImage tag = new BufferedImage((int) widthdist,(int) heightdist, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);
            FileOutputStream out = new FileOutputStream(this.imgDist);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private int[] getImageSize(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int[] result = { 0, 0 };
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            result[0] = src.getWidth(null); // 得到源图宽
            result[1] = src.getHeight(null); // 得到源图高
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        CompressImageHelper compress = new CompressImageHelper("E:/workspace_vme/ui/1.jpg");
        //compress.compress(0.5f);
        compress.compress(1376, 768);
    }

}
