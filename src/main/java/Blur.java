import org.jocl.Pointer;
import org.jocl.Sizeof;

import static org.jocl.CL.*;

public class Blur {
    public static short[] Run(short[] in, ShotUtils utils, int power, double Rad) {
       /* boolean res = true;
        if(power == 1) res = false;
        if(res) in = Resizer.Binning(in, width, power);
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = //"#define Radi "+Rad +"\n" +
                //"#define width "+width +"\n" +
                "#define setSigma 5\n" +
                "kernel void sampleKernel(global int *A,global short *B, global short *C){\n" +
                "const int x = get_global_id(0);\n" +
                "int temp = 0;\n" +
                "int width = (int)(A[0]);\n" +
                "int Radi = (int)(A[1]);\n" +
                "for(int t = 0; t<3; t++)\n" +
                " {\n" +
                " int x2 = 0;\n" +
                " int y2 = 0;\n" +
                " temp = 0;" +
                " short offset = (Radi-1)/2;" +
                "  for(int h=0; h<Radi; h++) {\n" +
                "\t  for(int w=0; w<Radi; w++){\t\n" +
                "\t\t x2 = h - (Radi-1)/2;\n" +
                "\t\t y2 = w - (Radi-1)/2;\n" +
                "\t\t temp += B[(x+w-offset + width*(-h+offset))*3+t]*native_exp(-((x2*x2 + y2*y2)/(Radi*Radi)));\n" +
                "\t\t//temp += B[(x-w - width*h)*3+t]*A[k];\n" +
                "  \t  }\n" +
                "  }\n" +
                "  C[(x - width*(0))*3+t] = (short)(temp/(Radi*Radi));\n" +
                " }\n" +
                "}\n";
        Clu.InitCl("Blur");
        int k = 2;
        int[] t = new int[k];
        t[0] = width/power;
        t[1] = Rad;
        short[] out = new short[in.length];
        Pointer srcA = Pointer.to(t);
        Pointer In = Pointer.to(in);
        Pointer Out = Pointer.to(out);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * t.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, In, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short * in.length, null, null);
        Clu.setArg(3);

        long global_work_size[] = new long[]{(in.length+width*Rad)/3 + Rad};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_short, Out, 0, null, null);
        Clu.ReleaseAll(3);
        if(res) out = Resizer.Upsampling(in, width, power);
        return out; */
       return FastBlur.Run(in,utils,power,Rad);
    }
    public static short[] Run(short[] in, short[] Activation, ShotUtils utils, int power, double radius){
        short[] blurred = FastBlur.Run(in,utils,power,radius);
        //short[] blurred = new short[in.length];
        return Ops.Run(in,blurred,Activation);
    }
    public static short[] Run(short[] in, short[] Blur, short[] Activation){
        //short[] blurred = new short[in.length];
        return Ops.Run(in,Blur,Activation);
    }
    public static int[] Run(int[] in, int width, int power, int Rad) {
        boolean res = true;
        if(power == 1) res = false;
        if(res) in = Resizer.Binning(in, width, power);
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = //"#define Radi "+Rad +"\n" +
                //"#define width "+width +"\n" +
                "#define setSigma 5\n" +
                        "kernel void sampleKernel(global int *A,global int *B, global int *C){\n" +
                        "const int x = get_global_id(0);\n" +
                        "int temp = 0;\n" +
                        "int width = (int)(A[0]);\n" +
                        "int Radi = (int)(A[1]);\n" +
                        "for(int t = 0; t<3; t++)\n" +
                        " {\n" +
                        " int x2 = 0;\n" +
                        " int y2 = 0;\n" +
                        " temp = 0;" +
                        " short offset = (Radi-1)/2;" +
                        "  for(int h=0; h<Radi; h++) {\n" +
                        "\t  for(int w=0; w<Radi; w++){\t\n" +
                        "\t\t x2 = h - (Radi-1)/2;\n" +
                        "\t\t y2 = w - (Radi-1)/2;\n" +
                        "\t\t temp += B[(x+w-offset + width*(-h+offset))*3+t]*native_exp(-((x2*x2 + y2*y2)/(Radi*Radi)));\n" +
                        "\t\t//temp += B[(x-w - width*h)*3+t]*A[k];\n" +
                        "  \t  }\n" +
                        "  }\n" +
                        "  C[(x - width*(0))*3+t] = (temp/(Radi*Radi));\n" +
                        " }\n" +
                        "}\n";
        Clu.InitCl("Blur");
        int k = 2;
        int[] t = new int[k];
        t[0] = width/power;
        t[1] = Rad;
        int[] out = new int[in.length];
        Pointer srcA = Pointer.to(t);
        Pointer In = Pointer.to(in);
        Pointer Out = Pointer.to(out);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * t.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * in.length, In, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_int * in.length, null, null);
        Clu.setArg(3);

