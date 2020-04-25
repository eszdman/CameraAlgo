public class byteTo {
    public static short[] start(byte[]in, int bl){
        short[] output = new short[in.length];
        for (int i = 0; i < output.length; i++) {
            output[i] = (short)((in[i] & 0xff)*bl);
        }
        return output;
    }
}
