import Neural.UseNetwork;
import org.opencv.core.Algorithm;
import org.opencv.osgi.OpenCVInterface;
import sun.java2d.pipe.AAShapePipe;

import javax.rmi.CORBA.Util;
import java.awt.font.GraphicAttribute;
import java.io.IOException;
public class Main {
    public static void main(String[] args) throws IOException {
        ConvertImageToByteArray Read = new ConvertImageToByteArray();
        //Read.directory = "C:\\Users\\eszdman\\IdeaProjects\\CameraAlgo\\test";
        Read.directory = "H:\\Camera\\test";
        Read.name= "0";
        byte[] t1 = Read.getRGB(); //read 0.jpg
        ShotUtils utils = new ShotUtils();
        utils.Init();
        utils.width = Read.width;
        utils.height = Read.height;
        LightCycle2 Cycle = new LightCycle2();
        Wavelet wavelet = new Wavelet();
        Cycle.dir = Read.directory;
        for(int i = 0; i<1; i++) Cycle.output = Cycle.Run(i); //read 0.jpg 1.jpg 2.jpg...
       short[] GrayScale =  GetGray.GrayArray(Cycle.output);
       short[] color = GetColorCoefficients.GetColorCoeff(GrayScale, Cycle.output);
       color = MedianFilter.Run(color,utils,2,10);
       //GrayScale = MedianFilter.Run(GrayScale,Read.width,1,9);
       //color = Blur.Run(color, utils,2,15);
       //color = WaveletDenoise.Run(color,utils,37.5,12,2.0);
        int Rmin = 5;
        int Rmax = 25;
        //Cycle.output = GrayScale;
        wavelet.Run(Cycle.output,utils,3,75,6);
        short[][] wav = (short[][])wavelet.waves;
        short[] Activation = ActivationMask.Get(GetGray.GrayArray(wav[3]));

        Read.getImage(toByte.start(Activation,64),"ActivationEdge");

        wav[1] = Blur.Run(wav[1],wav[5],Activation);
        short[] ActivationCol = ActivationMask.Get(wav[2]);
        color = Blur.Run(color, ActivationCol, utils, 4, 25);
        wav[0] = Ops.Run(wav[0], "*1.5"); //FastOperations
        wav[1] = Ops.Run(wav[1], "*1.1");
        wav[2] = Ops.Run(wav[2], "*1");
        /*wav[3] = Ops.Run(wav[3], "*1+4000");
        wav[4] = Ops.Run(wav[4], "*1+4000");
        wav[5] = Ops.Run(wav[5], "*1+4000");*/
        for(int i = 0; i<wav.length; i++){
            Read.getImage(toByte.start(Ops.Run(wav[i],"+4000"),64),"Wave"+i);
        }

        GrayScale = GetGray.GrayArray((short[])wavelet.Image());wavelet = null;
        //GrayScale = Ops.Run(GrayScale, "*0+4000");
        Cycle.output = WayBackColor.GetImage(GrayScale, color);
        //Cycle.output = (short[])wavelet.Image();
        //Cycle.output = Blur.Run(Cycle.output,Read.width,1,150.1);
       Cycle.output = Normalize.Run(Cycle.output, utils);
        Read.getImage(toByte.start(Cycle.output,64));
    }
}
