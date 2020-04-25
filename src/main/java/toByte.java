public class toByte {
    public static byte[] start(short[] in, int bl){
        byte[] output = new byte[in.length];
        for (int i = 0; i < output.length; i++) {
            output[i] = (byte) (Math.max(in[i],0)/bl);
        }
        return output;
    }
    public static byte[] start(int[] in, int bl){
        byte[] output = new byte[in.length];
        for (int i = 0; i < output.length; i++) {
            if(in[i] > 0) output[i] = (byte) (in[i]/bl);
        }
        return output;
    }
    public static byte[] start(double[] in, int bl){
        byte[] output = new byte[in.length];
        for (int i = 0; i < output.length; i++) {
            if(in[i] > 0) output[i] = (byte) (in[i]*bl);
        }
        return output;
    }
    public static byte[] start(float[] in, int bl){
        byte[] output = new byte[in.length];
        for (int i = 0; i < output.length; i++) {
            if(in[i] > 0) output[i] = (byte) (in[i]*bl);
        }
        return output;
    }
    public static byte[] start(boolean[] in, int bl){
        byte[] output = new byte[in.length];
        for (int i = 0; i < output.length; i++) {
            if(in[i]) output[i] = (byte)((int)(bl));
        }
        return output;
    }
}
