import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_mem;

import static org.jocl.CL.*;

public class Normalize {
    public static int[] Run(int[] in, ShotUtils utils) {
        utils.Algo.Normalize.name = "Normalize";
        int[] output = new int[in.length];
        Pointer In = Pointer.to(in);
        Pointer out = Pointer.to(output);
        cl_mem[] memObjects = new cl_mem[utils.Algo.Normalize.inNum];
        memObjects[0] = clCreateBuffer(utils.Algo.Normalize.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * in.length, In, null);
        memObjects[1] = clCreateBuffer(utils.Algo.Normalize.context,
                CL_MEM_READ_WRITE,
                Sizeof.cl_int * in.length, null, null);
        utils.Algo.Normalize.setArg(memObjects);
        long global_work_size[] = new long[]{in.length};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(utils.Algo.Normalize.commandQueue, utils.Algo.Normalize.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);
        clEnqueueReadBuffer(utils.Algo.Normalize.commandQueue, memObjects[1], CL_TRUE, 0,
                in.length * Sizeof.cl_int, out, 0, null, null);
        utils.Algo.Normalize.ReleaseAll(memObjects);
        return output;
    }
    public static short[] Run(short[] in, ShotUtils utils) {
        utils.Algo.Normalize.name = "Normalize";
        short[] output = new short[in.length];
        Pointer In = Pointer.to(in);
        Pointer out = Pointer.to(output);
        cl_mem[] memObjects = new cl_mem[utils.Algo.Normalize.inNum];
        memObjects[0] = clCreateBuffer(utils.Algo.Normalize.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, In, null);
        memObjects[1] = clCreateBuffer(utils.Algo.Normalize.context,
                CL_MEM_READ_WRITE,
                Sizeof.cl_short * in.length, null, null);
        utils.Algo.Normalize.setArg(memObjects);
        long global_work_size[] = new long[]{in.length};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(utils.Algo.Normalize.commandQueue, utils.Algo.Normalize.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);
        clEnqueueReadBuffer(utils.Algo.Normalize.commandQueue, memObjects[1], CL_TRUE, 0,
                in.length * Sizeof.cl_short, out, 0, null, null);
        utils.Algo.Normalize.ReleaseAll(memObjects);
        return output;
    }
}
