package org.awesky.common.barcode;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@SuppressWarnings(value="all")
public class CodeUtil {

    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "PNG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 500;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;
    
    private static final Random random = new Random();
    static final String FORMAT = "MMddHHmmss";
    
    static final SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
    
    public static String getOrder() {
    	String time =sdf.format(new java.util.Date());
    	String order = "";
        for(int i=0;i<3;i++)
            order  = order+(char)(random.nextInt(26)+65);
        return order+time;
    }

    private static BufferedImage createImage(String content, String imgPath,
            boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
                        : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
     // 插入图片
        CodeUtil.insertImage(image, imgPath, needCompress);
        return image;
    }

   
    private static void insertImage(BufferedImage source, String imgPath,
            boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println(""+imgPath+"   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) {// 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
     // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

   /**
    * 生成二维码
    */
    public static byte[] encode(String content, String imgPath, String destPath,
            boolean needCompress) {
    	byte[] buf;
    	try{
	        BufferedImage image = CodeUtil.createImage(content, imgPath,
	                needCompress);
	        mkdirs(destPath);
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        ImageIO.write(image, FORMAT_NAME, out);
	        out.close();
			buf = out.toByteArray();
			return buf;
    	}catch(Exception e) {
    		throw new RuntimeException("生成二维码失败");
    	}
    }
    /**
     * 生成条形二维码
     */
    public static byte[] encode(String contents, int width, int height, String imgPath) {
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, width);
        byte[] buf = null;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    BarcodeFormat.CODE_128, codeWidth, height, null);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
 	        ImageIO.write(image, FORMAT_NAME, out);
 	        out.close();
 			buf = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buf;
    }
    

   
    public static void mkdirs(String destPath) {
        File file =new File(destPath);   
        //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

   
    public static void encode(String content, String imgPath, String destPath)
            throws Exception {
        CodeUtil.encode(content, imgPath, destPath, false);
    }

   
    public static void encode(String content, String destPath,
            boolean needCompress) throws Exception {
        CodeUtil.encode(content, null, destPath, needCompress);
    }

   
    public static void encode(String content, String destPath) throws Exception {
        CodeUtil.encode(content, null, destPath, false);
    }

   
    public static void encode(String content, String imgPath,
            OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = CodeUtil.createImage(content, imgPath,
                needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

   
    public static void encode(String content, OutputStream output)
            throws Exception {
        CodeUtil.encode(content, null, output, false);
    }

   
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        CodeImage source = new CodeImage(
                image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }
    
	/**
	 * 解析条形码
	 */
    public static String decodes(String imgPath) {
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(new File(imgPath));
            if (image == null) {
                System.out.println("the decode image may be not exit.");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
 
            result = new MultiFormatReader().decode(bitmap, null);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
   
    public static String decode(String path) throws Exception {
        return CodeUtil.decode(new File(path));
    }
   
   
    public static void main(String[] args) throws Exception {
        String text = "http://www.dans88.com.cn";
        byte[] buf = CodeUtil.encode(text, null, "", true);
        System.out.println(buf.length);
    }
   
}