import org.jocl.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.jocl.CL.*;

/**
 * Created by eszdman on 21.12.2018.
 */
public class OpenClUtils {
    cl_mem memObjects[] = null;
    public OpenClUtils(int InNumber){
        memObjects = new cl_mem[InNumber];
    }
    cl_context context;
    cl_command_queue commandQueue;
    cl_kernel kernel;
    cl_program program;
    long time;
    String name2;
    String programSource;
     public void ReadProg(String dir, String name){
        try {
            name2 = name;
            programSource = new Scanner(new File(dir,  name+".eszdman")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void InitCl(String name) {
         name2 = name;
         InitCl();
    }
    public void InitCl() {
        //String programSource = null;
        time = System.currentTimeMillis();
        /* try {
            programSource = new Scanner(new File("C:\\Users\\eszdman\\IdeaProjects\\CameraAlgo\\test",  name+".eszdman")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } */
        // Create input- and output data
        // The platform, device type and device number
        // that will be used
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;
        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);
        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];
        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];
        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];
        // Obtain a device ID
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];
        // Create a context for the selected device
        context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);
        // Create a command-queue for the selected device
        commandQueue =
                clCreateCommandQueue(context, device, 0, null);
        // Create the program from the source code
        program = clCreateProgramWithSource(context,
                1, new String[]{ programSource }, null, null);
        // Build the program
        clBuildProgram(program, 0, null, null, null, null);

        // Create the kernel
        kernel = clCreateKernel(program, "sampleKernel", null);
    }
    public void setArg(int num) {
        for(int i = 0; i<num; i++){
            clSetKernelArg(kernel, i,
                    Sizeof.cl_mem, Pointer.to(memObjects[i]));
        }
    }
    public void ReleaseAll(int num){
        for(int i = 0; i<num; i++){
            clReleaseMemObject(memObjects[i]);
        }
        clReleaseKernel(kernel);
        clReleaseProgram(program);
        clReleaseCommandQueue(commandQueue);
        clReleaseContext(context);
        System.out.println("Time elapsed: " + (System.currentTimeMillis()-time)+ "ms " + name2);
    }
}
