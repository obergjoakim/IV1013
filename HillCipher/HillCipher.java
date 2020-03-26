
import java.io.*;
import java.util.*;

/**
 * Task 1
 * 
 * Created by: Joakim Öberg 25/03/2020
 * 
 * HillCipher takes a plainfile which is a text file of integers representing letters, ex A = 0, B = 1,etc
 * and creates a hill cipher, stored in cipherfile
 * 
 * The program can only handle blocksize == 3 and radix == 26
 * 
 * blocksize(n) is the (n*n) size matrix we can handle
 * 
 * Input:
 * radix = args[0], blocksize = args[1], keyfile = args[2], plainfile = args[3], cipherfile = args[4]
 * 
 *   
 */

class HillCipher
{

    private ArrayList<Integer> array_msg; 
    private FileWriter cipherFile; 
 
  
    /**
     * msgToArray takes the message in plainfile and stores it in an Arraylist array_msg
     * 
     * @param plainfile
     * @param radix
     * @throws Exception
     */

    public void msgToArray(String plainfile, int radix) throws Exception
    {
        FileInputStream plainFile = new FileInputStream(plainfile);
        Scanner sc_msg = new Scanner(plainFile);
        array_msg = new ArrayList<Integer>();

            while(sc_msg.hasNextInt())
            {
                int p = 0;
                if(radix < (p = sc_msg.nextInt()))
                {
                    sc_msg.close(); throw new Exception("Cant handle encoded letters bigger than radix = "+ radix);
                }else
                {
                    array_msg.add(p);
                }
                
            }
            sc_msg.close();  
    }

   
    /**
     * encode performes a matrix multiplication between matrix_key and array_msg
     * The result is taken mod radix and written in cipherfile
     * 
     * matrix_key is a double array created from the keyfile
     * 
     * @param keyfile
     * @param cipherfile
     * @param radix
     * @param blocksize
     * @throws Exception
     * @throws FileNotFoundException
     * @throws IOException
     */

    
    public void encode(String keyfile, String cipherfile, int radix, int blocksize) throws Exception, FileNotFoundException, IOException
    {
        try
        {
            cipherFile = new FileWriter(cipherfile);
        }catch(IOException e)
        {
            System.out.println("CipherFile could not be written to!");
        }
        
        FileInputStream keyFile = new FileInputStream(keyfile);
        Scanner sc_key = new Scanner(keyFile); 
        ArrayList<ArrayList<Integer>> matrix_key = new ArrayList<ArrayList<Integer>>();
            
        // for(int i=0;i<blocksize;i++)
        // {
        //     ArrayList<Integer> row_key = new ArrayList<Integer>();
        //     for(int j=0;j<blocksize;j++)
        //     {
        //         row_key.add(sc_key.nextInt());
        //     }
        //     matrix_key.add(row_key);
        // }
        // sc_key.close();
        while(sc_key.hasNextInt())
        {

        
            for(int i=0;i<blocksize;i++)
            {
                ArrayList<Integer> row_key = new ArrayList<Integer>();
                //row_key = new ArrayList<Integer>();
                for(int j=0;j<blocksize ;j++)
                {
                    if(row_key.size()!=blocksize && !sc_key.hasNextInt()){sc_key.close();throw new Exception("Invaid matrix, needs to be size: "+blocksize+" x "+blocksize );}
                    int p = sc_key.nextInt();
                    row_key.add(p);
                   // System.out.println(p);
                }
                matrix_key.add(row_key);
                //System.out.println("row size: "+ row_key.size()+" matrix size: "+matrix_key.size());
            }
        } //System.out.println();

        sc_key.close();

       
        // matrix multiplication and write (result mod radix) to cipherfile
       for(int x=0;x < array_msg.size(); x += blocksize)
       {
        for(int i= 0;i < blocksize;i++)
        {   
            int encoded_msg = 0;
            for(int j =0;j<blocksize;j++)
            {
               encoded_msg += matrix_key.get(i).get(j) * array_msg.get(j+x);
              // System.out.println("matrix_key "+matrix_key.get(i).get(j)+" array_msg " +array_msg.get(j+x)+ " encoded_msg "+encoded_msg );
            }
           // System.out.println(encoded_msg%radix);
         cipherFile.write(String.valueOf(encoded_msg % radix));
         cipherFile.write(' ');
        }
       } 
       cipherFile.close();
    }
     
    
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
               hillCipher.msgToArray(args[3], radix);
           }catch(FileNotFoundException e)
           {
            System.out.println( "File: " + args[3] + " Could not be found!");
           }

           try
           {
               hillCipher.encode(args[2], args[4], radix, blocksize);
           }catch(FileNotFoundException e)
           {
               System.out.println("File: "+args[2] + " Could not be found!");
           }
        }       

    }
}