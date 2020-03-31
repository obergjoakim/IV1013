class MultInv
{


    public static void main(String[] args) 
    {

        int det = Integer.parseInt(args[0]);
        int radix = Integer.parseInt(args[1]);
        int a=0;
        if(det < 0)
        {
            a = (((det % radix)+radix)%radix);
            System.out.println("negative det: " +det+" gets a="+a);
        }else
        {
            a = (det % radix);
            System.out.println("poitive det: " +det+" gets a="+a);
        }
        
        System.out.println(a);
        //System.out.println(a);
        int multiplicative_invers = 0;
        for(int x = 1; x<radix;x++)
        {
            if(((a*x) % radix) == 1)
            {
             System.out.println("set multiplicative to: "+x);
                multiplicative_invers = x;
                System.out.println("a:"+a+" * x:"+x+" % radix:"+radix+"=="+(a*x)%radix);
                break;
            }
        }System.out.println("We got a multiplicative invers: "+multiplicative_invers);
        
    }
}
