import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.awt.*;

import static org.jocl.CL.*;

class MyPoint{
    double x;
    double y;
    MyPoint(double x, double y){
        this.x = x;
        this.y = y;
    }
    void mul(double koeff){
        x*=koeff;
        y*=koeff;
    }
    void div(double koeff){
        x/=koeff;
        y/=koeff;
    }
    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + "]";
    }
}
public class EsStackingAlgo2 {
    Point ConvertCoords(int x,int y){
        Point o1,o2,o3,o4;
        int width = 4056;
        int height = 3000;
        o1 = new Point(2,15);
        o2 = new Point(0,3);
        o3 = new Point(19,0);
        o4 = new Point(25,12);
        Point vec1 = new Point((o2.x-o1.x), (o2.y-o1.y));//x vec
        Point vec2 = new Point((o3.x-o2.x), (o3.y-o2.y));//y vec
        //Point vec3 = new Point((o4.x-o3.x), (o4.y-o3.y));
        //Point vec4 = new Point((o1.x-o4.x), (o1.y-o4.y));
        //Point vec13 = new Point(vec1.x-vec3.x, vec1.y-vec3.y);
        //Point vec24 = new Point(vec2.x-vec4.x, vec2.y-vec4.y);
        Point vec13 = new Point((o2.x-o1.x)-(o4.x-o3.x),(o2.y-o1.y)-(o4.y-o3.y)); //vector x differential
        Point vec24 = new Point((o3.x-o2.x)-(o1.x-o4.x), (o3.y-o2.y)-(o1.y-o4.y)); //vector y differential
        return new Point((vec1.x+vec13.x*x)*x+(vec2.x+vec24.x*y)*y,(vec1.y+vec13.y*x)*x+(vec2.y+vec24.y*y)*y);
    }
    public static short[] Run(int from, int to, ShotUtils utils, Point p[]){
        short[] output = new short[utils.images[0].length];
        OpenClUtils Clu = new OpenClUtils(3);
        Point[] p2 = new Point[p.length];
        int width = utils.width;
        /*p2[0] = p[3];
        p2[1] = p[2];
        p2[2] = p[1];
        p2[3] = p[0];
        p = p2;*/
        p[0].setLocation(0,0);//LU
        p[1].setLocation(0,0);//
        p[2].setLocation(0,0);//
        p[3].setLocation(-200,0);//
        //p[3].setLocation(-50,-100);
        //p[1].setLocation(20,0);


        p[1].x+=width;//1
        p[2].y+=utils.height;//2
        p[3].x+=width;//3
        p[3].y+=utils.height;//3
        MyPoint vec1 = new MyPoint(((double)(p[1].x)-p[0].x)/(width),(((double)(p[1].y)-p[0].y))/(width));
        MyPoint vec2 = new MyPoint(((double)(p[2].x)-p[0].x)/utils.height,(((double)(p[2].y)-p[0].y)/utils.height));
        MyPoint vec3 = new MyPoint(((double)(p[3].x)-p[2].x)/(width),((double)(p[3].y)-p[2].y)/(width));
        MyPoint vec4 = new MyPoint(((double)(p[3].x)-p[1].x)/utils.height,((double)(p[3].y)-p[1].y)/utils.height);
        MyPoint vec13 = new MyPoint(vec3.x-vec1.x,vec3.y-vec1.y);
        //vec13.div(utils.height);
        vec13.div(width);
        MyPoint vec24 = new MyPoint(vec2.x-vec4.x,vec2.y-vec4.y);
        vec24.div(width);
        //vec24.div(utils.height);

        System.out.println("v1:"+vec1);
        System.out.println("v2:"+vec2);
        System.out.println("v3:"+vec3);
        System.out.println("v4:"+vec4);
        System.out.println("v13:"+vec13);
        System.out.println("v24:"+vec24);
        Clu.programSource = "#define cords(x,y)(y*"+utils.width*3+" + x*3)" +
                "" +
                "" +
                "" +
                "" +
                "\nint ConvertCoords(int x, int y) {" +
                "float2 vec1 = (float2)("+vec1.x+" , "+vec1.y+");" + //X vector
                "float2 vec2 = (float2)("+vec2.x+" , "+vec2.y+");" + //Y vector
                "float2 vec13 = (float2)("+vec13.x+" , "+vec13.y+");" +
                "float2 vec24 = (float2)("+vec24.x+" , "+vec24.y+");" +
                //"int x2 = (int)( ((vec1.x*x+vec13.x*y*x))  +  (vec2.x*y + vec24.x*y*x));" +
                //"int y2 = (int)( ((vec1.y*x+vec13.y*y*x))  +  (vec2.y*y + vec24.y*y*x));" +
                "float2 vecy = (float2)((vec2.x + vec24.x*x), (vec2.y + vec24.y*x));" +
                "vecy/= native_sqrt(vecy.x*vecy.x + vecy.y+vecy.y);" +
                "vecy*= y;"+
                "int x2 = (int)( ((vec1.x*x))  +  ( (vec2.x + vec24.x*x)*y));" +
                "int y2 = (int)( ((vec1.y*x))  +  ( (vec2.y + vec24.y*x)*y));" +
                "x2+="+p[0].x+";" +
                "y2+="+p[0].y+";" +
                "if(x2>="+width+"){x2 = "+(width-1)+";}"+
                "if(y2>="+utils.height+"){y2 = "+(utils.height-1)+";}" +
                "if(x2<0){x2 = 0;}"+
                "if(y2<0){y2 = 0;}" +
                //"int x2 = (vec1.x)*x;" +
                //"int y2 = (vec2.y)*y;" +
                //"return cords((vec1.x+vec13.x*x)*x + (vec2.x+vec24.x*y)*y , (vec1.y+vec13.y*x)*x +(vec2.y+vec24.y*y)*y);" +

                "return cords(x2,y2);" +
                "}" +
                "" +
                "" +
                "\nkernel void sampleKernel(global const short *A,global const short *B, global short *C){" +
                "const int x = get_global_id(0);" +
                "const int y = get_global_id(1);" +
                //"for(int t =0; t<3;t++) C[cords(x,y)+t] = (B[ConvertCoords(x,y)+t]+A[cords(x,y)+t])/2;" +
                "for(int t =0; t<3;t++) C[cords(x,y)+t] = (B[ConvertCoords(x,y)+t])/1;" +
                "if(x<"+(p[0].x+10)+" && y<"+(p[0].y+10)+") C[cords(x,y)] = 0;" +
                "if(x>"+(p[1].x-10)+" && y<"+(p[1].y+10)+") C[cords(x,y)] = 0;" +
                "if(x<"+(p[2].x+10)+" && y>"+(p[2].y-10)+") C[cords(x,y)] = 0;" +
                "if(x>"+(p[3].x-10)+" && y>"+(p[3].y-10)+") C[cords(x,y)] = 0;" +
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
        Clu.InitCl("EsStacking2");
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
        long global_work_size[] = new long[]{utils.width, utils.height};
        long local_work_size[] = new long[]{1,1};
        clEnqueueNDRangeKernel(Clu.commandQueue, Clu.kernel, 2, null,
                global_work_size, local_work_size, 0, null, null);
        clEnqueueReadBuffer(Clu.commandQueue, Clu.memObjects[2], CL_TRUE, 0,
                output.length * Sizeof.cl_short, out, 0, null, null);
        Clu.ReleaseAll(3);
        return output;
    }
}
