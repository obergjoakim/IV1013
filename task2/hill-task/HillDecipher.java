
import org.jscience.mathematics.number.*;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;
import org.jscience.mathematics.vector.DimensionException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Task 2
 * 
 * HillDecipher arg0 = radix(max 256), arg1 = blocksize(max 8), arg2 = keyfile,
 * arg3 = plainfile(output), arg4 = cipherfile(input)
 * 
 * Inverts the keyfile matrix and deciphers the cipherfie, prints the decoded
 * message in plainfile
 * 
 */

class HillDecipher {
    private DenseMatrix<Real> keyMatrix;
    private DenseMatrix<Real> msgMatrix;
    private ArrayList<Integer> array_msg;
    int radix;
    int blocksize;
    Real[][] keyMatrix_temp;

    public void keyToMatrix(int radix, int blocksize, String keyfile) throws FileNotFoundException, Exception {

        Scanner check_keyFile = new Scanner(new FileInputStream(keyfile));
        int numrows = 0;
        this.radix = radix;
        this.blocksize = blocksize;

        while (check_keyFile.hasNextLine()) {
            // System.out.println(numrows);
            numrows++;
            check_keyFile.nextLine();
        }
        if (numrows != blocksize) {
            throw new Exception("INVALID MATRIX FORMAT 1");
        }
        check_keyFile.close();

        Scanner sc_key = new Scanner(new FileInputStream(keyfile));
        // matrix_key = new ArrayList<ArrayList<Integer>>();
        keyMatrix_temp = new Real[blocksize][blocksize];
        while (sc_key.hasNextLine()) {
            int columns = 0;
            String[] tempArray = sc_key.nextLine().split(" ");
            if (tempArray.length != blocksize) {
                throw new Exception("INVALID MATRIX FORMAT 2");
            }
            for (int rows = 0; rows < tempArray.length; rows++) {
                keyMatrix_temp[columns][rows] = Real.valueOf((tempArray[rows]));
                System.out.println(Integer.parseInt(tempArray[rows]));
            }
            columns++;
            // System.out.println();
        }
        keyMatrix = DenseMatrix.valueOf(keyMatrix_temp);
       

    }

    public void createMatrix(int radix, int blocksize, String keyfile) throws Exception
    {
        //System.out.println("Create matrix");
        DenseMatrix<Real> keyMatrix;
        Real[][] keyMatrix_temp = new Real[blocksize][blocksize];
        Random random = new Random();
        boolean inverse_exist = false;

        while (!inverse_exist) 
        {
            for (int i = 0; i < blocksize; i++) 
            {   
                //System.out.println('\n');
                for (int j = 0; j < blocksize; j++) 
                {
                    int randomInt = random.nextInt(radix) + 1;
                    keyMatrix_temp[i][j] = Real.valueOf(randomInt);
                    //System.out.println("add " + randomInt+ " to matrix");
                }
            }

            //System.out.println("Matrix created");
            keyMatrix = DenseMatrix.valueOf(keyMatrix_temp);
            // if det(keyMatrix) != 0, then keyMatrix has an inverse
            Real detOfKeyMatrix = keyMatrix.determinant();
            if (detOfKeyMatrix.intValue() != 0) 
            {
                //inverse_exist = true;
                int det = detOfKeyMatrix.intValue();

               

                

                int a = (det % radix);
                //System.out.println(a);
                int multiplicative_invers = 0;
                for(int x = 1; x<radix;x++)
                {
                    if(((a*x) % radix) == 1)
                    {
                     System.out.println("set multiplicative to: "+x);
                        multiplicative_invers = x;
                        inverse_exist = true;
                        System.out.println("a:"+a+" * x:"+x+" % radix:"+radix+"=="+(a*x)%radix);
                        break;
                    }/*else if(x == radix-1)
                    {
                         throw new Exception("Problem with multiplicative invers calculation");
                    }*/
                   // System.out.println(x);
                }
                DenseMatrix<Real> adj = keyMatrix.adjoint();
                Real[][] keyMatrix_temp2 = new Real[blocksize][blocksize];
     
                for(int i=0; i<blocksize; i++)
                {
                    for(int j=0; j<blocksize;j++)
                    {
                         //System.out.println("before modulo "+keyMatrix_temp2[i][j]);
                       //  keyMatrix_temp[i][j] = Rational.valueOf(adj.get(i, j).getDividend().mod(LargeInteger.valueOf(radix)),LargeInteger.ONE);
                       LargeInteger mod = LargeInteger.valueOf(adj.get(i, j).longValue()).mod(LargeInteger.valueOf(radix)); 
                       keyMatrix_temp2[i][j] = Real.valueOf(mod.longValue()*multiplicative_invers);
                      // System.out.println("after modulo "+keyMatrix_temp2[i][j]);
                     
                     
                     //Real adj_elem = adj.get(i, j);
                       // LargeInteger mod = LargeInteger.valueOf(adj.get(i, j).longValue()).mod(LargeInteger.valueOf(radix));
                        
                    // keyMatrix_temp[i][j] = adj_elem.modulos();
                     
                    }
                }
                //System.out.println("Matrix has an inverse, printing to keyFile");

              /*  try 
                {
                    //BufferedWriter keyFile = new BufferedWriter(new FileWriter(keyfile));
                    PrintWriter keyFile = new PrintWriter(keyfile);
                    //System.out.println(" rows: " +keyMatrix.getNumberOfRows()+" columns: "+keyMatrix.getNumberOfColumns()+" blocksize: "+blocksize);
                    for(int i=0;i<blocksize;i++)
                    {   
                        //System.out.println(i);
                        for(int j=0; j<blocksize;j++)
                        {
                            //System.out.println(j);
                            //System.out.println(keyMatrix.get(i, j));
                            keyFile.print(keyMatrix.get(i, j));
                            keyFile.print(' ');
                            if(j == blocksize-1)
                            { 
                                //System.out.println("newline");
                                // we want textfile in matrix form
                                keyFile.print('\n');
                            }
                        }
                    }
                    keyFile.close();
                } catch (IOException e)
                {
                    System.out.println("KeyFile could not be written to!");
                }*/
            }
            
            
            
            
        }
    }      


