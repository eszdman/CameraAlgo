

public class ShotUtils {
    int height;
    int width;
    OpenClUtils Clu;
    Algorithms Algo = new Algorithms();
    public void Init(){
        Algo.FastblurHr = Initialize(Algo.FastblurHr);
        Algo.FastblurVr = Initialize(Algo.FastblurVr);
        Algo.Normalize = Initialize(Algo.Normalize);
    }
    private Algo Initialize(Algo in){
        OpenClUtils Clu = new OpenClUtils(in.inNum);
        Clu.programSource = in.programSource;
        Clu.InitCl("Init");
        in.commandQueue = Clu.commandQueue;
        in.kernel = Clu.kernel;
        in.context = Clu.context;
        return in;
    }
}
