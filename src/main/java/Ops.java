import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.opencv.core.Point;

import static org.jocl.CL.*;

public class Ops {
    public static int[] Run(int[] in, int[]Mask, String operation) {
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource =
                "kernel void sampleKernel(global const int *A,global const int *B, global int *C){\n" +
                "const int x = get_global_id(0);\n" +
                "C[x] = (A[x]"+operation+"B[x])/2;\n" +
                "}\n";
        Clu.InitCl("Ops");
        int[] output = new int[in.length];
        Pointer In = Pointer.to(in);
        Pointer mask = Pointer.to(Mask);
        Pointer Out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * in.length, In, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
        Sizeof.cl_int * in.length, mask, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_int * in.length, null, null);
        Clu.setArg(3);

        long global_work_size[] = new long[]{in.length};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_int, Out, 0, null, null);
        Clu.ReleaseAll(3);
        return output;
    }
    public static short[] Run(short[] in, short[]Mask, String operation) {
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource =
                "kernel void sampleKernel(global const short *A,global const short *B, global short *C){\n" +
                        "const int x = get_global_id(0);\n";

        int accel = 9;
        for(int i =0; i<accel; i++) Clu.programSource = Clu.programSource + "C[x*"+accel+"+"+i+"] = (A[x*"+accel+"+"+i+"]"+operation+"B[x*"+accel+"+"+i+"])/2;\n";
        Clu.programSource = Clu.programSource + "}\n";
        Clu.InitCl("Ops2");
        short[] output = new short[in.length];
        Pointer In = Pointer.to(in);
        Pointer mask = Pointer.to(Mask);
        Pointer Out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, In, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, mask, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short * in.length, null, null);
        Clu.setArg(3);

        long global_work_size[] = new long[]{in.length/accel};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_short, Out, 0, null, null);
        Clu.ReleaseAll(3);
        return output;
    }
    public static short[] Run(short[] in, short[]Blurred, short[]Activation) {
        OpenClUtils Clu = new OpenClUtils(4);
        Clu.programSource =
                "kernel void sampleKernel(global const short *A,global const short *B, global short *C, global short *D){\n" +
                        "const int x = get_global_id(0);\n";
        int accel = 9;
        for(int i =0; i<accel; i++) Clu.programSource = Clu.programSource + "if(D[x*"+accel+"+"+i+"]==4000) C[x*"+accel+"+"+i+"] = (A[x*"+accel+"+"+i+"]);" +
                "else C[x*"+accel+"+"+i+"] = (B[x*"+accel+"+"+i+"]);\n";
        Clu.programSource = Clu.programSource + "}\n";
        Clu.InitCl("Ops3");
        short[] output = new short[in.length];
        Pointer In = Pointer.to(in);
        Pointer mask = Pointer.to(Blurred);
        Pointer Act = Pointer.to(Activation);
        Pointer Out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, In, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, mask, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short * in.length, null, null);
        Clu.memObjects[3] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, Act, null);
        Clu.setArg(4);

        long global_work_size[] = new long[]{in.length/accel};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_short, Out, 0, null, null);
        Clu.ReleaseAll(4);
        return output;
    }
    public static short[] Run(short[] in, String operation) {
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource =
                "kernel void sampleKernel(global const short *A, global short *C){\n" +
                        "const int x = get_global_id(0);\n";
                        /*"C[x*6] = (A[x*6]"+operation+");\n" +
                        "C[x*6+1] = (A[x*6+1]"+operation+");\n" +
                        "C[x*6+2] = (A[x*6+2]"+operation+");\n" +
                        "C[x*6+3] = (A[x*6+3]"+operation+");\n" +
                        "C[x*6+4] = (A[x*6+4]"+operation+");\n" +
                        "C[x*6+5] = (A[x*6+5]"+operation+");\n" + */
        int accel = 15;
        for(int i =0; i<accel; i++) Clu.programSource = Clu.programSource + "C[x*"+accel+"+"+i+"] = (A[x*"+accel+"+"+i+"]"+operation+");\n";
        Clu.programSource = Clu.programSource + "}\n";
            Clu.InitCl("Ops1");
        short[] output = new short[in.length];
        Pointer In = Pointer.to(in);
        Pointer Out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, In, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short * in.length, null, null);
        Clu.setArg(2);

        long global_work_size[] = new long[]{in.length/accel};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[1], CL_TRUE, 0,
                in.length * Sizeof.cl_short, Out, 0, null, null);
        Clu.ReleaseAll(2);
        return output;
    }

    public static double[] Run(double[] in, double[]Mask, String operation) {
        OpenClUtils Clu = new OpenClUtils(2);
        Clu.programSource =
                "kernel void sampleKernel(global const double *A,global double *B, global double *C){\n" +
                        "const int x = get_global_id(0);\n" +
                        "C[x*3] = (A[x*3]"+operation+"B[x/3]);\n" +
                        "C[x*3 + 1] = (A[x*3 + 1]"+operation+"B[x/3 + 1]);\n" +
                        "C[x*3 + 2] = (A[x*3 + 2]"+operation+"B[x/3 + 2]);\n" +
                        "}\n";
        Clu.InitCl("Ops");
        double[] output = new double[in.length];
        Pointer In = Pointer.to(in);
        Pointer Out = Pointer.to(output);
        Pointer mask = Pointer.to(Mask);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_double * in.length, In, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_double * in.length, mask, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_double * in.length, null, null);
        Clu.setArg(3);

        long global_work_size[] = new long[]{in.length/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_double, Out, 0, null, null);
        Clu.ReleaseAll(2);
        return output;
    }
}
