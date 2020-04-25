import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_mem;
import org.opencv.core.Point;
import org.tensorflow.op.core.Add;

import static org.jocl.CL.*;

public class FastConv {
    public static float[] CenteredMirror(float[] kernel){
        float[] output = new float[kernel.length*2-1];
        for(int i = 0; i<output.length; i++){
            if(i < kernel.length)output[i] = kernel[kernel.length-i-1];
            else output[i] = kernel[i-kernel.length+1];
        }
        return output;
    }
    public static short[] Hr(short[] in, ShotUtils Utils, int power, float[] kernel) {
        kernel = CenteredMirror(kernel);
        double sum = 0;
        for(int i =0; i<kernel.length; i++) sum += kernel[i];
        double koeff = 1/Math.abs(sum);
        for(int i = 0; i<kernel.length; i++){
            kernel[i] *= koeff;
        }
        float[] arr = kernel;
        int Rad = arr.length;
        Utils.Algo.FastblurHr.name = "FastConvHr";
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
    public static short[] Vr(short[] in, ShotUtils Utils, int power, float[] kernel) {
        kernel = CenteredMirror(kernel);
        double sum = 0;
        for(int i =0; i<kernel.length; i++) sum += kernel[i];
        double koeff = 1/Math.abs(sum);
        for(int i = 0; i<kernel.length; i++){
            kernel[i] *= koeff;
        }
        float[] arr = kernel;
        int Rad = arr.length;
        Utils.Algo.FastblurVr.name = "FastConvVr";
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
    public static short[] DeltaVr(short[] in,short[] orig, ShotUtils Utils, int power, float[] kernel) {
        kernel = CenteredMirror(kernel);
        double sum = 0;
        for(int i =0; i<kernel.length; i++) sum += kernel[i];
        double koeff = 1/Math.abs(sum);
        for(int i = 0; i<kernel.length; i++){
            kernel[i] *= koeff;
        }
        float[] arr = kernel;
        int Rad = arr.length;
        Utils.Algo.FastblurDeltaVr.name = "FastConvDeltaVr";
        int k = 2;
        short[] t = new short[k];
        t[0] = (short)(Utils.width/power);
        t[1] = (short)Rad;
        short[] out = new short[in.length];
        Pointer srcA = Pointer.to(t);
        Pointer In = Pointer.to(in);
        Pointer Out = Pointer.to(out);
        Pointer ker = Pointer.to(arr);
        Pointer delta = Pointer.to(orig);
        cl_mem[] memObjects = new cl_mem[Utils.Algo.FastblurDeltaVr.inNum];
        memObjects[0] = clCreateBuffer(Utils.Algo.FastblurDeltaVr.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * t.length, srcA, null);
        memObjects[1] = clCreateBuffer(Utils.Algo.FastblurDeltaVr.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, In, null);
        memObjects[2] = clCreateBuffer(Utils.Algo.FastblurDeltaVr.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short * in.length, null, null);
        memObjects[3] = clCreateBuffer(Utils.Algo.FastblurDeltaVr.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_float * arr.length, ker, null);
        memObjects[4] = clCreateBuffer(Utils.Algo.FastblurDeltaVr.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * orig.length, delta, null);
        Utils.Algo.FastblurDeltaVr.setArg(memObjects);

        long global_work_size[] = new long[]{(in.length+Utils.width*Rad + Rad)/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Utils.Algo.FastblurDeltaVr.commandQueue, Utils.Algo.FastblurDeltaVr.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Utils.Algo.FastblurDeltaVr.commandQueue, memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_short, Out, 0, null, null);
        Utils.Algo.FastblurDeltaVr.ReleaseAll(memObjects);
        //if(res) out = Resizer.Upsampling(in, width, power);
        return out;
    }
    public static short[] Run(short[] in, ShotUtils Utils, int power, float[] kernel){
        boolean res = true;
        if(power == 1) res = false;
        if(res) in = Resizer.Binning(in,Utils, power);
        short[] out= Vr(Hr(in,Utils,power,kernel),Utils,power,kernel);
        if(res) out = Resizer.Upsampling(out, Utils, power);
        return out;
    }
    public static short[] DeltaRun(short[] in, ShotUtils Utils, int power, float[] kernel){
        boolean res = true;
        if(power == 1) res = false;
        if(res) in = Resizer.Binning(in,Utils, power);
        short[] original = new short[in.length];
        original = in;
        short[] out= DeltaVr(Hr(in,Utils,power,kernel),original,Utils,power,kernel);
        if(res) out = Resizer.Upsampling(out, Utils, power);
        return out;
    }
}
