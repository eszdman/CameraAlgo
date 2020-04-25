import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.io.FileNotFoundException;

import static org.jocl.CL.*;

/**
 * Created by eszdman on 25.12.2018.
 */
public class Denoise {
    public static short[] Denoise(short[] in, int width) {
        int num = 3;
        OpenClUtils Opencl = new OpenClUtils(num);
        Opencl.programSource= "kernel void sampleKernel(global const int *A,global const short *B,global short *C){\n" +
                "                const int x = get_global_id(0);\n" +
                "                const int Width = A[0];\n" +
                "                double temp[4];\n" +
                "                short tempwb[4];\n" +
                "                short wb[4];\n" +
                "                double t = 0;\n" +
                "                double b = 0;\n" +
                "                tempwb[0] = 0;\n" +
                "                tempwb[1] = 0;\n" +
                "                tempwb[2] = 0;\n" +
                "                tempwb[3] = 0;\n" +
                "                for(int col = 0; col<3; col++) {\n" +
                "                wb[0] += B[x*3 + col];\n" +
                "                wb[1] += B[(x+1)*3 + col];\n" +
                "                wb[2] += B[(x+Width)*3 + col];\n" +
                "                wb[3] += B[(x+Width+1)*3 + col];\n" +
                "                }\n" +
                "                wb[0] /=3;\n" +
                "                wb[1] /=3;\n" +
                "                wb[2] /=3;\n" +
                "                wb[3] /=3;\n" +
                "                for(int col = 0; col<3; col++){\n" +
                "                temp[0] =  (double)(B[x*3 + col])/(double)(wb[0]+1);\n" +
                "                temp[1] =  (double)(B[(x+1)*3 + col])/(double)(wb[1]+1);\n" +
                "                temp[2] =  (double)(B[(x+Width)*3 + col])/(double)(wb[2]+1);\n" +
                "                temp[3] =  (double)(B[(x+Width+1)*3 + col])/(double)(wb[3]+1);\n" +
                "                tempwb[0] =  wb[0];\n" +
                "                tempwb[1] =  wb[1];\n" +
                "                tempwb[2] =  wb[2];\n" +
                "                tempwb[3] =  wb[3];\n" +
                "                for(int i =0; i<4;i++) for(int ind = 0; ind<3; ind++) if(temp[ind+1]>temp[ind]){b=tempwb[ind+1]; t=temp[ind+1];tempwb[ind+1]=tempwb[ind];tempwb[ind]=b; temp[ind+1]=temp[ind]; temp[ind] = t;}\n" +
                "                C[x*3 + col] = (tempwb[1]*temp[1]+tempwb[2]*temp[2])/2;\n" +
                "                }\n" +
                "                }\n";
        String Avr ="kernel void sampleKernel(global const int *A,global const short *B,global short *C){\n" +
                "const int x = get_global_id(0);\n" +
                "const int Width = A[0];\n" +
                "double temp[4];\n" +
                "for(int col = 0; col<3; col++){\n" +
                "temp[0] =  B[x*3 + col]);\n" +
                "temp[1] =  B[(x+1)*3 + col];\n" +
                "temp[2] =  B[(x+Width)*3 + col];\n" +
                "temp[3] =  B[(x+Width+1)*3 + col];\n" +
                "C[x*3 + col] = (temp[0]+temp[1]+temp[2]+temp[3])/4;\n" +
                "}\n" +
                "}\n";
        String Med ="kernel void sampleKernel(global const int *A,global const short *B,global short *C){\n" +
                "const int x = get_global_id(0);\n" +
                "const int Width = A[0];\n" +
                "double temp[4];\n" +
                "short tempwb[4];" +
                "short wb[4];\n" +
                "double t = 0;" +
                "double b = 0;" +
                "tempwb[0] = 0;\n" +
                "tempwb[1] = 0;\n" +
                "tempwb[2] = 0;\n" +
                "tempwb[3] = 0;\n" +
                "for(int col = 0; col<3; col++) {\n" +
                "wb[0] += B[x*3 + col];\n" +
                "wb[1] += B[(x+1)*3 + col];\n" +
                "wb[2] += B[(x+Width)*3 + col];\n" +
                "wb[3] += B[(x+Width+1)*3 + col];\n" +
                "}\n" +
                "wb[0] /=3;\n" +
                "wb[1] /=3;\n" +
                "wb[2] /=3;\n" +
                "wb[3] /=3;\n" +
                "for(int col = 0; col<3; col++){\n" +
                "temp[0] =  (double)(B[x*3 + col])/(double)(wb[0][0]+1);\n" +
                "temp[1] =  (double)(B[(x+1)*3 + col])/(double)(wb[0][1]+1);\n" +
                "temp[2] =  (double)(B[(x+Width)*3 + col])/(double)(wb[0][2]+1);\n" +
                "temp[3] =  (double)(B[(x+Width+1)*3 + col])/(double)(wb[0][3]+1);\n" +
                "tempwb[0] =  wb[0]\n" +
                "tempwb[1] =  wb[1]\n" +
                "tempwb[2] =  wb[2]\n" +
                "tempwb[3] =  wb[3]\n" +
                "for(int i =0; i<4;i++) for(int ind = 0; ind<3; ind++) if(temp[ind+1]>temp[ind]){b=tempwb[ind+1]; t=temp[ind+1];tempwb[ind+1]=tempwb[ind];tempwb[ind]=b; temp[ind+1]=temp[ind]; temp[ind] = t;}\n" +
                "C[x*3 + col] = (temp[2]*tempwb[2]);\n" +
                "}\n" +
                "}\n";
        String medAvr = "kernel void sampleKernel(global const int *A,global const short *B,global short *C){\n" +
                "const int x = get_global_id(0);\n" +
                "const int Width = A[0];\n" +
                "double temp[4];\n" +
                "short tempwb[4];" +
                "short wb[4];\n" +
                "double t = 0;" +
                "double b = 0;" +
                "tempwb[0] = 0;\n" +
                "tempwb[1] = 0;\n" +
                "tempwb[2] = 0;\n" +
                "tempwb[3] = 0;\n" +
                "for(int col = 0; col<3; col++) {\n" +
                "wb[0] += B[x*3 + col];\n" +
                "wb[1] += B[(x+1)*3 + col];\n" +
                "wb[2] += B[(x+Width)*3 + col];\n" +
                "wb[3] += B[(x+Width+1)*3 + col];\n" +
                "}\n" +
                "wb[0] /=3;\n" +
                "wb[1] /=3;\n" +
                "wb[2] /=3;\n" +
                "wb[3] /=3;\n" +
                "for(int col = 0; col<3; col++){\n" +
                "temp[0] =  (double)(B[x*3 + col])/(double)(wb[0][0]+1);\n" +
                "temp[1] =  (double)(B[(x+1)*3 + col])/(double)(wb[0][1]+1);\n" +
                "temp[2] =  (double)(B[(x+Width)*3 + col])/(double)(wb[0][2]+1);\n" +
                "temp[3] =  (double)(B[(x+Width+1)*3 + col])/(double)(wb[0][3]+1);\n" +
                "tempwb[0] =  wb[0]\n" +
                "tempwb[1] =  wb[1]\n" +
                "tempwb[2] =  wb[2]\n" +
                "tempwb[3] =  wb[3]\n" +
                "for(int i =0; i<4;i++) for(int ind = 0; ind<3; ind++) if(temp[ind+1]>temp[ind]){b=tempwb[ind+1]; t=temp[ind+1];tempwb[ind+1]=tempwb[ind];tempwb[ind]=b; temp[ind+1]=temp[ind]; temp[ind] = t;}\n" +
                "C[x*3 + col] = (temp[1]*tempwb[1]+temp[2]*tempwb[2])/2;\n" +
                "}\n" +
                "}\n";

        Opencl.InitCl("T4");
        short[] output = new short[in.length];
        Pointer gr = Pointer.to(in);
        Pointer out = Pointer.to(output);
        int[] ltt = new int[1];
        ltt[0] = width;
        Pointer lt = Pointer.to(ltt);
        Opencl.memObjects[0] = clCreateBuffer(Opencl.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * ltt.length, lt, null);
        Opencl.memObjects[1] = clCreateBuffer(Opencl.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * in.length, gr, null);
        Opencl.memObjects[2] = clCreateBuffer(Opencl.context,
                CL_MEM_READ_WRITE,
                Sizeof.cl_short * in.length, null, null);
        /*Opencl.memObjects[3] = clCreateBuffer(Opencl.context,
                CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int, lt, null);*/
        Opencl.setArg(num);
        long global_work_size[] = new long[]{(in.length/3)};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Opencl.commandQueue, Opencl.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);
        //double k = 0.02;
        /*for(int t = 0; t<in.length/3 - width-1; t++){
            if(t > width)
            red[t] += (red[t-1 - width]+ red[t-width] + red[t-width + 1] + red[t-1] + red[t] + red[t+1] + red[t-1 + width] + red[t+width] + red[t+1+width]) * k;
        }*/
        clEnqueueReadBuffer(Opencl.commandQueue, Opencl.memObjects[2], CL_TRUE, 0,
                output.length * Sizeof.cl_short, out, 0, null, null);
        Opencl.ReleaseAll(num);
        return output;
    }
}
