import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.awt.*;

import static org.jocl.CL.*;

public class EsShiftFinder {
public static Point[]Run(ShotUtils utils) {
    int size = 128;
    Point[] points = new Point[utils.width*utils.height/(size*size)];
    short[] output = new short[points.length*2];
    OpenClUtils Clu = new OpenClUtils(3);
    Clu.programSource = "#define cords(x0,y0)(y0*"+utils.width*3+" + x0*3)" +
            "\n#define sqloc(x,y)(cords(x*"+size+"+"+size/2+",y*"+size+" + "+size/2+"))" +
            "\n" +
            "" +
            "\nfloat sqShiftFinder(int x, int y, int2 shift,global const short *A, global const short *B){" +
            "int cnt = 0;" +
            "float out = 0;" +
            "int sqlocc = sqloc(x,y);" +
            "int sqsize = "+size+";" +
            "for(int h = 0; h<sqsize; h++)" +
            "for(int w =0; w<sqsize; w++) {" +
            "out += abs(A[sqlocc+cords(h,w)]-B[sqlocc+cords(h+shift.y,w+shift.x)]);" +
            "}" +
            "return out/sqsize;" +
            "}" +
            "\nkernel void sampleKernel(global const short *A,global const short *B, global short *C){" +
            "const int x = get_global_id(0);" +
            "const int y = get_global_id(1);" +
            "float cmp = 0;" +
            "float mincmp = sqShiftFinder(x,y,(int2)(0,0),A,B);" +
            "int2 out = (int2)(0,0);" +
            "for(int h = 0; h<"+size+";h++)" +
            "for(int w = 0; w<"+size+";w++) {" +
            "cmp = sqShiftFinder(x,y,(int2)(h,w),A,B);" +
            "if(cmp<mincmp) {" +
            "mincmp = cmp;" +
            "out = (int2)(h,w);" +
            "}" +
            "C[y+x*2] = out.x;" +
            "C[y+x*2+1] = out.y;" +
            "}" +
            "" +
            "}" +
            "";
    Clu.InitCl("EsStab");
    Pointer srcA = Pointer.to(utils.images[0]);
    Pointer srcB = Pointer.to(utils.images[1]);
    Pointer out = Pointer.to(output);
    Clu.memObjects[0] = clCreateBuffer(Clu.context,
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_short * utils.images[0].length, srcA, null);
    Clu.memObjects[1] = clCreateBuffer(Clu.context,
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_short * utils.images[1].length, srcB, null);
    Clu.memObjects[2] = clCreateBuffer(Clu.context,
            CL_MEM_READ_WRITE, Sizeof.cl_short  * utils.images[0].length, null, null);
    Clu.setArg(3);
    long global_work_size[] = new long[]{utils.width/size - 1, utils.height/size - 1};
    long local_work_size[] = new long[]{1,1};
    clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 2, null,
            global_work_size, local_work_size, 0, null, null);
    clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
            output.length * Sizeof.cl_short, out, 0, null, null);
    Clu.ReleaseAll(3);
    for(int i =0; i<points.length;i++){
        points[i] = new Point(output[i*2],output[i*2 + 1]);
    }
    return points;
}
}