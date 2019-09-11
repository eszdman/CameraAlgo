import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.io.FileNotFoundException;

import static org.jocl.CL.*;

/**
 * Created by eszdman on 25.12.2018.
 */
public class Denoise {
    public int[] Denoise(int[] in, int width) throws FileNotFoundException {
        int num = 3;
        OpenClUtils Opencl = new OpenClUtils(num);
        Opencl.InitCl("T4");
        int[] red = new int[in.length/3+ width+1];
        int[] green = new int[in.length/3+ width+1];
        int[] blue = new int[in.length/3+ width+1];
        for(int i = 0; i<in.length-3; i+=3){
            red[i/3] = in[i];
            green[i/3] = in[i+1];
            blue[i/3] = in[i+2];
        }
        Pointer rd = Pointer.to(red);
        Pointer gr = Pointer.to(green);
        Pointer bl = Pointer.to(blue);
        int[] ltt = new int[1];
        ltt[0] = width;
        Pointer lt = Pointer.to(ltt);
        Opencl.memObjects[0] = clCreateBuffer(Opencl.context,
                CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * red.length, rd, null);
        Opencl.memObjects[1] = clCreateBuffer(Opencl.context,
                CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * in.length, gr, null);
        Opencl.memObjects[2] = clCreateBuffer(Opencl.context,
                CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * in.length, bl, null);
        /*Opencl.memObjects[3] = clCreateBuffer(Opencl.context,
                CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int, lt, null);*/
        Opencl.setArg(num);
        long global_work_size[] = new long[]{(red.length - 6)};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Opencl.commandQueue, Opencl.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);
        //double k = 0.02;
        /*for(int t = 0; t<in.length/3 - width-1; t++){
            if(t > width)
            red[t] += (red[t-1 - width]+ red[t-width] + red[t-width + 1] + red[t-1] + red[t] + red[t+1] + red[t-1 + width] + red[t+width] + red[t+1+width]) * k;
        }*/

        clEnqueueReadBuffer(Opencl.commandQueue, Opencl.memObjects[0], CL_TRUE, 0,
                red.length * Sizeof.cl_int, rd, 0, null, null);
        clEnqueueReadBuffer(Opencl.commandQueue, Opencl.memObjects[1], CL_TRUE, 0,
                green.length * Sizeof.cl_int, gr, 0, null, null);
        clEnqueueReadBuffer(Opencl.commandQueue, Opencl.memObjects[2], CL_TRUE, 0,
                blue.length * Sizeof.cl_int, bl, 0, null, null);
        Opencl.ReleaseAll(num);
        for(int i =0; i<in.length-3; i+=3){
            in[i] = red[i/3];
            in[i+1] = green[i/3];
            in[i+2] = blue[i/3];
        }
        return in;
    }
}
