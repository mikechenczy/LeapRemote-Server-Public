package com.mj.leapremote.util;

import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtils {
    public static BufferedImage imageToBufferedImage(Image image) {
        if(image==null)
            return null;
        ImageIcon icon = new ImageIcon(image);
        BufferedImage dest = new BufferedImage(
                icon.getIconWidth(), icon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = dest.createGraphics();
        dest=g2.getDeviceConfiguration().createCompatibleImage(icon.getIconWidth(),icon.getIconHeight(),Transparency.TRANSLUCENT);
        g2.dispose();
        g2=dest.createGraphics();
        g2.setColor(new Color(255,0,0));
        g2.setStroke(new BasicStroke(1));
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return dest;
    }
    public static byte[] bufferedImageToByteArray(BufferedImage bufferedImage){
        if(bufferedImage==null)
            return null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return out.toByteArray();
    }

    public static BufferedImage byteArrayToBufferedImage(byte[] bytes){
        if(bytes==null)
            return null;
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String byteArrayToString(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] stringToByteArray(String str){
        return Base64.getDecoder().decode(str);
    }

    public static ImageIcon stringToImage(String str) {
        return byteArrayToImageIcon(stringToByteArray(str));
    }

    public static String imageToString(ImageIcon imageIcon) {
        return byteArrayToString(imageIconToByteArray(imageIcon));
    }

    public static ImageIcon byteArrayToImageIcon(byte[] bytes) {
        return bytes==null?null:new ImageIcon(byteArrayToBufferedImage(bytes));
    }

    public static byte[] imageIconToByteArray(ImageIcon imageIcon) {
        return bufferedImageToByteArray(imageToBufferedImage(imageIcon.getImage()));
    }

    public static String getIcon(String name) {
        String icon="";
        String []names = name.split("\\.");
        String suffix = names[names.length-1].toLowerCase();
        switch(suffix) {
            case "mp3":
                icon="#icon-file_music";
                break;
            case "zip":
                icon="#icon-file_zip";
            case "mp4":
            case "avi":
            case "wmv":
                icon="#icon-file_video";
                break;
            case "txt":
                icon="#icon-file_txt";
                break;
            case "pdf":
                icon="#icon-file_pdf";
                break;
            case "docx":
            case "doc":
                icon="file_word_office";
                break;
            case "ppt":
                icon="file_ppt_office";
                break;
            case "jpg":
            case "jpeg":
            case "png":
                icon="#icon-file_pic";
                break;
            case "xlsx":
                icon="file_excel_office";
                break;
            case "css":
                icon="#icon-file_css";
                break;
            case "java":
            case "class":
            case "jar":
                icon="#icon-file_code";
                break;
            default :
                icon="#icon-file_unknown";
        }
        return icon;
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, 1);

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                image.setRGB(x, y, matrix.get(x, y) ? -16777216 : -1);
            }
        }

        return image;
    }
}
