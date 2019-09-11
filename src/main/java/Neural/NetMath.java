package Neural;

import org.jocl.Pointer;
import org.jocl.Sizeof;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NetMath {

    public static void PreIndex(Network net) {
        int n = 0;
        for (int i = 1; i < net.laycount; i++)
        {
            for (int b = 0; b < net.lay[i]; b++)
            {
                for (int c = 0; c < net.lay[i - 1]; c++) {
                    net.preIndexGp1[n] = c;
                    net.preIndexGp2[n] = i-1;
                    n++;
                }
            }
        }
    }
    private static double activationF(double in) {
        if (in > 0) {
            in = 1;
        } else {
            in = 0;
        }
        return in;
    }
    private static double ABS(double in){
        return Math.abs(in);
    }
    private static double activationLine(double in) {
        if (in < 0) {
            return 0.0;
        }
        return in;

    }
    private static double guse(double in) {return Math.pow(2.73, -(in*in));}
    private static double activationSoftplus(double in) {
        return Math.log10(1+ Math.pow(Math.E, in));
    }
    private static double PRelu(double in) {
        if(in < 0) {
            return in*(1/7);
        } else return in;
    }
    private static double sigmoid(double in) {
        if (in <= -10) {
            in = -1;
        } else if (in >= 10) {
            in = 1;
        } else {
            double kek = Math.pow(2.73, 2 * in);
            in = (kek - 1) / (kek + 1);
        }
        return in;
    }

    private static double neurone(double in) {
        //if(Math.abs(in) <= 0.001) {
        //    return 0;
        //} else {
        double out = activationSoftplus(in);
        return out; //}
    }
    private static double diff(double in) {
        double t = (1 / (1 + Math.exp(-in)));
        return (t) * (1 - t);
    }
    private static double diff_line(double in) {
        if(in > 0) {
            return 1.0;
        } else return 0.0;
    }
    private static double diffAbs(double in) {if(in > 0) return 1; else return -1;}
    private static double diffSoftplus(double in) {
        return (1/(1+Math.pow(2.73, -in)));
    }
    private static double diff_guse(double in) {
        return -2*in*Math.pow(Math.E, (-in*in));
    }
    private static double diffPrelu(double in) {
        if (in < 0) {
            return (1/7);
        } else {
            return 1;
        }
    }

    public static void PrintError(Network neu) {
        int n = 0;
        double sum = 0;
        for (int i = 1; i < neu.laycount; i++) {
            for (int b = 0; b < neu.lay[i]; b++) {
                for (int c = 0; c < neu.lay[i - 1]; c++) {
                    sum += Math.abs(neu.o[b][i] * diffSoftplus(neu.out[b][i])* diffSoftplus(neu.out[c][i-1])); //* neu.out[c][i-1];//diff(neu.out[c][i - 1]);
                    n++;
                }
            }
        }
        System.out.println(Math.abs(sum/n));
    }

    public static double[] mutation(double[] w1, double[] w2) {

        return w2;
    }

    public static void Weight_Calc(Network neu) {
        int n = 0;
        for (int i = 1; i < neu.laycount; i++) {
            for (int b = 0; b < neu.lay[i]; b++) {
                for (int c = 0; c < neu.lay[i-1]; c++) {
                    //  neu.w[n] =  neu.w[n] + neu.speed *neu.o[b][i] * diffPrelu(neu.out[b][i])* neu.out[c][i-1];
                    neu.w[n] =  neu.w[n] + neu.speed *neu.o[b][i] * diffSoftplus(neu.out[b][i])*diffSoftplus(neu.out[c][i-1]);
                    //   System.out.println(n+" "+ b+""+i+" "+ c+""+(i-1));
                    n++;
                }
            }
        }
    }

    public static void Work(Network neu) {
        double sum = 0;
        int n = 0;
        for (int i = 1; i < neu.laycount; i++)
        {

            for (int b = 0; b < neu.lay[i]; b++)
            {
               for (int c = 0; c < neu.lay[i - 1]; c++) {
                    //System.out.println("+= "+ c + (i-1));
                    //System.out.println("w:"+neu.w[n]+" array[][]"+c+" "+(i-1));
                    sum += neu.out[c][i - 1] * neu.w[n];
                    n++;
                }
               // neu.out[b][i] = neurone(OpenClWorker(ConvertMyArray.NeuroArray(neu.out,neu.lay[i],neu.laycount)));
                neu.out[b][i] = neurone((sum+1));
               sum = 0;
            }
        }
    }
    public static void Backpropogation(Network neu) {
        int a = neu.w.length - 1;
        double sum = 0;
        for (int i = neu.laycount - 1; i >= 0; i--) {
            // if(){ o[0][3] = need - out; }
            for (int b = neu.lay[i]-1; b >= 0; b--) {
                // System.out.println(b+""+ i+" "+num+ " "+prevnum);
                if (i == neu.laycount - 1)
                {
                    neu.o[b][i] = neu.need[b] - neu.out[b][i];
                } else {
                    //System.out.println("Ошибка нейрона "+b+""+i+"="+neu.o[b][i]);
                    for (int c = neu.lay[i+1]-1; c >= 0; c--) {
                        sum += neu.o[c][i+1] * neu.w[a];
                        // System.out.println("Ошибка нейрона "+b+""+i+"="+ o[b][i]);
                        //  System.out.println("            " +(c-1)+"" +(i+1)+" "+a);
                        a--;
                    }
                    neu.o[b][i] = sum;
                    sum = 0;
                }
            }
        }
    }

    public static void Rand_w(Network neu) {
        for (int u = 0; u < neu.w.length; u++) {
            neu.w[u] = Math.random() - 0.4;
            // System.out.println(u);
        }
        for (int t = 0; t < neu.laycount; t++) {
            for (int k = 0; k < neu.max; k++) {
                neu.o[k][t] = 0;
                neu.out[k][t] = 0;
            }
        }
    }
    public static void Clear_net(Network neu) {
        neu.o = new double[neu.max][neu.laycount];
        neu.w = new double[neu.w.length];
        neu.out = new double[neu.max][neu.laycount];
    }

    public static void half_r(Network neu) {
        for (int u = 0; u < neu.w.length; u++) {
            neu.w[u] = 0.5;
        }
        for (int t = 0; t < neu.laycount; t++) {
            for (int k = 0; k < neu.max; k++) {
                neu.o[k][t] = 0;
                neu.out[k][t] = 0;
            }
        }
    }

    public static double randbit() {
        double out;
        if (Math.random() > 0.5) {
            out = 1;
        } else {
            out = 0;
        }
        return out;
    }


}
