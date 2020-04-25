import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.io.IOException;

import static org.jocl.CL.*;

/**
 * Created by eszdman on 25.12.2018.
 */
public class LightCycle2 {
    String dir;
    short[] output;
    public short[] Run(int name) {
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "kernel void sampleKernel(global const short *A,global const short *B, global short *C){\n" +
                "const int x = get_global_id(0);\n" +
                "C[x] = (A[x]+B[x])/2;\n" +
                "}";
        Clu.InitCl("T3");
        ConvertImageToByteArray Read = new ConvertImageToByteArray();
        Read.directory = dir;
        int num = 3;
        Read.name = String.valueOf(name);
        byte[] in = new byte[0];
            in = Read.getRGB();
        short[] t = new short[in.length];
        for (int i = 0; i < in.length; i++) {
            t[i] = (short)((in[i] & 0xff)*64);
        }
        Pointer srcA = Pointer.to(t);
        if(name == 0){
            output = new short[t.length];
            output = t;
        }
        Pointer out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * t.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * t.length, out, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short * in.length, null, null);
        Clu.setArg(3);

        long global_work_size[] = new long[]{in.length};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null, //start
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                output.length * Sizeof.cl_short, out, 0, null, null);
        Clu.ReleaseAll(3);
        return output;
    }
}
