import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.io.FileNotFoundException;

import static org.jocl.CL.*;

public class GetColorCoefficients {
    public static double[] GetColorCoeff(int[] inGray, int[] inColor){
        double[] output = new double[inGray.length];
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "kernel void sampleKernel(global const int *A,global const int *B, global double *C){\n" +
                "const int x = get_global_id(0);\n" +
                "C[x*3] = (double)(B[x*3]+1)/(double)(A[x*3]+1);\n" +
                "C[x*3+1] = (double)(B[x*3+1]+1)/(double)(A[x*3+1]+1);\n" +
                "C[x*3+2] = (double)(B[x*3+2]+1)/(double)(A[x*3+2]+1);\n" +
                "}";
            Clu.InitCl("ColorCoeff");
        Pointer srcA = Pointer.to(inGray);
        Pointer srcB = Pointer.to(inColor);
        Pointer out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * inGray.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * inColor.length, srcB, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_double  * inColor.length, null, null);
        Clu.setArg(3);
        long global_work_size[] = new long[]{inGray.length/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);
        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                output.length * Sizeof.cl_double, out, 0, null, null);
        Clu.ReleaseAll(3);
        return output;
    }
    public static short[] GetColorCoeff(short[] inGray, short[] inColor){
        short[] output = new short[inGray.length];
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "kernel void sampleKernel(global const short *A,global const short *B, global short *C){\n" +
                "const int x = get_global_id(0);\n" +
                "C[x] = (short)(((double)(B[x])/(double)((A[x])+0.001)*10000));\n" +
                "}";
        Clu.InitCl("ColorCoeff");
        Pointer srcA = Pointer.to(inGray);
        Pointer srcB = Pointer.to(inColor);
        Pointer out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * inGray.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * inColor.length, srcB, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short  * inColor.length, null, null);
        Clu.setArg(3);
        long global_work_size[] = new long[]{inGray.length};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);
        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                output.length * Sizeof.cl_short, out, 0, null, null);
        Clu.ReleaseAll(3);
        return output;
    }
}
