import javax.swing.*;
import java.awt.*;
import java.lang.management.GarbageCollectorMXBean;

public class Main {
    public static void main(String[] args) {
        ConvertImageToByteArray Read = new ConvertImageToByteArray();
        Read.directory = "E:\\Camera\\test";
        ShotUtils utils = new ShotUtils();
        utils.images = new short[2][];
        Read.name= "shift7.jpg";
        utils.images[utils.imInd++] = byteTo.start(Read.getRGB(),64);
        Read.name= "shift8.jpg";
        utils.images[utils.imInd++] = byteTo.start(Read.getRGB(),64);
        utils.width = Read.width;
        utils.height = Read.height;
        Wavelet wavelet = new Wavelet();
        utils.Init();
        JFrame frame = new JFrame();//init
        Gr Gui = new Gr();
        Gui.size = 500;
        frame.add(Gui);
        frame.setSize(Gui.size+15, Gui.size+40);
        frame.setVisible(true);
        //utils.images[0] = Resizer.Upsampling(Resizer.Binning(utils.images[0],utils,25),utils,25);
        short[] orig1 = new short[utils.images[0].length];
        orig1 = utils.images[0];
        short[] orig2 = utils.images[1];
        //wavelet.Run(utils.images[0],utils,2,20,3);
        /*short[] gray = GetGray.GrayArray(utils.images[0]);
        short[] col = GetColorCoefficients.GetColorCoeff(gray,utils.images[0]);
        gray = FastBlur.Run(gray,utils,1,25);
        col = MedianFilter.Run(col,utils,1,25);
        utils.images[0] = WayBackColor.GetImage(gray,col);
        utils.images[0] = Normalize.Run(utils.images[0],utils);*/
        //utils.images[0] = MedianFilter.Run(utils.images[0],utils,1,13);
        //utils.images[0] = Blur.Run(utils.images[0],utils,2,30);
        //utils.images[0] = Ops.Run(utils.images[0],"*1.10");
        //orig1 = Ops.Run(orig1,"- 16384*0.10");
        //orig1 = Ops.Run(orig1,"*(1.0/0.90)");
        //utils.images[0] = Ops.Run(utils.images[0],"- 16384*0.10");
        //utils.images[0] = Ops.Run(utils.images[0],"*(1.0/0.90)");
        //orig1 = Ops.Run(Ops.Run(utils.images[0],Ops.Run(Ops.Run(orig1,"*(1.0/0.90)"),"- 16384*0.10"),"-"),orig1,"+");
        //utils.images[1] = FastBlur.SharpMask(utils.images[1],utils,2,15);
        //short[][] wav = (short[][])wavelet.waves;
        //Read.getImage(toByte.start(orig1,16),"wavelet");
        //ShiftFinder.GetDetectMatrix(utils,25,4, shift);
        Point shift =  ShiftFinder.Run(utils, 25);System.out.println("ShiftFinder = "+ shift);
        shift = ShiftFinder.Run(utils,shift, 5,10);System.out.println("ShiftFinder = "+ shift);
        shift = ShiftFinder.Run(utils,shift, 1,100);System.out.println("ShiftFinder = "+ shift);
        //Point[] added = new Point[4];
        //Point[] added = ShiftFinder.Run(utils);

        int x = 0;
        int y = 0;
        for(int i =0; i<4; i++){
         //   x += added[2].x;
         //   y += added[2].y;
        }
        x/=4;
        y/=4;
        //System.out.println("ShiftFinder = "+ added[0]);
        //System.out.println("ShiftFinder = "+ added[1]);
        //System.out.println("ShiftFinder = "+ added[2]);
        //System.out.println("ShiftFinder = "+ added[3]);
        //shift = new Point(shift.x+x,shift.y+y);
        utils.images[0] = FrameStacking.Run(orig1,orig2,utils,new Point(shift.x,shift.y));
        //utils.images[0] = Denoise.Denoise(utils.images[0],Read.width);
        Read.getImage(toByte.start(utils.images[0],64),"png",false);
        Gui.setImg(Read.buffer);
    }
}
