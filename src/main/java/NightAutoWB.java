import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.io.FileNotFoundException;

import static org.jocl.CL.*;

public class NightAutoWB {
    public static double[] Run(double[] in){
        OpenClUtils Clu = new OpenClUtils(2);
            Clu.InitCl("C4");
        double[] output = new double[3];
        Pointer srcA = Pointer.to(in);
        Pointer out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_double * in.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_double * output.length, null, null);
        Clu.setArg(2);

        long global_work_size[] = new long[]{in.length/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[1], CL_TRUE, 0,
                output.length * Sizeof.cl_double, out, 0, null, null);
        Clu.ReleaseAll(2);
        return output;
    }
}
