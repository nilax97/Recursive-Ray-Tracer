

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException
    {
        long start = System.currentTimeMillis();
        viewing Scene = new viewing(args[0]);
        Scene.render(args[1],Integer.valueOf(args[2]));
        long end = System.currentTimeMillis();
        System.out.println("Rendered " + args[0] + " configuration to " + args[1] + " image with supersampling of " + args[2] + " in time " + (end-start) +" msecs");
    }
}