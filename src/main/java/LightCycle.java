import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.jocl.CL.*;

/**
 * Created by eszdman on 25.12.2018.
 */
public class LightCycle {

    public static void main(String[] args)  throws IOException {
        OpenClUtils Clu = new OpenClUtils(5);
        Clu.InitCl("T2");
        ConvertImageToByteArray Read = new ConvertImageToByteArray();
        Read.directory = "C:\\Users\\eszdman\\IdeaProjects\\CameraAlgo\\test";
        /*byte[] in = Read.getRGB();
        Read.name = "2";
        byte[] in2 = Read.getRGB();
        Read.name = "2";
        byte[] in3 = Read.getRGB();
        int[] t3 = new int[in2.length];
        int[] t2 = new int[in2.length];
        int[] t = new int[in.length];
        int[] out = new int[in2.length];
        byte[] out2 = new byte[in2.length];*/
        int num = 3;
        Read.name = "0";
        byte[] in = Read.getRGB();
        Read.name = "1";
        byte[] in1 = Read.getRGB();
        Read.name = "2";
        byte[] in2 = Read.getRGB();
        Read.name = "3";
        byte[] in3 = Read.getRGB();
        //System.out.println(read[0][123123]);
        int[] t = new int[in.length];
        int[] t1 = new int[in.length];
        int[] t2 = new int[in.length];
        int[] t3 = new int[in.length];
        for (int i = 0; i < in.length; i++) {
                t[i] = (in[i] & 0xff);
            t1[i] = (in1[i] & 0xff);
            t2[i] = (in2[i] & 0xff);
            t3[i] = (in3[i] & 0xff);
                // t[i] = (in[i]&0xff);
        }
        int[] output = new int[in.length];
        Pointer out = Pointer.to(output);
        Pointer srcA = Pointer.to(t);
        Pointer srcA1 = Pointer.to(t1);
        Pointer srcA2 = Pointer.to(t2);
        Pointer srcA3 = Pointer.to(t3);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * t.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * t1.length, srcA1, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * t2.length, srcA2, null);
        Clu.memObjects[3] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * t3.length, srcA3, null);
        Clu.memObjects[4] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_int * in.length, null, null);
        Clu.setArg(5);

        long global_work_size[] = new long[]{in.length*3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[4], CL_TRUE, 0,
                output.length * Sizeof.cl_int, out, 0, null, null);
        Clu.ReleaseAll(5);
        byte[] ts = new byte[output.length];
        for (int i = 0; i < output.length; i++) {
            ts[i] = (byte) output[i];

        }
        Read.getImage(ts);
    }
}

