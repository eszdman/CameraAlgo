package Neural;

/**
 * Created by eszdman on 12.12.2018.
 */
public class ConvertMyArray {
    public static byte[][] ArrayXY(byte[] in, int height, int width) {
        byte[][] out = new byte[height][width];
        int n = 0;
        for (int h = 0; h < height; h++) {
            for(int w = 0; w< width; w++){
                out[h][w] = in[n];
                n++;
            }
        }
        return out;
    }
    public static byte[] ArrayX(byte[][] in, int height, int width){
        byte[] out = new byte[height*width];
        int n = 0;
        for(int h = 0; h<height; h++){
            for(int w =0; w<width; w++){
                out[n] = in[h][w];
                n++;
            }
        }
        return out;
    }
    public static double[][] NeuroArrayX(double[] in,int num, int lay) {
       double[][] out = new double[num][lay];
        int n = 0;
        for(int w = 0; w< lay; w++){
        for (int h = 0; h < num; h++) {
                out[h][w] = in[n];
                n++;
            }
        }
        return out;
    }
    public static double[] NeuroArray(double[][] in, int num, int lay){
        double[] out = new double[num*lay];
        int n = 0;
        for(int w =0; w<lay; w++){
        for(int h = 0; h<num; h++){
                out[n] = in[h][w];
                n++;
            }
        }
        return out;
    }
}
