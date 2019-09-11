import org.jocl.*;

import static org.jocl.CL.clReleaseMemObject;
import static org.jocl.CL.clSetKernelArg;

public class Algo {
    int inNum;
    String programSource;
    cl_command_queue commandQueue;
    cl_kernel kernel;
    cl_context context;
    String name;
    Long start;
    public Algo(Prog program){
        programSource = program.programm;
        inNum = program.inNum;
    }
    public void setArg(cl_mem[] memObjects) {
        start = System.currentTimeMillis();
        for (int i = 0; i < memObjects.length; i++) {
            clSetKernelArg(kernel, i,
                    Sizeof.cl_mem, Pointer.to(memObjects[i]));
        }
    }
    public void ReleaseAll(cl_mem[] memObjects) {
        System.out.println("Time elapsed: "+(System.currentTimeMillis()-start)+"ms "+ name);
        for (int i = 0; i < memObjects.length; i++) {
            clReleaseMemObject(memObjects[i]);
        }
    }
}
