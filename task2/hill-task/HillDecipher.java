import java.io.*;
import java.util.*;
import org.jscience.mathematics.number.*;
import org.jscience.mathematics.vector.*;

/**
 * Task 2 HillDecipher
 * 
 * Created by: Joakim Öberg 25/03/2020
 * 
 * HillDecipher takes a cipherfile which is a text file of integers each representing a crypted letter, and a keyfile
 * The program inverts the key and decrypts the cipherfile, the decrypted text is written to plainfile
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

class HillDecipher
{

    private ArrayList<Integer> array_crypted_msg; 
    private FileWriter plainFile; 
    private ArrayList<Integer> row_key;
    private ArrayList<ArrayList<Integer>> matrix_key;
    private DenseMatrix<Real> keyMatrixToInvert;
    private ArrayList<ArrayList<Integer>> matrix_key_inverted;
    private  ArrayList<Integer> temprow;
  
 
  /**
   * cipherToArray reads the cipherfile(from a textfile) and stores it in an array
   * 
   * 
   * @param cipherfile
   * @param radix
   * @throws Exception
   * @throws FileNotFoundException
   */

    public void cipherToArray(String cipherfile, int radix) throws Exception, FileNotFoundException
    {
        try {
            File checkFile = new File(cipherfile);
            if(!checkFile.canRead()){throw new Exception("File not readable!");}
            Scanner sc_crypted_msg = new Scanner(new FileInputStream(cipherfile));
            //DenseVector<Real>[] arr
            array_crypted_msg = new ArrayList<Integer>();
    
                while(sc_crypted_msg.hasNextInt())
                {
                    int p = 0;
                    if(radix < (p = sc_crypted_msg.nextInt()))
                    {
                        sc_crypted_msg.close(); throw new Exception("Cant handle encoded letters bigger than radix = "+ radix);
                    }else
                    {
                        array_crypted_msg.add(p);
                    }
                }
                sc_crypted_msg.close();  
        } catch (FileNotFoundException e) {
            System.out.println("ERROR "+cipherfile+" COULD NOT BE FOUND");
        }
    
    }

    /**
     * keyToDecodeMatrix reads the key stored in a textfile and stores it in an double array. 
     * The key is then inverted and stored in matrix_key_inverted, so it can be used to decrypt the cipherfile
     * 
     * @param keyfile
     * @param blocksize
     * @param radix
     * @throws FileNotFoundException
     * @throws Exception
     */

    public void keyToDecodeMatrix(String keyfile, int blocksize, int radix) throws FileNotFoundException, Exception
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
            if(numrows != blocksize){throw new Exception("INVALID MATRIX FORMAT, WRONG nr of rows");}
            check_keyFile.close();
    
            Scanner sc_key = new Scanner(new FileInputStream(keyfile));
            matrix_key = new ArrayList<ArrayList<Integer>>();
            Real[][] toMatrix_temp = new Real[blocksize][blocksize]; 
            int row = 0;
            while(sc_key.hasNextLine())
            {
                row_key = new ArrayList<Integer>();
                String[] tempRow = sc_key.nextLine().split(" ");
                if(tempRow.length!=blocksize){throw new Exception("INVALID MATRIX FORMAT, WRONG length of row");}
                for(int i=0;i<tempRow.length;i++)
                {
                    row_key.add(Integer.parseInt(tempRow[i]));
                    toMatrix_temp[row][i] = Real.valueOf(tempRow[i]);
                   // System.out.println(Integer.parseInt(tempRow[i]));
                }
                // System.out.println();
                matrix_key.add(row_key);
                row++;
                //sc_key.nextLine();
            }
            sc_key.close();
    
            keyMatrixToInvert = DenseMatrix.valueOf(toMatrix_temp);
            DenseMatrix<Real> inverse = keyMatrixToInvert.inverse();
            matrix_key_inverted = new ArrayList<ArrayList<Integer>>();
    
            for(int i=0;i<blocksize;i++)
            {
                temprow = new ArrayList<Integer>();
                for(int j=0; j<blocksize;j++)
                {
                    LargeInteger mod = LargeInteger.valueOf(inverse.get(i,j).longValue()).mod(LargeInteger.valueOf(radix));
                    temprow.add(mod.intValue());
                   // System.out.println("inverted keymatrix mod radix "+mod.intValue()+" "+temprow.get(j));   
                }
               // System.out.println();
                matrix_key_inverted.add(temprow);
            }  
            
        } catch (FileNotFoundException e) {
            System.out.println("ERROR "+keyfile+" COULD NOT BE FOUND");
        }
       
       
       
    }

     /**
      * decodeCipher performes a matrix multiplication between matrix_key_inverted and array_crypted_msg
      * The result is taken mod radix and written to plainfile
      * 
      * 
      * @param plainfile
      * @param radix
      * @param blocksize
      * @throws Exception
      * @throws FileNotFoundException
      * @throws IOException
      */

    
    public void decodeCipher(String plainfile, int radix, int blocksize) throws IOException
    {
        try
        {
            plainFile = new FileWriter(plainfile);
             // matrix multiplication and write (result mod radix) to cipherfile
            // System.out.println("msg size "+array_crypted_msg.size());
            for(int messageblock=0;messageblock < array_crypted_msg.size(); messageblock = messageblock +blocksize)
            {   
               // System.out.println("messageblock "+messageblock);
                for(int rows= 0; rows < blocksize; rows++)
                {   
                    //System.out.println("rows "+rows);
                    int decoded_msg = 0;
                    for(int elem =0; elem<blocksize; elem++)
                    {
                       // System.out.println("elem "+elem);
                        decoded_msg += matrix_key_inverted.get(rows).get(elem) * array_crypted_msg.get(elem+messageblock);
                       // System.out.println("matrix_key_inverted "+matrix_key_inverted.get(rows).get(elem)+" array_crypted_msg " +array_crypted_msg.get(elem+messageblock)+ " encoded_msg "+decoded_msg );
                    }
                   // System.out.println(decoded_msg%radix);
                    plainFile.write(String.valueOf(decoded_msg % radix));
                    plainFile.write(' ');
                
                }
            } 
            plainFile.close();
        }catch(IOException e)
        {
            System.out.println("plainFile could not be written to!");
        }
    }
     
    
    public static void main( String[] args) throws Exception 
    {  
        int radix = Integer.parseInt(args[0]);
        int blocksize = Integer.parseInt(args[1]);
        String key = args[2];
        String output = args[3];
        String cipher = args[4];
        
        if(blocksize > 8 || radix > 256 || args.length != 5)
        {
            throw new Exception("This program ONLY takes input in format <radix =< 256> <blocksize =< 8> <keyFile> <plainFile(output)> <cipherFile(input)>");            
         } 
        else
        {
           HillDecipher hillDecipher = new HillDecipher();
           hillDecipher.cipherToArray(cipher, radix);
           hillDecipher.keyToDecodeMatrix(key, blocksize, radix);
           hillDecipher.decodeCipher(output, radix, blocksize);
           
        }       

    }
}
