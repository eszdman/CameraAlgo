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
                output[i] = (float) Math.exp(-((double) i * i) / (Rad+added));
            }
            double x = Rad-1 + added;
            output[Rad] = (float) Math.exp(-(x*x) / Rad);
        } else
            for (int i = 0; i < Rad; i++) {
                output[i] = (float) Math.exp(-((double) i * i) / Rad);
        }
        output = CenteredMirror(output);
        double sum = 0;
        for(int i =0; i<output.length; i++) sum += output[i];
        double koeff = 1/sum;
        for(int i = 0; i<output.length; i++){
            output[i] *= koeff;
        }
        return output;
    }
    public static float[] CenteredMirror(float[] kernel){
        float[] output = new float[kernel.length*2-1];
        for(int i = 0; i<output.length; i++){
            if(i < kernel.length)output[i] = kernel[kernel.length-i-1];
            else output[i] = kernel[i-kernel.length+1];
        }
        return output;
    }
    public static short[] Hr(short[] in, ShotUtils Utils, int power, double radius) {
            float[] arr = GenKernel(radius);
            int Rad = arr.length;
            Utils.Algo.FastblurHr.name = "FastBlurHr";
            int k = 2;
            short[] t = new short[k];
            t[0] = (short)(Utils.width/power);
            t[1] = (short)Rad;
            short[] out = new short[in.length];
            Pointer srcA = Pointer.to(t);
            Pointer In = Pointer.to(in);
            Pointer Out = Pointer.to(out);
            Pointer ker = Pointer.to(arr);
            cl_mem[] memObjects = new cl_mem[Utils.Algo.FastblurHr.inNum];
            memObjects[0] = clCreateBuffer(Utils.Algo.FastblurHr.context,
                    CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_short * t.length, srcA, null);
            memObjects[1] = clCreateBuffer(Utils.Algo.FastblurHr.context,
                    CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_short * in.length, In, null);
            memObjects[2] = clCreateBuffer(Utils.Algo.FastblurHr.context,
                    CL_MEM_READ_WRITE, Sizeof.cl_short * in.length, null, null);
            memObjects[3] = clCreateBuffer(Utils.Algo.FastblurHr.context,
                    CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_float * arr.length, ker, null);
            Utils.Algo.FastblurHr.setArg(memObjects);

            long global_work_size[] = new long[]{(in.length+Utils.width*Rad + Rad)/3};
            long local_work_size[] = new long[]{1};
            clEnqueueNDRangeKernel(Utils.Algo.FastblurHr.commandQueue, Utils.Algo.FastblurHr.kernel, 1, null,
                    global_work_size, local_work_size, 0, null, null);

            clEnqueueReadBuffer(Utils.Algo.FastblurHr.commandQueue, memObjects[2], CL_TRUE, 0,
                    in.length * Sizeof.cl_short, Out, 0, null, null);
            Utils.Algo.FastblurHr.ReleaseAll(memObjects);
            //if(res) out = Resizer.Upsampling(in, width, power);
            return out;
        }
    public static short[] Vr(short[] in, ShotUtils Utils, int power, double radius) {
        float[] arr = GenKernel(radius);
        int Rad = arr.length;
        Utils.Algo.FastblurVr.name = "FastBlurVr";
        int k = 2;
        short[] t = new short[k];
        t[0] = (short)(Utils.width/power);
        t[1] = (short)Rad;
        short[] out = new short[in.length];
        Pointer srcA = Pointer.to(t);
        Pointer In = Pointer.to(in);
        Pointer Out = Pointer.to(out);
        Pointer ker = Pointer.to(arr);
        cl_mem[] memObjects = new cl_mem[Utils.Algo.FastblurVr.inNum];
        memObjects[0] = clCreateBuffer(Utils.Algo.FastblurVr.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * t.length, srcA, null);
        memObjects[1] = clCreateBuffer(Utils.Algo.FastblurVr.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, In, null);
        memObjects[2] = clCreateBuffer(Utils.Algo.FastblurVr.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short * in.length, null, null);
        memObjects[3] = clCreateBuffer(Utils.Algo.FastblurVr.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_float * arr.length, ker, null);
        Utils.Algo.FastblurVr.setArg(memObjects);

        long global_work_size[] = new long[]{(in.length+Utils.width*Rad + Rad)/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Utils.Algo.FastblurVr.commandQueue, Utils.Algo.FastblurVr.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Utils.Algo.FastblurVr.commandQueue, memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_short, Out, 0, null, null);
        Utils.Algo.FastblurVr.ReleaseAll(memObjects);
        //if(res) out = Resizer.Upsampling(in, width, power);
        return out;
    }
     public static short[] Run(short[] in, ShotUtils Utils, int power, double Rad){
         boolean res = true;
         if(power == 1) res = false;
         if(res) in = Resizer.Binning(in,Utils, power);
         short[] out= Vr(Hr(in,Utils,power,Rad),Utils,power,Rad);
         if(res) out = Resizer.Upsampling(out, Utils, power);
            return out;
     }
}
