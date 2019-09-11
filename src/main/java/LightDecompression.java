import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.io.FileNotFoundException;

import static org.jocl.CL.*;

public class LightDecompression {
    public static double[] Run(double[]col, double[]correction){
        OpenClUtils Clu = new OpenClUtils(3);
            Clu.InitCl("C5");
        Pointer srcA = Pointer.to(correction);
        Pointer out = Pointer.to(col);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_double * correction.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_double * col.length, out, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_double * col.length, null, null);
        Clu.setArg(3);
        long global_work_size[] = new long[]{col.length/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                col.length * Sizeof.cl_double, out, 0, null, null);
        Clu.ReleaseAll(3);
        return col;
    }
}
