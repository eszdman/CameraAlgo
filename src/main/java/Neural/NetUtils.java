package Neural;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by eszdman on 21.11.2018.
 */
public class NetUtils {
    Network n;
    //int inNum;
    //int hiNum;
    //int outNum;
    //int laycount;
    //double[] w;

    public NetUtils(Network p ) {
        this.n = p;
        //this.inNum = n.inNum;
        //this.hiNum = n.hiNum;
        //this.outNum = n.outNum;
        //this.laycount = n.laycount;
        //this.w = n.w;
    }

    void set_laycount(int laycounts) {
        n.laycount = laycounts;
        n.lay = new int[n.laycount];
    }

    private void set_max() {
        n.max = Math.max(n.inNum, n.hiNum);
        n.max = Math.max(n.outNum, n.max) + 1;
        generateArray(n.max);
    }

    void array() {
        n.inNum = n.lay[0];
        n.outNum = n.lay[n.laycount - 1];
        n.out = new double[n.max][n.laycount];
        n.o = new double[n.max][n.laycount];
        n.need = new double[n.outNum];
        int ms = 0;
        int prev = n.lay[0];
        for (int s = 1; s < n.laycount - 1; s++) {
            ms += n.lay[s] * prev;
            prev = n.lay[s];
        }
        n.lay[n.laycount - 1] = n.outNum;
        n.w = new double[ms + n.lay[n.laycount - 1] * n.lay[n.laycount - 2]];
    }

    private void generateArray(int m) {
        n.out = new double[m][n.laycount];
        n.o = new double[m][n.laycount];
        n.need = new double[n.outNum];
        n.lay[0] = n.inNum;
        int prev = n.lay[0];
        int ms = 0;
        for (int s = 1; s < n.laycount - 1; s++) {
            n.lay[s] = n.hiNum;
            ms += n.lay[s] * prev;
            prev = n.lay[s];
        }
        n.lay[n.laycount - 1] = n.outNum;
        n.w = new double[ms + n.lay[n.laycount - 1] * n.lay[n.laycount - 2]];
    }

    void set_Num(int in, int hi, int out) {
        n.inNum = in;
        n.hiNum = hi;
        n.outNum = out;
        set_max();
    }

    public double[] Output() {
        double[] out1 = new double[n.outNum];
        for (int i = n.outNum - 1; i > 0; i--) {
            out1[i] = n.out[i][n.laycount - 1];
        }
        return out1;
    }

    public void Input(double[] in) {
        for (int a = n.inNum - 1; a > 0; a--) {
            in[a] = n.out[a][0];
            System.out.println(a);
        }
    }

    public void print_w() {
        System.out.println(n.w.length - 1);
        System.out.println(n.laycount);
        System.out.println(n.inNum);
        System.out.println(n.outNum);
        for (int u = n.w.length - 1; u > 0; u--) {
            System.out.println((int) (n.w[u] * Math.pow(2, 14)));
        }
    }

    public void save_w() {
        File f = new File(n.directory, "weights.txt");
        FileWriter wr = null;
        try {
            wr = new FileWriter(f, false);
            wr.write(Integer.toString(n.w.length - 1));
            wr.write(" ");
            wr.write(Integer.toString(n.laycount));
            wr.write(" ");
            wr.write(Integer.toString(n.inNum));
            wr.write(" ");
            wr.write(Integer.toString(n.hiNum));
            wr.write(" ");
            wr.write(Integer.toString(n.outNum));
            wr.write(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int u = n.w.length - 1; u > 0; u--) {
            try {
                wr.write(Integer.toString((int) (n.w[u] * Math.pow(2, 14))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                wr.write(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public void restore_w(boolean fusion) {
       Scanner fScanner = null;
        File file = new File(n.directory, "weights.txt");
       Scanner scanner = null;
       try {
           scanner = new Scanner(file);
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
       int k = scanner.nextInt();
        n.w = new double[k + 1];
        set_laycount(scanner.nextInt());
        n.inNum = scanner.nextInt();
        n.hiNum = scanner.nextInt();
        n.outNum = scanner.nextInt();
        set_Num(n.inNum, n.hiNum, n.outNum);
        if(fusion) {
            File fusionFile = new File(n.directory, "weights2.txt");
            try {
                fScanner = new Scanner(fusionFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //for(int i = 0; i<6; i++)
            fScanner.nextInt();
           fScanner.nextInt();
           fScanner.nextInt();
           fScanner.nextInt();
           fScanner.nextInt();
       }
        for (int l = k; l > 0; l--) {
            if (fusion) {
                n.w[l] += ((scanner.nextInt() / Math.pow(2, 14)) / 2) + (fScanner.nextInt() / Math.pow(2, 14)) / 2;
            } else {
                n.w[l] = (scanner.nextInt() / Math.pow(2, 14));
            }
        }
    }

    public void read_w(boolean fusion){
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        n.w = new double[k + 1];
        n.laycount = scanner.nextInt();
        n.inNum = scanner.nextInt();
        n.hiNum = scanner.nextInt();
        n.outNum = scanner.nextInt();
        for (int l = k; l > 0; l--) {
            if (fusion) {
                n.w[l] += (scanner.nextInt() / Math.pow(2, 14)) / 2;
            } else {
                n.w[l] = (scanner.nextInt() / Math.pow(2, 14));
            }
        }
    }

}
