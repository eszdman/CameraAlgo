import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.io.FileNotFoundException;

import static org.jocl.CL.*;
import static org.jocl.CL.CL_TRUE;
import static org.jocl.CL.clEnqueueReadBuffer;

public class WayBackColor {
    public static int[] GetImage(int[] GrayScale,double[] colorCoeff){
        int[] output = new int[GrayScale.length];
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "kernel void sampleKernel(global int *A,global const double *B, global int *C){\n" +
                "const int x = get_global_id(0);\n" +
                "C[x*3] = (int)((A[x*3])*B[x*3]);\n" +
                "C[x*3+1] = (int)((A[x*3+1])*B[x*3+1]);\n" +
                "C[x*3+2] = (int)((A[x*3+2])*B[x*3+2]);\n" +
                "}\n";
        Clu.InitCl("Gr+Col");
        Pointer srcA = Pointer.to(GrayScale);
        Pointer srcB = Pointer.to(colorCoeff);
        Pointer out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * GrayScale.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * colorCoeff.length, srcB, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_int  * GrayScale.length, null, null);
        Clu.setArg(3);
        long global_work_size[] = new long[]{GrayScale.length/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);
        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                output.length * Sizeof.cl_int, out, 0, null, null);
        Clu.ReleaseAll(3);
        return output;
    }
    public static short[] GetImage(short[] GrayScale,double[] colorCoeff){
        short[] output = new short[GrayScale.length];
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "kernel void sampleKernel(global const short *A,global const double *B, global short *C){\n" +
                "const int x = get_global_id(0);\n" +
                "C[x*3] = (short)((A[x*3])*B[x*3]);\n" +
                "C[x*3+1] = (short)((A[x*3+1])*B[x*3+1]);\n" +
                "C[x*3+2] = (short)((A[x*3+2])*B[x*3+2]);\n" +
                "}\n";
        Clu.InitCl("Gr+Col");
        Pointer srcA = Pointer.to(GrayScale);
        Pointer srcB = Pointer.to(colorCoeff);
        Pointer out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * GrayScale.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * colorCoeff.length, srcB, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short  * GrayScale.length, null, null);
        Clu.setArg(3);
        long global_work_size[] = new long[]{GrayScale.length/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);
        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                output.length * Sizeof.cl_short, out, 0, null, null);
        Clu.ReleaseAll(3);
        return output;
    }
    public static short[] GetImage(short[] GrayScale,short[] colorCoeff){
        short[] output = new short[GrayScale.length];
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "kernel void sampleKernel(global const short *A,global const short *B, global short *C){\n" +
                "const int x = get_global_id(0);\n" +
                "C[x*3] = (short)((double)(A[x*3])*(double)(B[x*3])/10000);\n" +
                "C[x*3+1] = (short)((double)(A[x*3+1])*(double)(B[x*3+1])/10000);\n" +
                "C[x*3+2] = (short)((double)(A[x*3+2])*(double)(B[x*3+2])/10000);\n" +
                "}\n";
        Clu.InitCl("Gr+Col");
        Pointer srcA = Pointer.to(GrayScale);
        Pointer srcB = Pointer.to(colorCoeff);
        Pointer out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * GrayScale.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_short * colorCoeff.length, srcB, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short  * GrayScale.length, null, null);
        Clu.setArg(3);
        long global_work_size[] = new long[]{GrayScale.length/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);
        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                output.length * Sizeof.cl_short, out, 0, null, null);
        Clu.ReleaseAll(3);
        return output;
    }
}
