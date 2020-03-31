import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseMatrix;


/**
* Task 2 HillKeys
* 
* Creates a matrix K that is invertible, and writes K to keyfile(one matrix row
* per line with blank between each number) Every number in matrix is modulo
* radix
* 
* 
* input arg0 = radix(<=256), arg1 = blocksize(<= 8), keyfile(output)
* 
*/


public class HillKeys 
{

    /**
     * createMatrix creates a blocksize * blocksize matrix with random integers, and writes it to a textfile
     * The matrix must be invertible and has a non-zero multiplicative invers, so it can be used to decrypt messages 
     * 
     * 
     * @param radix
     * @param blocksize
     * @param keyfile
     */
    public void createMatrix(int radix, int blocksize, String keyfile)
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
            int detOfKeyMatrix = keyMatrix.determinant().intValue();

            // java takes reminder with %, need to do different for negativa values
            int a=0;
            if(detOfKeyMatrix < 0)
            {
                a = (((detOfKeyMatrix % radix)+radix)%radix);
                //System.out.println("negative det: " +detOfKeyMatrix+" gets a="+a);
            }else
            {
                a = (detOfKeyMatrix % radix);
                //System.out.println("poitive det: " +detOfKeyMatrix+" gets a="+a);
            }
            
            //System.out.println(a);
            //System.out.println(a);

            // we also need a multiplicative invers for the matrix to exist
            int multiplicative_invers = 0;
            for(int x = 1; x<radix;x++)
            {
                if(((a*x) % radix) == 1)
                {
                    //System.out.println("set multiplicative to: "+x);
                    multiplicative_invers = x;
                    //System.out.println("a:"+a+" * x:"+x+" % radix:"+radix+"=="+(a*x)%radix);
                    break;
                }
            }
            
            if ((detOfKeyMatrix != 0)&&(multiplicative_invers != 0)) 
            {
                inverse_exist = true;
               // System.out.println("We got a multiplicative invers: "+multiplicative_invers);
                //System.out.println("a:"+a+" * x:"+multiplicative_invers+" % radix:"+radix+"=="+(a*multiplicative_invers)%radix);
                //System.out.println("Matrix has an inverse, printing to keyFile");

                try 
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
                }
            }
        }
    }      
        
                  

    public static void main(String[] args)throws Exception
    {
        int radix = Integer.parseInt(args[0]);
        int blocksize = Integer.parseInt(args[1]);
        String outputfile = args[2];
        //System.out.println("HELLO");
        if( args.length != 3)
        {
            throw new Exception("This program takes 3 argument(radix, blocksize, outputfile)");
        }

        HillKeys hill = new HillKeys();
        hill.createMatrix(radix, blocksize, outputfile);
    }



}