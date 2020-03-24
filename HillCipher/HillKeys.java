import java.io.*;
import java.util.*;

class HillKeys
{
    // java HillKeys <radix x (mod x)> <blocksize(n*n matrix)> <keyfile(output file)>
    public static void main( String[] args) throws (some exeption)
    {
        int radix = Integer.parseInt(args[0]);
        int blocksize = Integer.parseInt(args[1]);
        FileOutputStream out = args[2];
        Random rand = new Random();

        // for the matrix
        // range 0 to x (radix)
        int random_int = rand.nextInt(radix);

    }
}