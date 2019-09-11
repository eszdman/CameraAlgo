package Neural;

public class UseNetwork {
    public static int[] GetNR(int[] inn, int height, int width){
        byte[] in = new byte[inn.length];
        for(int i = 0; i<inn.length; i++) in[i] = (byte)inn[i];
        int[] output = new int[in.length];
        SqMixer Mixer = new SqMixer();
        LiveNet net = new LiveNet();
        net.Network.directory = "C:\\Users\\eszdman\\IdeaProjects\\CameraAlgo\\test";
        net.NetUtils.restore_w(false);
        Mixer.Use.height = height;
        Mixer.Use.width = width;
        Mixer.Use.in = in;
        Mixer.Use.out = in;
        Mixer.Use.size = (int)Math.sqrt((net.Network.inNum-1)*3);
        Mixer.Algo.CreateCubes();
        double compenBr = 1.5;
        byte[][] Input = Mixer.Use.square;
        double nt = 0;
        for(int i = 0; i<Mixer.Use.num; i++){
                for (int k = 0; k < (Mixer.Use.size * Mixer.Use.size); k += 3) {
                    net.Network.out[k/3][0] = (double) (Input[i][k] & 0xff)*compenBr / 255;
                }
                NetMath.Work(net.Network);
                for (int k = 0; k< (Mixer.Use.size * Mixer.Use.size); k += 3) {
                    nt = (net.Network.out[k/3][net.Network.laycount - 1]*compenBr * 255);
                    if(nt>255) nt = 255;
                    for(int col = 0; col<3; col++) Mixer.Use.square[i][k+col] = (byte) ((byte) nt & 0xff);
                }
        }
        Mixer.Algo.releaseCubes();
        in = Mixer.Use.out;
        for(int i =0; i<in.length; i++) output[i] = (int)in[i];
        return output;
    }
}
