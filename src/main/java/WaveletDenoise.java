public class WaveletDenoise {
    public static short[] Run(short[] in,ShotUtils utils, double noiseFreq, double noiseFloat, double NCancell){
        /*Wavelet wavelet = new Wavelet();
        wavelet.Run(in,Width,(noiseFreq-noiseFloat),(noiseFreq+noiseFloat),3);
        short[][] wav = (short[][])wavelet.waves;
        wavelet.source = new short[in.length];
        wav[0] = Ops.Run(wav[0], "*0");
        wav[1] = Ops.Run(wav[1], "*0");
        wav[2] = Ops.Run(wav[2], "*10");
        return (short[])wavelet.Image(); */
        return Ops.Run(in,Ops.Run(Blur.Run(in,utils,1,noiseFreq-noiseFloat),Blur.Run(in,utils,1,noiseFreq+noiseFloat),"*2-2*"),"*"+(2)+"-"+NCancell+"*");
        //return Ops.Run(Ops.Run(Blur.Run(in,Width,1,noiseFreq-noiseFloat),Blur.Run(in,Width,1,noiseFreq+noiseFloat),"*2-2*"),"*70");
    }
}
