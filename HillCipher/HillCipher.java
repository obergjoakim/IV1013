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
    private ArrayList<Integer> row_key;
    private ArrayList<ArrayList<Integer>> matrix_key;
 
  
    /**
     * msgToArray takes the message in plainfile and stores it in an Arraylist array_msg
     * 
     * @param plainfile
     * @param radix
     * @throws Exception
     */

    public void msgToArray(String plainfile, int radix) throws Exception
    {
        File checkFile = new File(plainfile);
        if(!checkFile.canRead()){throw new Exception("File not readable");}
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
    }

    public void keyToMatrix(String keyfile, int blocksize) throws FileNotFoundException, Exception
    {
       
        Scanner check_keyFile = new Scanner(new FileInputStream(keyfile));
        int numrows = 0;

        while(check_keyFile.hasNextLine())
        {
            //System.out.println(numrows);
            numrows++;
            check_keyFile.nextLine();  
        }
        if(numrows != blocksize){throw new Exception("INVALID MATRIX FORMAT 1");}
        check_keyFile.close();

        Scanner sc_key = new Scanner(new FileInputStream(keyfile));
        matrix_key = new ArrayList<ArrayList<Integer>>();
        

        while(sc_key.hasNextLine())
        {
            row_key = new ArrayList<Integer>();
            String[] tempRow = sc_key.nextLine().split(" ");
            if(tempRow.length!=blocksize){throw new Exception("INVALID MATRIX FORMAT 2");}
            for(int i=0;i<tempRow.length;i++)
            {
                row_key.add(Integer.parseInt(tempRow[i]));
               // System.out.println(Integer.parseInt(tempRow[i]));
            }
            // System.out.println();
            matrix_key.add(row_key);
            //sc_key.nextLine();
        }
    }
        
    //     FileInputStream keyFile = new FileInputStream(keyfile);
    //     Scanner sc_key = new Scanner(keyFile);
    //     matrix_key = new ArrayList<ArrayList<Integer>>(); 
    //     //ArrayList<ArrayList<Integer>> matrix_key = new ArrayList<ArrayList<Integer>>();
   
    //     while(sc_key.hasNextInt())
    //     {
        
    //         for(int i=0;i<blocksize;i++)
    //         {
    //             //ArrayList<Integer> row_key = new ArrayList<Integer>();
    //             row_key = new ArrayList<Integer>();
    //             for(int j=0;j<blocksize ;j++)
    //             {
    //                 // DOESNT WORK!! can have a matrix with 4 rows but not complete
    //                 // Ta ut en line i taget och töm, efter blocksize ska !hasNext
                    
    //                 if(row_key.size()!=blocksize && !sc_key.hasNextInt()){sc_key.close();throw new Exception("INVALID MATRIX FORMAT");}
    //                 int p = sc_key.nextInt();
    //                 row_key.add(p);
    //                 //System.out.println(p);
    //             }
    //             matrix_key.add(row_key);
    //             //System.out.println("row size: "+ row_key.size()+" matrix size: "+matrix_key.size());
    //         }
    //        // System.out.println("matrix: "+matrix_key.size()+"row: "+ row_key.size());
    //        // if(matrix_key.size() * row_key.size()!= blocksize * blocksize){throw new Exception("Invalid matrix");}
    //     } //System.out.println();

    //     sc_key.close();

    //  }

   
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

    
    public void encode(String cipherfile, int radix, int blocksize) throws Exception, FileNotFoundException, IOException
    {
        try
        {
            cipherFile = new FileWriter(cipherfile);
        }catch(IOException e)
        {
            System.out.println("CipherFile could not be written to!");
        }

       
        // matrix multiplication and write (result mod radix) to cipherfile
       for(int x=0;x < array_msg.size(); x += blocksize)
       {
        for(int i= 0;i < blocksize;i++)
        {   
            int encoded_msg = 0;
            for(int j =0;j<blocksize;j++)
            {
               encoded_msg += matrix_key.get(i).get(j) * array_msg.get(j+x);
               System.out.println("matrix_key "+matrix_key.get(i).get(j)+" array_msg " +array_msg.get(j+x)+ " encoded_msg "+encoded_msg );
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

        //Tests whether a file is readable. This method checks that a file exists 
        // and that this Java virtual machine has appropriate privileges that would allow it open the file for reading
        
        if(blocksize != 3 || radix != 26 || args.length != 5)
        {
            throw new Exception("This program ONLY takes input in format <radix == 26> <blocksize == 3> <keyFile> <plainFile> <cipherFile>");            
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
               hillCipher.keyToMatrix(args[2], blocksize);
               hillCipher.encode(args[4], radix, blocksize);
           }catch(FileNotFoundException e)
           {
               System.out.println("File: "+args[2] + " Could not be found!");
           }
           
        }       

    }
}