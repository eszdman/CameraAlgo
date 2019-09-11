package Neural;

public class Network {
    public int laycount = 4; //predefined value
    int max;
    int[] lay;
    int inNum;
    int hiNum;
    int outNum;
    double w[];
    public double speed;
    double[][] out;
    double[][] o;
    public double[] need;
    int[] preIndexGp1;
    int[] preIndexGp2;
    int fitness;
    public String directory;
}