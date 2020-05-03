import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.awt.*;

import static org.jocl.CL.*;

public class EsStackingAlgo {
    Point ConvertCoords(int x,int y){
        Point o1,o2,o3,o4;
        int width = 4056;
        int height = 3000;
        o1 = new Point(2,15);
        o2 = new Point(0,3);
        o3 = new Point(19,0);
        o4 = new Point(25,12);
        Point vec1 = new Point((o2.x-o1.x)-(o4.x-o3.x), (o2.y-o1.y)-(o4.y-o3.y));//x vec
        Point vec2 = new Point((o3.x-o2.x)-(o1.x-o4.x), (o3.y-o2.y)-(o1.y-o4.y));//y vec
        //Point vec3 = new Point((o4.x-o3.x), (o4.y-o3.y));
        //Point vec4 = new Point((o1.x-o4.x), (o1.y-o4.y));
        //Point vec13 = new Point(vec1.x-vec3.x, vec1.y-vec3.y);
        //Point vec24 = new Point(vec2.x-vec4.x, vec2.y-vec4.y);
        Point vec13 = new Point(vec1.x, vec1.y); //vector x differential
        Point vec24 = new Point(vec2.x, vec2.y); //vector y differential
        return new Point((vec1.x+vec13.x*x)*x+(vec2.x+vec24.x*y)*y,(vec1.y+vec13.y*x)*x+(vec2.y+vec24.y*y)*y);
    }
    public static short[] Run(int from, int to, ShotUtils utils, Point points[]){
        short[] output = new short[utils.images[0].length];
        OpenClUtils Clu = new OpenClUtils(3);
        int squaresize = 25;
        short[] pointsval = new short[points.length*2];
        for(int i =0; i<points.length*2; i+=2){
            pointsval[i] = (short)points[i/2].x;
            pointsval[i+1] = (short)points[i/2].y;
        }
        Clu.programSource = "#define cords(x,y)(y*"+utils.width*3+" + x)" +
                "" +
                "" +
                "" +
                "\ndouble CmpSquareFast(int2 cords1, int2 cords2, const short *A, const short *B){" +
                "int sqsize = " + squaresize+";"+
                "int rad = sqsize/2;" +
                "int cnt = 1;" +
                "int x = cords1.x;" +
                "int y = cords1.y;" +
                "for(int l = 0; l<3;l++){" +
                "rad/=2;" +
                "for(int i =0; i<rad*3.14*2; i++){" +
                "int x2 = " +
                "int y2 = " +
                "}" +
                "}" +
                "}" +
                "\nkernel void sampleKernel(global const short *A,global const short *B, global short *C){" +
                "const int x = get_global_id(0);" +
                "const int y = get_global_id(1);" +
                "" +
                "" +
                "" +
                "" +
                "" +
                "" +
                "" +
                "" +
                "" +
                "" +
                "" +
                "" +
                "" +
                "" +
                "}";
        Clu.InitCl("EsStacking");
        Pointer srcA = Pointer.to(utils.images[from]);
        Pointer srcB = Pointer.to(utils.images[to]);
        Pointer out = Pointer.to(output);
        Clu.memObjects[0] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_short * utils.images[from].length, srcA, null);
        Clu.memObjects[1] = clCreateBuffer(Clu.context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_short * utils.images[to].length, srcB, null);
        Clu.memObjects[2] = clCreateBuffer(Clu.context,
                CL_MEM_READ_WRITE, Sizeof.cl_short  * utils.images[from].length, null, null);
        Clu.setArg(3);
        long global_work_size[] = new long[]{utils.width*3, utils.height};
        long local_work_size[] = new long[]{1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);
        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                output.length * Sizeof.cl_short, out, 0, null, null);
        Clu.ReleaseAll(3);
        return output;
    }
}
