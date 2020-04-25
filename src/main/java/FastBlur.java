import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_mem;
import org.tensorflow.op.core.Add;

import static org.jocl.CL.*;

public class FastBlur {
    public static float[] GenKernel(double Radius){
        int Rad = (int)Radius;
        double added = Radius-Rad;
        boolean edKernel = false;
        float[] output = new float[Rad];
        if(added != 0) {
            output = new float[Rad+1];
            for (int i = 0; i < Rad; i++) {
                output[(int)i] = (float) Math.exp(-((double) i * i) / (Rad+added));
                //output[(int)i] = (float) (20f*Math.sin(i)/i);
            }
            double x = Rad-1 + added;
            output[Rad] = (float) Math.exp(-(x*x) / Rad);
        } else
            for (int i = 0; i < Rad; i++) {
                output[i] = (float) Math.exp(-((double) i * i) / Rad);
                output[(int)i] = (float) (1f*Math.sin(20*i)/i);
                if(i == 0) output[0] = 1;
        }
        return output;
    }
    public static float[] SharpKernel(double Radius, double impulse){
        int Rad = (int)Radius;
        double added = Radius-Rad;
        boolean edKernel = false;
        float[] output = new float[Rad];
        if(added != 0) {
            output = new float[Rad+1];
            for (int i = 0; i < Rad; i++) {
                output[i] = (float) Math.exp(-((double) i * i) / (Rad+added));
            }
            double x = Rad-1 + added;
            output[Rad] = (float) Math.exp(-(x*x) / Rad);
        } else
            for (int i = 0; i < Rad; i++) {
                output[i] = (float) (Math.exp(-((double) i * i) / Rad) - impulse);
            }
            double sum = 0;
        for(int i =0; i<output.length; i++) sum += output[i];
        double koeff = 1/Math.abs(sum);
        for(int i = 0; i<output.length; i++){
            output[i] *= koeff;
            output[i] = (float)(output[i]-impulse);
        }
        return output;
    }
     public static short[] Run(short[] in, ShotUtils Utils, int power, double Rad){

            return FastConv.Run(in,Utils,power,GenKernel(Rad));
     }
    public static short[] SharpMask(short[] in, ShotUtils Utils, int power, double Rad){

        return FastConv.DeltaRun(in,Utils,power,GenKernel(Rad));
    }
}
