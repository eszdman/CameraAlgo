import java.awt.*;

public class ShiftFinder {
    private static double CmpPhoto(short[]in1, short[]in2, Point shift, int width){
        int height = (in1.length/(width*3));
        double diff = 0;
        int ind;
        int ind2;
        int cnt = 1;
        for(int i =0; i<height; i++)
            for(int k =0; k<width*3; k++){
            ind = i*width*3 + k;
            ind2 = ind+(shift.y*width*3) + shift.x*3;
            if(ind2 >= 0 && ind2 < in1.length) {diff += Math.abs(in1[ind]-in2[ind2]);cnt++; }
            }
            return (diff/cnt);
    }
    private static double CmpPhotoFast(short[]in1, short[]in2, Point shift, int width){
        int height = (in1.length/(width*3));
        double diff = 0;
        int ind;
        int ind2;
        int cnt = 1;
        for(int i =0; i<4; i++)
            for(int k =0; k<Math.min(width*3, height); k++){
                ind = 0;
                if(i == 0) ind = k*width*3 + k;
                if(i == 1) ind = (height-k)*width*3 + k;
                if(i == 2) ind = (height/2)*width*3 + k;
                if(i == 3) ind = (k)*width*3 + width*3/2;
                ind2 = ind+(shift.y*width*3) + shift.x*3;
                if(ind2 >= 0 && ind2 < in1.length && ind < in1.length) {diff += Math.abs(in1[ind]-in2[ind2]);cnt++; }
            }
        return (diff/cnt);
    }
    private static double CmpPhotoFaster(short[]in1, short[]in2, Point shift, Point shift2, int width) {
        int height = (in1.length / (width * 3));
        double diff = 0;
        int ind;
        int ind2;
        int cnt = 1;
        int x = ((width * 3) / 2)+shift2.x;
        int y = (height / 2)+shift2.y;
        int rad = Math.min(width * 3, height)/4;
        for (int l = 0; l < 3; l++)
        {
            rad/=2;
            for (int i = 0; i < rad * Math.PI * 2; i++) {
                int x2 = x + (int) (Math.cos(((double) (i)) / rad) * (rad));
                int y2 = y + (int) (Math.sin(((double) (i)) / rad) * rad);
                int ind1 = y2 * width * 3 + x2;
                ind2 = ind1 + (shift.y * width * 3) + shift.x * 3;
                if (ind2 >= 0 && ind2 < in1.length && ind1 < in1.length) {diff += Math.abs(in1[ind1] - in2[ind2]);cnt++;}
            }
    }
        return (diff/(cnt));
    }
public static Point Run(ShotUtils utils, int mul, double shiftx, double shifty){
    Point output = new Point();
    short[][]in = new short[utils.images.length][];
    double diff = Double.MAX_VALUE;
    double temp;
    for(int i = 0; i<2; i++) in[i] = Resizer.Binning(utils.images[i],utils,mul);
    int width = utils.width/mul;
    int height = utils.height/mul;
    Point shift2 = new Point((int)(width*shiftx),(int)(height*shifty));
    Point find = new Point();
    for(find.y = -height/4 ; find.y<height/4; find.y++)
        for(find.x = -width/4; find.x<width/4; find.x++) {
            temp = CmpPhotoFaster(in[0], in[1], find, shift2, width);
            //System.out.println("Cmp Out:"+temp);
            if (temp < diff) {
                diff = temp;
                output = new Point(find.x*mul, find.y*mul);
            }
        }
    return output;
}
public static Point[] GetEISPoints(ShotUtils utils)
{
    Point[] points = new Point[4];
    points[0] = Run(utils,25,0,0);
    points[1] = Run(utils,25,0.35,0);
    points[2] = Run(utils,25,0,0.35);
    points[3] = Run(utils,25,0.35,0.35);
    for(int i =0; i<points.length; i++) System.out.println(points[i]);
    points[0] = Run(utils,points[0],12,25,0,0);
    points[1] = Run(utils,points[1],12,25,0.25,0);
    points[2] = Run(utils,points[2],12,25,0,0.25);
    points[3] = Run(utils,points[3],12,25,0.25,0.25);
    return points;
}
    public static Point Run(ShotUtils utils,Point prev, int mul, int mul2, double shiftx, double shifty){
        Point output = new Point();
        short[][]in = new short[utils.images.length][];
        double diff = Double.MAX_VALUE;
        double temp;
        for(int i = 0; i<2; i++) in[i] = Resizer.Binning(utils.images[i],utils,mul);
        int width = utils.width/mul;
        int height = utils.height/mul;
        Point shift2 = new Point((int)(height*shifty),(int)(width*shiftx));
        Point find = new Point();
        for(find.y = (-height/mul2 + prev.y)/mul; find.y<(height/mul2+ prev.y)/mul; find.y++)
            for(find.x = (-width/mul2+ prev.x)/mul; find.x<(width/mul2 + prev.x)/mul; find.x++) {
                temp = CmpPhotoFaster(in[0], in[1], find,shift2, width);
                if (temp < diff) {
                    diff = temp;
                    output = new Point(find.x*mul, find.y*mul);
                }
            }
        return output;
    }
    public static Point[] Run(ShotUtils utils){
        Point[] output = new Point[utils.detectMatrix[0].length];
        double diff = Double.MAX_VALUE;
        double temp;
        int width = utils.matrixwidth;
        int height = utils.matrixheight;
        Point find = new Point();
        for(int i = 0; i<utils.detectMatrix[0].length; i++) {
            for (find.y = -height/2; find.y < height/2; find.y++)
                for (find.x = -width/2; find.x < width/2; find.x++) {
                    temp = CmpPhoto(utils.detectMatrix[0][i], utils.detectMatrix[1][i], find, width);
                    if (temp < diff) {
                        diff = temp;
                        output[i] = new Point(find.x * utils.matrixmul, find.y * utils.matrixmul);
                    }
                }
                diff = Double.MAX_VALUE;
        }
        return output;
    }
public static void GetDetectMatrix(ShotUtils utils,int edgeperc, int mul, Point prevshift){
short[][][] output = new short[2][4][30*90];
short[][] in = new short[2][];
utils.matrixheight = 30;
utils.matrixwidth = 30;
utils.matrixmul = mul;
int width = utils.width/mul;
int height = utils.height/mul;
int cropx = (width*3/100)*edgeperc;
int cropy = (height/100)*edgeperc;
int pshiftx = prevshift.x/mul;
int pshifty = prevshift.y/mul;
for(int i = 0; i<2; i++) in[i] = Resizer.Binning(utils.images[i],utils,mul);

for(int i =0; i<2; i++)
        for(int h = 0; h<utils.matrixheight; h++)
            for(int w =0; w<utils.matrixwidth*3; w++){
                int xadd = Math.max(pshiftx,0);
                int yadd = Math.max(pshifty,0);
                if(i == 0)
                {
                    xadd = 0;
                    yadd = 0;
                }
                output[i][0][utils.matrixwidth*h + w] = in[i][xadd+cropx+w+(h+yadd+cropy)*width*3];
                output[i][1][utils.matrixwidth*h + w] = in[i][pshiftx+w+cropx+(height-(h+yadd+cropy+1))*width*3];
                xadd = Math.min(pshiftx,width);
                yadd = Math.min(pshifty,height);
                if(i == 0)
                {
                    xadd = 0;
                    yadd = 0;
                }
                output[i][2][utils.matrixwidth*h + w] = in[i][in[0].length - 1 - ((xadd+cropx+w+(h+yadd+cropy)*width*3))];
                output[i][3][utils.matrixwidth*h + w] = in[i][(in[0].length - 1 -(pshiftx+w+cropx+(height-(h+yadd+cropy+1))*width*3))];
            }
            utils.detectMatrix = output;
}
}
