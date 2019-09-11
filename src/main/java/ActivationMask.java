import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.nio.Buffer;

import static org.jocl.CL.*;
import static org.jocl.Sizeof.cl_short;

public class ActivationMask {
    public static short[] Get(short[] in) {
        short[] output = new short[in.length];
        Pointer srcA = Pointer.to(in);
        Pointer out = Pointer.to(output);
        OpenClUtils Clu = new OpenClUtils(2);
        Clu.programSource = "kernel void sampleKernel(global const short *A,global short *B){\n" +
                "const int x = get_global_id(0);" +
                "short t = 0;" +
                "if(abs(A[x])> 70)\n" +
                "B[x] = 4000;" +
                "else B[x] = 0;\n" +
                "}";
        Clu.InitCl("GetActivation");
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                cl_short * in.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, in.length*cl_short, null, null);
        Clu.setArg(2);
        long global_work_size[] = new long[]{in.length};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[1], CL_TRUE, 0,
                in.length*cl_short, out, 0, null, null);
        Clu.ReleaseAll(2);
        return output;
    }
}
