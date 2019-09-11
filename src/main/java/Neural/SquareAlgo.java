package Neural;

/**
 * Created by eszdman on 21.11.2018.
 */
public class SquareAlgo {

    private Square p;
    public SquareAlgo(Square p) {this.p = p;}

    public void CreateCubes() {
        int lay = 0;
        int shiftX = 0;
        int shiftY = 0;
        int cnum = 0;
        int weight = 0;
        p.num = (p.width*3*p.height/(p.size*p.size)) +1;
        byte[][] s = ConvertMyArray.ArrayXY(p.in, p.height, p.width*3);
        p.square = new byte[p.height*3 + p.height*p.width*3][p.size * p.size];
        for(int h = 0; h< p.height; h+=p.size) {//run filler
            for(int w = 0; w<p.width*3; w+=p.size) {

                for(int height = 0; height<p.size; height++){
                    for(int width = 0; width<p.size; width++) {
                       //if(w+width < p.width*3){
                        if(h+height < p.height && w+width < p.width*3)  {
                            if(cnum > 1700){
                              // System.out.println("Debug");
                            }
                            p.square[cnum][width + height * p.size] = p.in[((p.width*3)*(height +h))+w+width];
                        }
                    }
                }
                cnum++;
            }

        }
    }

    public void releaseCubes() {
        int pointerX = 0;
        int pointerY = 0;
        int shiftX = 0;
        int shiftY = 0;
        int n = 0;
           /* while(true){
             //   System.out.println("releasing");
                if(pointerX + n*p.size*p.size + p.width*pointerY > p.out.length) break;
                p.out[pointerX + n*p.size*p.size + p.width*pointerY] = p.square[shiftX+shiftY][pointerX+ pointerY*pointerX];
                pointerX++;

                if(pointerX == p.size) {pointerY++;
                pointerX = 0;
                }
                if(pointerY == p.size)
                {shiftX++; pointerX = 0;
                pointerY = 0;
                n++;
                }
                if(shiftX == p.numY*p.numX) {
                    shiftY++;
                    shiftX = 0;
                    pointerX = 0;
                    pointerY = 0;
                }

        }*/
           int cnum = 0;
        for(int h = 0; h< p.height; h+=p.size) {//run filler
            for(int w = 0; w<p.width*3; w+=p.size) {
                cnum++;
                for(int height = 0; height<p.size; height++){
                    for(int width = 0; width<p.size; width++) {
                        //if(w+width < p.width*3){
                        if(w+width < p.width*3 && h+height < p.height) {
                            p.out[((p.width*3)*(height +h))+w+width] = p.square[cnum][width + height * p.size];
                        }
                    }
                }
            }
        }

    }


}
