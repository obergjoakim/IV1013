import java.io.*;
import java.util.*;

class HillCipher
{

    public ArrayList<Integer> array_msg; // want to use it in two methods
 
   
    public void msgToArray(String plainfile, int blocksize) throws FileNotFoundException
    {
        
        FileInputStream plainFile = new FileInputStream(plainfile);

        //build a quadratic matrix(blocksize(rows)*?) holding the message from plaintext
        Scanner sc_msg = new Scanner(plainFile);
        array_msg = new ArrayList<Integer>();

            
            while(sc_msg.hasNextInt())
            {
            array_msg.add(sc_msg.nextInt());
            }

        sc_msg.close();
        
    }
    //String keyfile(input), String cipherFile(output), int radix, int blocksize)
    public void encode(String keyfile, String cipherfile, int radix, int blocksize) throws FileNotFoundException, IOException
    {
        
        FileInputStream keyFile = new FileInputStream(keyfile);

        // !!!Do I need an enxta try catch OR how to throw the IOException
        FileOutputStream cipherFile = new FileOutputStream(new File(cipherfile));
        
        
        Scanner sc_key = new Scanner(keyFile); // file to be scanned
        // build a (blocksize * blocksize) matrix holding the matrix from keyfile
        ArrayList<ArrayList<Integer>> matrix_key = new ArrayList<ArrayList<Integer>>();
            
        for(int i = 0; sc_key.hasNextLine() && i < blocksize; i++)
        {
            ArrayList<Integer> row_key = new ArrayList<Integer>();
            while(sc_key.hasNextInt())
            {
                row_key.add(sc_key.nextInt());
            }
            matrix_key.add(row_key);
        }

        sc_key.close();

        // do the matrix multiplication between matrix_key (blocksize* blocksize) and matrix_msg(blocksize * )

         
       
       for(int i = 0; i < blocksize;i++)
       {   
           int encoded_msg = 0;
           for(int j =0;j<blocksize;j++)
           {
               encoded_msg += matrix_key.get(i).get(j) * array_msg.get(j);
           }
        cipherFile.write(encoded_msg % radix);
        cipherFile.write(' ');
       }

       

       cipherFile.close();

    }
    
    
    
    
    
    
    
     // java HillKeys <radix x (mod x)> <blocksize(n*n matrix)> <keyfile(input file)> <plainfile> <cipherfile>
    // ta in argumenten direkt i metoderna och kolla här om de är valid
    //  radix = args(0), blocksize = args(1), keyfile = args(2), 
    //  plainfile = args(3), cipherfile = args(4)
    
    public static void main( String[] args) throws Exception 
    {
        
        int radix = Integer.parseInt(args[0]);
        int blocksize = Integer.parseInt(args[1]);
        
        if(blocksize != 3 || radix != 26)
        {
            throw new Exception("blocksize must be 3 AND radix must be 26");            
        }
        else
        {
           HillCipher hillCipher = new HillCipher();

           try
           {
               hillCipher.msgToArray(args[3], blocksize);

           }catch(FileNotFoundException e)
           {
            System.out.println( args[3] + " Is not a valid file");

           }
           try
           {
               hillCipher.encode(args[2], args[4], radix, blocksize);
           }catch(FileNotFoundException e)
           {
               System.out.println(args[2] + " is not a valid file");
           }



           
    



        }
 
        
       

    }
}