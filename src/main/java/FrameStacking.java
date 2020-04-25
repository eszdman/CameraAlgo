import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.awt.*;

import static org.jocl.CL.*;

public class FrameStacking {
    public static short[] Run(short[]in, short[]in2,ShotUtils utils, Point shifting) {
        OpenClUtils Clu = new OpenClUtils(4);
        Clu.programSource = "kernel void sampleKernel(global const short *A,global const short *B, global short *C, global short *D){\n" +
                "const int x = get_global_id(0);\n" +
                "const short shiftx = D[0];" +
                "const short shifty = D[1];" +
                "const short width = D[2];" +
                "const short height = D[3];" +
                "const short line = x/(width*3);" +
                "const short hr = x - line*width*3; " +
                //"if(hr+shiftx*3 >= 0 && line - shifty*width*3 >=0 && hr+shiftx*3 < width*3 && line - shifty < height)" +
                " C[x] = (A[x]+B[x+(shiftx*3)+shifty*width*3])/2;" +
                //"else  C[x] = A[x];\n" +
                "}";
        Clu.InitCl("Stacking");
        Pointer out = Pointer.to(in2);
        Pointer srcA = Pointer.to(in);
        Pointer shift = Pointer.to(new short[]{(short)shifting.x,(short)shifting.y, (short)utils.width, (short)utils.height});
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in2.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in2.length, out, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short * in.length, null, null);
        Clu.memObjects[3] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * 4, shift, null);
        Clu.setArg(4);

        long global_work_size[] = new long[]{in.length};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null, //start
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in2.length * Sizeof.cl_short, out, 0, null, null);
        Clu.ReleaseAll(4);
        return in2;
    }
    public static short[] RunSimple(short[]in, short[]in2) {
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "kernel void sampleKernel(global const short *A,global const short *B, global short *C){\n" +
                "const int x = get_global_id(0);\n" +
                " C[x] = (A[x]+B[x])/2;" +
                "}";
        Clu.InitCl("Stacking");
        Pointer out = Pointer.to(in2);
        Pointer srcA = Pointer.to(in);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in2.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in2.length, out, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short * in.length, null, null);
        Clu.setArg(3);

        long global_work_size[] = new long[]{in.length};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null, //start
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in2.length * Sizeof.cl_short, out, 0, null, null);
        Clu.ReleaseAll(3);
        return in2;
    }
}