    // Takes no argument DenseMatrix<Real> matrix is local
    public void invertMatrixNO() throws Exception
    {
        System.out.println("got to invert");
        
        LargeInteger detOfKeyMatrix = LargeInteger.valueOf(keyMatrix.determinant().longValue());
        if (detOfKeyMatrix.intValue() != 0)
        {
            throw new Exception("ERROR Matrix not invertible, you have Wrong keyMatrix!");
        }else
        {
           int det = detOfKeyMatrix.intValue();
           System.out.println(det);

           int a = det % radix;
           int multiplicative_invers = 0;
           for(int x = 1; x<radix;x++)
           {
               if((a*x) % radix == 1)
               {
                System.out.println("set multiplicative");
                   multiplicative_invers = x;
                   break;
               }else if(x == radix-1)
               {
                    throw new Exception("Problem with multiplicative invers calculation");
               }
           }

           DenseMatrix<Real> adj = keyMatrix.adjoint();
           Real[][] keyMatrix_temp = new Real[blocksize][blocksize];

           for(int i=0; i<blocksize; i++)
           {
               for(int j=0; j<blocksize;j++)
               {
                    System.out.println("before modulo "+keyMatrix_temp[i][j]);
                  //  keyMatrix_temp[i][j] = Rational.valueOf(adj.get(i, j).getDividend().mod(LargeInteger.valueOf(radix)),LargeInteger.ONE);
                    System.out.println("after modulo "+keyMatrix_temp[i][j]);
                
                
                //Real adj_elem = adj.get(i, j);
                  // LargeInteger mod = LargeInteger.valueOf(adj.get(i, j).longValue()).mod(LargeInteger.valueOf(radix));
                   
               // keyMatrix_temp[i][j] = adj_elem.modulos();
                
               }
           }
          // LargeInteger modulos = LargeInteger.valeuOf(inverse.key.get(i,j).longValue()).mod(LargeInteger.valueOf(radix));
          
/*
           
           det mod radix

           find ?(multiplicative inverse)
           det * ? = 1 mod radix

           calculate adjugate Matrix
            (take each value mod radix)

            multiplicative inverse * adjugate matrix mod radix -> DONE
*/



        }


    }

    public void msgToArray(String plainfile) throws Exception
    {
        File checkFile = new File(plainfile);
        if(!checkFile.canRead()){throw new Exception("File not readable!");}
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

    public static void main(String[] args) {
        int radix = Integer.parseInt(args[0]);
        int blocksize = Integer.parseInt(args[1]);
        HillDecipher hill = new HillDecipher();

        try {
            hill.keyToMatrix(radix, blocksize, args[2]);
            hill.createMatrix(radix, blocksize, args[2]);
        } catch (FileNotFoundException e) {
            System.out.println(args[2]+" Could not be found!");
            //e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}