public class Wavelet {
    Object source = null;
    Object waves = null;
    boolean shorter = false;
    int wavenumber = 0;
    public void Run(int[] in,int width, int minR, int maxR, int wavenum){
        wavenumber = wavenum;
        waves = new int[wavenum][];
        int[][] wav = (int[][])waves;
        double inc = (maxR-minR)/wavenum;
        if(inc < 1) inc = 1;
        for(int k = 0; k<wavenum; k++){
            int Rad = (int)(minR+inc*k);
            int power = 1;
            //if(Rad > 5) {power = 2; Rad /=2;}
            //if(Rad > 15) {power = 3; Rad /=3;}
            source = Blur.Run(in,width,power,Rad);
            wav[k] = Ops.Run(in, (int[])source,"*2-2*");
            in = (int[])source;
        }
        source = in;
        waves = wav;
        //source = Blur.Run(in,width,1,maxR);

    }
    public void Run(short[] in,ShotUtils utils, double minR, double maxR, int wavenum){
        wavenumber = wavenum;
        waves = new int[wavenum][];
        shorter = true;
        waves = new short[wavenum][];
        short[][] wav = (short[][])waves;
        double inc = (maxR-minR)/wavenum;
        source = in;
        int power = 1;
        short quality = 7;
        if(inc < 1) inc = 1;
        for(int k = 0; k<wavenum; k++){
            double Rad = (minR+inc*k);
            Rad /= power;
            //if(Rad > quality) {power +=1;}
            if(Rad > quality) power = 2;
            source = Blur.Run(in,utils,power,Rad);
            wav[k] = Ops.Run(in, (short[])source,"*2-2*");
            in = (short[])source;
        }
        source = in;
        waves = wav;
        //source = Blur.Run(in,width,1,maxR);

    }
    public Object Image(){
        Object output;
        output = source;
        for(int k = 0; k<wavenumber; k++){
            if(shorter) {
                output = Ops.Run(((short[][]) waves)[k], (short[]) output, "*2+2*");
                ((short[][]) waves)[k] = null;
            }
            else {
                output = Ops.Run(((int[][]) waves)[k], (int[]) output, "*2+2*");
                ((int[][]) waves)[k] = null;
            }
        }
        //source = Blur.Run(in,width,1,maxR);
        return output;
    }
    public void Clean(){
        source = null;
        waves = null;
        shorter = false;
    }
}
