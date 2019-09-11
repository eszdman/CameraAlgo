import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.io.FileNotFoundException;

import static org.jocl.CL.*;

public class LowPass {
    public static int[] Run(int[] in, double dt, double RC){
        int num = 4;
        OpenClUtils Clu = new OpenClUtils(num);
            Clu.InitCl("T7");
        double[] t = new double[2];
        t[0] = dt;
        t[1] = RC;
        int[] x = new int[1];
        x[0] = 0;
        Pointer xp = Pointer.to(x);
        Pointer srcA = Pointer.to(in);
        Pointer srcB = Pointer.to(t);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * t.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_double * in.length, srcB, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_int * in.length, null, null);
        Clu.memObjects[3] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * x.length, xp, null);
        Clu.setArg(num);

        long global_work_size[] = new long[]{(in.length)/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_int, srcB, 0, null, null);
        Clu.ReleaseAll(num);
        return in;
    }
}
