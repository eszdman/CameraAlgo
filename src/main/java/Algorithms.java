public class Algorithms {
    Algo FastblurHr = new Algo(new Prog("kernel void sampleKernel(global short *A,global short *B, global short *C, global float *D){\n" +
            "const int x = get_global_id(0);\n" +
            "int temp = 0;\n" +
            "short width = (short)(A[0]);\n" +
            "short Radi = (short)(A[1]);\n" +
            //"int t = x%3;\n" +
            "for(int t = 0; t<3; t++)\n" +
            " {\n" +
            " temp = 0;" +
            " short offset = (Radi-1)/2;" +
            "  for(int w=0; w<Radi; w++) {\n" +
            "\t\t  temp += B[(x+w-offset)*3+t]*D[w];" +
            "\t\t//temp += B[(x-w - width*h)*3+t]*A[k];\n" +
            "  }\n" +
            "  C[(x)*3+t] = (short)(temp);\n" +
            " }\n" +
            "}\n", 4));
    Algo FastblurVr = new Algo(new Prog("kernel void sampleKernel(global short *A,global short *B, global short *C, global float *D){\n" +
            "const int x = get_global_id(0);\n" +
            "int temp = 0;\n" +
            "short width = (short)(A[0]);\n" +
            "short Radi = (short)(A[1]);\n" +
            //"int t = x%3;\n" +
            "for(int t = 0; t<3; t++)\n" +
            " {\n" +
            " temp = 0;" +
            " short offset = (Radi-1)/2;" +
            "  for(int h=0; h<Radi; h++) {\n" +
            "\t\t  temp += B[(x + width*(-h+offset))*3+t]*D[h];" +
            "\t\t//temp += B[(x-w - width*h)*3+t]*A[k];\n" +
            "  }\n" +
            "  C[(x)*3+t] = (short)(temp);\n" +
            " }\n" +
            "}\n", 4));
    Algo FastblurDeltaVr = new Algo(new Prog("kernel void sampleKernel(global short *A,global short *B, global short *C, global float *D, global short *E){\n" +
            "const int x = get_global_id(0);\n" +
            "int temp = 0;\n" +
            "short width = (short)(A[0]);\n" +
            "short Radi = (short)(A[1]);\n" +
            //"int t = x%3;\n" +
            "for(int t = 0; t<3; t++)\n" +
            " {\n" +
            " temp = 0;" +
            " short offset = (Radi-1)/2;" +
            "  for(int h=0; h<Radi; h++) {\n" +
            "\t\t  temp += B[(x + width*(-h+offset))*3+t]*D[h];" +
            "\t\t//temp += B[(x-w - width*h)*3+t]*A[k];\n" +
            "  }\n" +
            "  C[(x)*3+t] = E[x*3 + t] - (short)(temp);\n" +
            " }\n" +
            "}\n", 5));
    Algo Normalize = new Algo(new Prog("kernel void sampleKernel(global const short *A,global short *B){\n" +
            "const int x = get_global_id(0);\n" +
            "B[x] = (short)max(min((short)A[x],(short)16320),(short)0); "+
            "}\n", 2));
}
