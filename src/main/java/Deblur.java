import org.jocl.Pointer;
import org.jocl.Sizeof;

import static org.jocl.CL.*;

public class Deblur {
    public static int[] Run(int[] in, int width) {
        int power =1;
        int Rad = 3;
        boolean res = true;
        //if(res) in = Resizer.colorBinning(in, width, power);
        OpenClUtils Clu = new OpenClUtils(3);
        Clu.programSource = "//#define Radi 100\n" +
                "#define setSigma 5\n" +
                "kernel void sampleKernel(global const int *A,global int *B, global int *C){\n" +
                "const int x = get_global_id(0);\n" +
                "double temp = 0;\n" +
                "int sigma = 2*setSigma*setSigma;\n" +
                "\n" +
                "int width = (int)(A[0]);\n" +
                "int Radi = (int)(A[1]);\n" +
                "for(int t = 0; t<3; t++)\n" +
                " {\n" +
                " int x2 = 0;\n" +
                " int y2 = 0;" +
                "temp = 0;\n" +
                " short offset = -  (Radi-1)/2;" +
                "  for(int h=0; h<Radi; h++) {\n" +
                "\t  for(int w=0; w<Radi; w++){\t\n" +
                "\t\t x2 = h - Radi/2;\n" +
                "\t\t y2 = w - Radi/2;\n" +
                "\t\t temp += B[(x+w-offset + width*(h+offset))*3+t]/native_exp(-(x2*x2 + y2*y2)/(Radi*Radi));\n" +
                "\t\t//temp += B[(x-w - width*h)*3+t]*A[k];\n" +
                "  \t  }\n" +
                "  }\n" +
                "  C[(x-offset - width*(offset))*3+t] = temp/(Radi*Radi);\n" +
                " }\n" +
                "}\n";


        /*Clu.programSource = "//#define Radi 16\n" +
                "kernel void sampleKernel(global const int *A,global const double *B, global double *C){\n" +
                "const int x = get_global_id(0);\n" +
                "double temp[10*10];\n" +
                "double temp2 =0;\n" +
                "int width = A[0];\n" +
                "int Radi = A[1];\n" +
                "if(Radi > 10) Radi = 10;\n" +
                "for(int t = 0; t<3; t++) {\n" +
                "  for(int h=0; h<Radi; h++) {\n" +
                "\t  for(int w=0; w<Radi; w++){\n" +
                "\t\t  temp[h*Radi + w] = B[(x+w - width*h)*3 + t];\n" +
                "  \t  }\n" +
                "  }\n" +
                "  for(int i=0; i<Radi; i++) for(int k = 1; k<Radi; k++) if(temp[k] > temp[k-1]) {temp2 = temp[k]; temp[k] = temp[k-1]; temp[k-1] = temp2; }\n" +
                "  C[(x+(Radi/2) - width*(Radi/2))*3 + t] = (int)(temp[Radi-1]);\n" +
                " }\n" +
                "}\n"; */
        Clu.InitCl("C6");
        ConvertImageToByteArray Read = new ConvertImageToByteArray();
        Read.directory = "C:\\Users\\eszdman\\IdeaProjects\\CameraAlgo\\test";
        int k = 2;
        int[] t = new int[k];
        t[0] = width/power;
        t[1] = Rad;
        int[] output = new int[in.length];
        Pointer srcA = Pointer.to(t);
        Pointer In = Pointer.to(in);
        Pointer out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * t.length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * in.length, In, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_int * in.length, null, null);
        Clu.setArg(3);

        long global_work_size[] = new long[]{(in.length+width*50)/3};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                in.length * Sizeof.cl_int, out, 0, null, null);
        Clu.ReleaseAll(3);
        //if(res) in = Resizer.upsampling(in, width, power);
        return output;
    }
}
