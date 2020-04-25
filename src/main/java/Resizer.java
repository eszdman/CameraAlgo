import org.jocl.Pointer;
import org.jocl.Sizeof;

import static org.jocl.CL.*;

public class Resizer {
    public static double[] Upsampling(double[] in, int width, int power){
        int height = (in.length/3)/(width/power);
        //int nwidth = width;
        int nheight = height*power;
        double[] inter = new double[width*nheight*3];
        int[] set = new int[2];
        set[0] = width;
        set[1] = power;
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "kernel void sampleKernel(global const double *A,global int *B, global double *C){\n" +
                "const int x = get_global_id(0);\n" +
                "int width = B[0];\n" +
                "int bin = B[1];\n" +
                "int line = x/width;\n" +
                "int hr = x - line*width;\n" +
                " for(int t = 0; t<3; t++){\n" +
                "\tfor(int h = 0; h<bin; h++)\n" +
                "\t\tfor(int w =0; w<bin; w++) {\n" +
                "\t\t\t\n" +
                "\t\t\tC[(x - h*width - w)*3 + t] = A[((hr/bin) + (line/bin)*(width/bin))*3 + t];\n" +
                "\t\t\t\n" +
                "\t\t}\n" +
                " }\n" +
                "}\n";
        Clu.InitCl("B2");
        Pointer srcA = Pointer.to(in);
        Pointer srcB = Pointer.to(set);
        Pointer srcC = Pointer.to(inter);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_double * in.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * set.length, srcB, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_double * inter.length, null, null);
        Clu.setArg(3);
        long global_work_size[] = new long[]{(inter.length)/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                inter.length * Sizeof.cl_double, srcC, 0, null, null);
        Clu.ReleaseAll(3);
        return inter;
    }
    public static int[] Upsampling(int[] in, int width, int power){
        int height = (in.length/3)/(width/power);
        int nheight = height*power;
        int[] inter = new int[width*nheight*3];
        int[] set = new int[2];
        set[0] = width;
        set[1] = power;
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "kernel void sampleKernel(global const int *A,global int *B, global int *C){\n" +
                "const int x = get_global_id(0);\n" +
                "int width = B[0];\n" +
                "int bin = B[1];\n" +
                "int line = x/width;\n" +
                "int hr = x - line*width;\n" +
                " for(int t = 0; t<3; t++){\n" +
                "\tfor(int h = 0; h<bin; h++)\n" +
                "\t\tfor(int w =0; w<bin; w++) {\n" +
                "\t\t\t\n" +
                "\t\t\tC[(x - h*width - w)*3 + t] = A[((hr/bin) + (line/bin)*(width/bin))*3 + t];\n" +
                "\t\t\t\n" +
                "\t\t}\n" +
                " }\n" +
                "}\n";
        Clu.InitCl("B2");
        Pointer srcA = Pointer.to(in);
        Pointer srcB = Pointer.to(set);
        Pointer srcC = Pointer.to(inter);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * in.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * set.length, srcB, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_int * inter.length, null, null);
        Clu.setArg(3);
        long global_work_size[] = new long[]{(inter.length)/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                inter.length * Sizeof.cl_int, srcC, 0, null, null);
        Clu.ReleaseAll(3);
        return inter;
    }
    public static short[] Upsampling(short[] in, ShotUtils utils, int power){
        if(power != 1) {
        int height = (in.length/3)/(utils.width/power);
        int nheight = height*power;
        short[] inter = new short[utils.width*nheight*3];
        int[] set = new int[2];
        set[0] = utils.width;
        set[1] = power;
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "kernel void sampleKernel(global const short *A,global int *B, global short *C){\n" +
                "const int x = get_global_id(0);\n" +
                "int width = B[0];\n" +
                "int bin = B[1];\n" +
                "int line = x/width;\n" +
                "int hr = x - line*width;\n" +
                " for(int t = 0; t<3; t++){\n" +
                "\tfor(int h = 0; h<bin; h++)\n" +
                "\t\tfor(int w =0; w<bin; w++) {\n" +
                "\t\t\t\n" +
                "\t\t\tC[(x - h*width - w)*3 + t] = A[((hr/bin) + (line/bin)*(width/bin))*3 + t];\n" +
                "\t\t\t\n" +
                "\t\t}\n" +
                " }\n" +
                "}\n";
        Clu.InitCl("B2");
        Pointer srcA = Pointer.to(in);
        Pointer srcB = Pointer.to(set);
        Pointer srcC = Pointer.to(inter);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * set.length, srcB, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short * inter.length, null, null);
        Clu.setArg(3);
        long global_work_size[] = new long[]{(inter.length)/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                inter.length * Sizeof.cl_short, srcC, 0, null, null);
        Clu.ReleaseAll(3);
        return inter;
           // return inter;
        } else return in;
    }
    public static double[] Binning(double[] in, int width, int power){
        int height = (in.length/3)/width;
        int nwidth = width/power;
        int nheight = height/power;
        int[] set = new int[2];
        double[] binned = new double[nheight*nwidth*3];
        set[0] = nwidth;
        set[1] = power;
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "kernel void sampleKernel(global const double *A,global int *B, global double *C){\n" +
                "const int x = get_global_id(0);\n" +
                "int width = B[0];\n" +
                "int bin = B[1];\n" +
                "int line = x/width;\n" +
                "int hr = x - line*width;\t\n" +
                "double temp = 0;\n" +
                " for(int t = 0; t<3; t++) {\n" +
                "\tfor(int h=0; h<bin; h++){\n" +
                "\t\tfor(int w=0; w<bin; w++){\n" +
                "\t\t\ttemp += A[((line*bin*(width*bin) + hr*bin) - h*width*bin - w)*3 + t];\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\tC[(x)*3 + t] = temp/(bin*bin);\n" +
                "\ttemp = 0;\n" +
                "\t}\n" +
                "}\n";
        Clu.InitCl("B1");
        Pointer srcA = Pointer.to(in);
        Pointer srcB = Pointer.to(set);
        Pointer srcC = Pointer.to(binned);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_double * in.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * set.length, srcB, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_double * binned.length, null, null);
        Clu.setArg(3);
        long global_work_size[] = new long[]{(binned.length)/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                binned.length * Sizeof.cl_double, srcC, 0, null, null);
        Clu.ReleaseAll(3);
        return binned;
    }
    public static int[] Binning(int[] in, int width, int power){
        int height = (in.length/3)/width;
        int nwidth = width/power;
        int nheight = height/power;
        int[] set = new int[2];
        int[] binned = new int[nheight*nwidth*3];
        set[0] = nwidth;
        set[1] = power;
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "kernel void sampleKernel(global const int *A,global int *B, global int *C){\n" +
                "const int x = get_global_id(0);\n" +
                "int width = B[0];\n" +
                "int bin = B[1];\n" +
                "int line = x/width;\n" +
                "int hr = x - line*width;\t\n" +
                "double temp = 0;\n" +
                " for(int t = 0; t<3; t++) {\n" +
                "\tfor(int h=0; h<bin; h++){\n" +
                "\t\tfor(int w=0; w<bin; w++){\n" +
                "\t\t\ttemp += A[((line*bin*(width*bin) + hr*bin) - h*width*bin - w)*3 + t];\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\tC[(x)*3 + t] = temp/(bin*bin);\n" +
                "\ttemp = 0;\n" +
                "\t}\n" +
                "}\n";
        Clu.InitCl("B1");
        Pointer srcA = Pointer.to(in);
        Pointer srcB = Pointer.to(set);
        Pointer srcC = Pointer.to(binned);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * in.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * set.length, srcB, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_int * binned.length, null, null);
        Clu.setArg(3);
        long global_work_size[] = new long[]{(binned.length)/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                binned.length * Sizeof.cl_int, srcC, 0, null, null);
        Clu.ReleaseAll(3);
        return binned;
    }
    public static short[] Binning(short[] in, ShotUtils utils, int power){
        if(power != 1) {
            int height = (in.length / 3) / utils.width;
            int nwidth = utils.width / power;
            int nheight = height / power;
            short[] set = new short[2];
            short[] binned = new short[nheight * nwidth * 3];
            set[0] = (short) nwidth;
            set[1] = (short) power;
            OpenClUtils Clu = new OpenClUtils(3);
            Clu.programSource = "kernel void sampleKernel(global const short *A,global short *B, global short *C){\n" +
                    "const int x = get_global_id(0);\n" +
                    "short width = B[0];\n" +
                    "short bin = B[1];\n" +
                    "short line = x/width;\n" +
                    "int hr = x - line*width;\t\n" +
                    "double temp = 0;\n" +
                    " for(short t = 0; t<3; t++) {\n" +
                    "\tfor(short h=0; h<bin; h++){\n" +
                    "\t\tfor(short w=0; w<bin; w++){\n" +
                    "\t\t\ttemp += A[((line*bin*(width*bin) + hr*bin) - h*width*bin - w)*3 + t];\n" +
                    "\t\t}\n" +
                    "\t}\n" +
                    "\tC[(x)*3 + t] = (short)(temp/(bin*bin));\n" +
                    "\ttemp = 0;\n" +
                    "\t}\n" +
                    "}\n";
            Clu.InitCl("B1");
            Pointer srcA = Pointer.to(in);
            Pointer srcB = Pointer.to(set);
            Pointer srcC = Pointer.to(binned);
            Clu.memObjects[0] = clCreateBuffer(Clu.context,
                    CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_short * in.length, srcA, null);
            Clu.memObjects[1] = clCreateBuffer(Clu.context,
                    CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_short * set.length, srcB, null);
            Clu.memObjects[2] = clCreateBuffer(Clu.context,
                    CL_MEM_READ_WRITE, Sizeof.cl_short * binned.length, null, null);
            Clu.setArg(3);
            long global_work_size[] = new long[]{(binned.length) / 3};
            long local_work_size[] = new long[]{1};
            clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                    global_work_size, local_work_size, 0, null, null);

            clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                    binned.length * Sizeof.cl_short, srcC, 0, null, null);
            Clu.ReleaseAll(3);
            return binned;
        } else return in;
    }
    public int[] BiCubicInterpolation(int[] in, int power){
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.InitCl("B3");



        return in;
    }
}
