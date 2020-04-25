import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Gr extends JPanel {
    BufferedImage image;
    int size;
    public Gr(){};
    public void redraw(){repaint();}
    public void setImg(BufferedImage in){image = in;repaint();};
    public void paint(Graphics g) {

        g.drawImage(image,0,0,size,size,this);
    }
}