        long global_work_size[] = new long[]{(in.length+width*Rad)/3 + Rad};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_int, Out, 0, null, null);
        Clu.ReleaseAll(3);
        if(res) out = Resizer.Upsampling(in, width, power);
        return out;
    }
    public static int[] Deblur(int[] in, int width, int power, int Rad) {
        boolean res = true;
        if(power == 1) res = false;
        if(res) in = Resizer.Binning(in, width, power);
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = //"#define Radi "+Rad +"\n" +
                //"#define width "+width +"\n" +
                "#define setSigma 5\n" +
                        "kernel void sampleKernel(global int *A,global int *B, global int *C){\n" +
                        "const int x = get_global_id(0);\n" +
                        "int temp = 0;\n" +
                        "int width = (int)(A[0]);\n" +
                        "int Radi = (int)(A[1]);\n" +
                        "for(int t = 0; t<3; t++)\n" +
                        " {\n" +
                        " int x2 = 0;\n" +
                        " int y2 = 0;\n" +
                        " temp = 0;" +
                        " short offset = (Radi-1)/2;" +
                        "  for(int h=0; h<Radi; h++) {\n" +
                        "\t  for(int w=0; w<Radi; w++){\t\n" +
                        "\t\t x2 = h - Radi/2;\n" +
                        "\t\t y2 = w - Radi/2;\n" +
                        "\t\t temp += B[(x+w-offset + width*(-h+offset))*3+t]-native_exp(-(x2*x2 + y2*y2)/(Radi*Radi))*1650;\n" +
                        "\t\t//temp += B[(x-w - width*h)*3+t]*A[k];\n" +
                        "  \t  }\n" +
                        "  }\n" +
                        "  C[(x - width*(0))*3+t] = temp/(Radi*Radi);\n" +
                        " }\n" +
                        "}\n";
        Clu.InitCl("Blur");
        int k = 2;
        int[] t = new int[k];
        t[0] = width/power;
        t[1] = Rad;
        int[] out = new int[in.length];
        Pointer srcA = Pointer.to(t);
        Pointer In = Pointer.to(in);
        Pointer Out = Pointer.to(out);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * t.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * in.length, In, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_int * in.length, null, null);
        Clu.setArg(3);

        long global_work_size[] = new long[]{(in.length+width*Rad)/3 + Rad};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_int, Out, 0, null, null);
        Clu.ReleaseAll(3);
        if(res) out = Resizer.Upsampling(in, width, power);
        return out;
    }
    public static double[] Run(double[] in, int width, int power, int Rad) {
        boolean res = true;
        if(power == 1) res = false;
        if(res) in = Resizer.Binning(in, width, power);
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "//#define Radi 100\n" +
                "#define setSigma 5\n" +
                "kernel void sampleKernel(global const int *A,global double *B, global double *C){\n" +
                "const int x = get_global_id(0);\n" +
                "double temp = 0;\n" +
                "int width = (int)(A[0]);\n" +
                "int Radi = (int)(A[1]);\n" +
                "for(int t = 0; t<3; t++)\n" +
                " {\n" +
                " int x2 = 0;\n" +
                " int y2 = 0;\n" +
                " temp = 0;" +
                " short offset = (Radi-1)/2;" +
                "  for(int h=0; h<Radi; h++) {\n" +
                "\t  for(int w=0; w<Radi; w++){\t\n" +
                "\t\t x2 = h - Radi/2;\n" +
                "\t\t y2 = w - Radi/2;\n" +
                "\t\t temp += B[(x+w-offset + width*(-h+offset))*3+t]*native_exp(-(x2*x2 + y2*y2)/(Radi*Radi))+1;\n" +
                "\t\t//temp += B[(x-w - width*h)*3+t]*A[k];\n" +
                "  \t  }\n" +
                "  }\n" +
                "  C[(x - width*(0))*3+t] = temp/(Radi*Radi) - 1;\n" +
                " }\n" +
                "}\n";
        Clu.InitCl("Blur");
        int k = 2;
        int[] t = new int[k];
        t[0] = width/power;
        t[1] = Rad;
        //double[] output = new double[in.length];
        Pointer srcA = Pointer.to(t);
        Pointer In = Pointer.to(in);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * t.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_double * in.length, In, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_double * in.length, null, null);
        Clu.setArg(3);

        long global_work_size[] = new long[]{(in.length+width*Rad)/3 + Rad};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_double, In, 0, null, null);
        Clu.ReleaseAll(3);
        if(res) in = Resizer.Upsampling(in, width, power);
        return in;
    }
}
