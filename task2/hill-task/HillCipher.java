import java.io.*;
import java.util.*;

/**
 * Task 2
 * 
 * Created by: Joakim Öberg 25/03/2020
 * 
 * HillCipher takes a plainfile which is a text file of integers representing letters, ex A = 0, B = 1,etc
 * and a key(from a textfile) in matrix form.
 * The program creates a hill cipher with the keyfile and the plaintext, printed in cipherfile
 * 
 * The program can only handle blocksize <= 8 and radix <= 256
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
    private ArrayList<Integer> row_key;
    private ArrayList<ArrayList<Integer>> matrix_key;
 
  /**
   * msgToArray takes the message in plainfile and stores it in an arraylist
   * 
   * @param plainfile
   * @param radix
   * @throws Exception
   * @throws FileNotFoundException
   */

    public void msgToArray(String plainfile, int radix) throws Exception, FileNotFoundException
    {
        File checkFile = new File(plainfile);
        if(!checkFile.canRead()){throw new Exception("File not readable!");}
        try 
        {
            Scanner sc_msg = new Scanner(new FileInputStream(plainfile));
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
            
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: " +plainfile+" COULD NOD BE FOUND");
        }
      
    }

    /**
     * keyToMatrix read key from keyfile and stores it in an doublearray(Matrix)
     * 
     * 
     * 
     * @param keyfile
     * @param blocksize
     * @throws FileNotFoundException
     * @throws Exception
     */

    public void keyToMatrix(String keyfile, int blocksize) throws FileNotFoundException, Exception
    {
        try {
            Scanner check_keyFile = new Scanner(new FileInputStream(keyfile));
            int numrows = 0;
    
            while(check_keyFile.hasNextLine())
            {
                //System.out.println(numrows);
                numrows++;
                check_keyFile.nextLine();  
            }
            if(numrows != blocksize){throw new Exception("INVALID MATRIX FORMAT: #ROWS NOT EQUAL TO BLOCKSIZE");}
            check_keyFile.close();
    
            Scanner sc_key = new Scanner(new FileInputStream(keyfile));
            matrix_key = new ArrayList<ArrayList<Integer>>();
    
            while(sc_key.hasNextLine())
            {
                row_key = new ArrayList<Integer>();
                String[] tempRow = sc_key.nextLine().split(" ");
                if(tempRow.length!=blocksize){throw new Exception("INVALID MATRIX FORMAT: #COLUMNS NOT EQUAL TO BLOCKSIZE");}
                for(int i=0;i<tempRow.length;i++)
                {
                    row_key.add(Integer.parseInt(tempRow[i]));
                   // System.out.println(Integer.parseInt(tempRow[i]));
                }
                // System.out.println();
                matrix_key.add(row_key);
                //sc_key.nextLine();
            }
            sc_key.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: " +keyfile+" COULD NOD BE FOUND");
        }
    }
        
    

     /**
      * encode performes a matrix multiplication between matrix_key and array_msg
      * The result is taken mod radix and written in cipherfile
      * 
      * matrix_key is a double array created from the keyfile
      * 
      * @param cipherfile
      * @param radix
      * @param blocksize
      * @throws IOException
      */

    
    public void encode(String cipherfile, int radix, int blocksize) throws IOException
    {
        try
        {
        cipherFile = new FileWriter(cipherfile);
         // matrix multiplication and write (result mod radix) to cipherfile
        for(int x=0;x < array_msg.size(); x += blocksize)
        {
            for(int i= 0;i < blocksize;i++)
            {   
                int encoded_msg = 0;
                for(int j =0;j<blocksize;j++)
                {
                encoded_msg += matrix_key.get(i).get(j) * array_msg.get(j+x);
                //System.out.println("matrix_key "+matrix_key.get(i).get(j)+" array_msg " +array_msg.get(j+x)+ " encoded_msg "+encoded_msg );
                }
                //System.out.println(encoded_msg%radix);
            cipherFile.write(String.valueOf(encoded_msg % radix));
            cipherFile.write(' ');
            }
        } 
        cipherFile.close();
        }catch(IOException e)
        {
            System.out.println("ERROR: CipherFile could not be written to!");
        }

       
   
    }
     
    
    public static void main( String[] args) throws Exception 
    {  
        int radix = Integer.parseInt(args[0]);
        int blocksize = Integer.parseInt(args[1]);
        String keyfile = args[2];
        String plainfile = args[3];
        String cipherfile = args[4];

        //Tests whether a file is readable. This method checks that a file exists 
        // and that this Java virtual machine has appropriate privileges that would allow it open the file for reading
        
        if(blocksize > 8 || radix > 256 || args.length != 5)
        {
            throw new Exception("This program ONLY takes input in format <radix <= 256> <blocksize <= 8> <keyFile> <plainFile> <cipherFile>");            
         } 
        else
        {
           HillCipher hillCipher = new HillCipher();
           hillCipher.msgToArray(plainfile, radix);
           hillCipher.keyToMatrix(keyfile, blocksize);
           hillCipher.encode(cipherfile, radix, blocksize);
        }       

    }
}
