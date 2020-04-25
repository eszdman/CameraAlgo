import org.jocl.Pointer;
import org.jocl.Sizeof;
import sun.awt.image.ShortInterleavedRaster;

import static org.jocl.CL.*;

public class MedianFilter {
    static String cancellation ="if((temp2/Radi)/(temp[(Radi-1)/2]+1) >= 0.8 || (temp2/Radi)/(temp[(Radi-1)/2]+1) <= 0.80)";
    public static short[] Hr(short[] in, ShotUtils utils, int power, int Rad) {
        //boolean res = true;
        //if(power == 1) res = false;
        //if(res) in = Resizer.Binning(in, width, power);
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource =
                "#define Radi "+Rad+"\n"+
                "kernel void sampleKernel(global int *A,global short *B, global short *C){\n" +
                        "const int x = get_global_id(0);\n" +
                        "int width = (int)(A[0]);\n" +
                        //"int Radi = (int)(A[1]);\n" +
                        "int temp[Radi];\n" +
                        "for(int t = 0; t<3; t++)\n" +
                        " {\n" +
                        //" int x2 = 0;\n" +
                        " short offset = (Radi-1)/2;" +
                        "double temp2 = 0;"+
                        "\t  for(int w=0; w<Radi; w++){\t\n" +
                        //"\t\t temp += B[(x+w-offset)*3+t]*D[w];" +
                        "\t\t temp[w] = B[((x+w-offset)*3+t)];" +
                        "     temp2+=temp[w];" +
                        // "else temp += B[(x+w-offset)*3+t]*D[w+1-Radi];" +
                        "\t\t//temp += B[(x-w - width*h)*3+t]*A[k];\n" +
                        "  }\n" +
                        cancellation+
                        "for(int b =0; b<Radi; b++) for(int i =0; i<Radi; i++) if(temp[i-1] > temp[i])" +
                        "{" +
                        "temp2 = temp[i];" +
                        " temp[i] = temp[i-1];" +
                        " temp[i-1] = temp2;" +
                        " }" +
                        "  C[(x)*3+t] = (temp[(Radi-1)/2]);\n" +
                        " }\n" +
                        "}\n";
        Clu.InitCl("FastMedianHr");
        int k = 2;
        int[] t = new int[k];
        t[0] = utils.width/power;
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

        long global_work_size[] = new long[]{(in.length+utils.width*Rad + Rad)/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_short, Out, 0, null, null);
        Clu.ReleaseAll(3);
        //if(res) out = Resizer.Upsampling(in, width, power);
        return out;
    }
    public static short[] Vr(short[] in, ShotUtils utils, int power, int Rad) {
       // boolean res = true;
        //if(power == 1) res = false;
        //if(res) in = Resizer.Binning(in, width, power);
        OpenClUtils Clu = new OpenClUtils(3);
        //int Rad = (int)radius;
        //if(radius-Rad != 0) {Rad++;}
        Clu.programSource =
                "#define Radi "+Rad+"\n"+
                "kernel void sampleKernel(global int *A,global short *B, global short *C){\n" +
                        "const int x = get_global_id(0);\n" +
                        "int temp[Radi];\n" +
                        "int width = (int)(A[0]);\n" +
                        //"int Radi = (int)(A[1]);\n" +
                        "for(int t = 0; t<3; t++)\n" +
                        " {\n" +
                        " short offset = (Radi-1)/2;" +
                        "double temp2 = 0;" +
                        "  for(int h=0; h<Radi; h++) {\n" +
                        //"\t\t y2 = h - (Radi-1)/2;\n" +
                        "\t\t temp[h] = B[(x + width*(-h+offset))*3+t];" +
                        "     temp2+=temp[h];" +
                        //"else temp += B[(x + width*(-h+offset))*3+t]*D[h+1-Radi]; \n" +
                        "\t\t//temp += B[(x-w - width*h)*3+t]*A[k];\n" +
                        "  }\n" +
                        cancellation+
                        "for(int b =0; b<Radi; b++) for(int i =0; i<Radi; i++) if(temp[i-1] > temp[i]){" +
                        "temp2 = temp[i];" +
                        " temp[i] = temp[i-1];" +
                        " temp[i-1] = temp2;" +
                        " }" +
                        "  C[(x)*3+t] = (temp[(Radi-1)/2]);\n" +
                        " }\n" +
                        "}\n";
        Clu.InitCl("FastMedianVr");
        int k = 2;
        int[] t = new int[k];
        t[0] = utils.width/power;
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

        long global_work_size[] = new long[]{(in.length+utils.width*Rad + Rad)/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_short, Out, 0, null, null);
        Clu.ReleaseAll(3);
        //if(res) out = Resizer.Upsampling(in, width, power);
        return out;
    }
    public static short[] Run(short[] in, ShotUtils utils, int power, int Rad){
        boolean res = true;
        if(power == 1) res = false;
        if(res) in = Resizer.Binning(in, utils, power);
        short[] out = Vr(Hr(in,utils,power,Rad),utils,power,Rad);
        if(res) out = Resizer.Upsampling(out, utils, power);
        return out;
    }
}
