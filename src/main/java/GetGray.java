import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.io.FileNotFoundException;

import static org.jocl.CL.*;

public class GetGray {
    public static int[] GrayArray(int[] in) {
        int[] output = new int[in.length];
        Pointer out = Pointer.to(output);
        Pointer srcA = Pointer.to(in);
        OpenClUtils Clu = new OpenClUtils(2);
        Clu.programSource = "kernel void sampleKernel(global const int *A,global int *B){\n" +
                "const int x = get_global_id(0);\n" +
                "B[(x*3)] = (A[x*3] + A[(x*3) + 1] + A[(x*3) + 2])/3;\n" +
                "B[(x*3) + 1] = (A[x*3] + A[(x*3) + 1] + A[(x*3) + 2])/3;\n" +
                "B[(x*3) + 2] = (A[x*3] + A[(x*3) + 1] + A[(x*3) + 2])/3;\n" +
                "}";
            Clu.InitCl("GrayArray");
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * in.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_int * in.length, null, null);
        Clu.setArg(2);
        long global_work_size[] = new long[]{in.length/3 + 2};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[1], CL_TRUE, 0,
                output.length * Sizeof.cl_int, out, 0, null, null);
        Clu.ReleaseAll(2);
        return output;
    }
    public static short[] GrayArray(short[] in) {
        short[] output = new short[in.length];
        Pointer srcA = Pointer.to(in);
        Pointer out = Pointer.to(output);
        OpenClUtils Clu = new OpenClUtils(2);
        Clu.programSource = "kernel void sampleKernel(global const short *A,global short *B){\n" +
                "const int x = get_global_id(0);" +
                "short t = 0;" +
                "t = (A[x*3] + A[(x*3) + 1] + A[(x*3) + 2])/3;\n" +
                "B[(x*3)] = t;\n" +
                "B[(x*3) + 1] = t;\n" +
                "B[(x*3) + 2] = t;\n" +
                "}";
        Clu.InitCl("GrayArray");
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short * in.length, null, null);
        Clu.setArg(2);
        long global_work_size[] = new long[]{in.length/3 + 2};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[1], CL_TRUE, 0,
                in.length * Sizeof.cl_short, out, 0, null, null);
        Clu.ReleaseAll(2);
        return output;
    }
}
