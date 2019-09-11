/**
 * Created by eszdman on 27.08.2018.
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

class ConvertImageToByteArray {
    int width;
    int height;
    boolean hasAlphaChannel;
    String directory;
    String name;
    public byte[] getRGB() throws IOException {
        BufferedImage image = ImageIO.read(new File(directory, name + ".jpg"));
        height = image.getHeight();
        width = image.getWidth();
        hasAlphaChannel = image.getAlphaRaster() != null;
        final byte[] pixels = (((DataBufferByte) image.getRaster().getDataBuffer()).getData());
        return pixels;
    }
    public void getImage(byte[] pixels) throws IOException {
        BufferedImage resultImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                resultImage.setRGB(col,row, argb);
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((pixels[pixel]&0xff))); // blue
                argb += (((pixels[pixel+1]&0xff) << 8)); // green
                argb += (((pixels[pixel+2]&0xff) << 16)); // red
                resultImage.setRGB(col,row, argb);
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
    }
        ImageIO.write(resultImage, "jpg", new File(directory,"imageresult.jpg"));
    }
    public void getImage(byte[] pixels, String name) throws IOException {
        BufferedImage resultImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                resultImage.setRGB(col,row, argb);
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((pixels[pixel]&0xff))); // blue
                argb += (((pixels[pixel+1]&0xff) << 8)); // green
                argb += (((pixels[pixel+2]&0xff) << 16)); // red
                resultImage.setRGB(col,row, argb);
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }
        ImageIO.write(resultImage, "jpg", new File(directory,name+".jpg"));
    }
    public void ImageAuto(int w,int h, byte[]in) throws IOException {
        width = w;
        height = h;
        getImage(in);
    }
}