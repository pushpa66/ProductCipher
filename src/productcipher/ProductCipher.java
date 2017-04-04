
package productcipher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;

public class ProductCipher {

    private static BufferedReader br = null;
    private static FileReader fr = null;
    private static String FILENAME;
    private static int blockLength = 1;
    private static int numberOfRepeatCycles = 1;
    private static String plainText = "";
    
    public static void main(String[] args) {

        URL url = ProductCipher.class.getClassLoader().getResource("productcipher/data.txt");
        System.out.println(url.getPath());
        ProductCipher.FILENAME = url.getPath();
        
        try {
           
            fr = new FileReader(FILENAME);
            System.out.println("file name");
            br = new BufferedReader(fr);
            String currentLine;

            br = new BufferedReader(new FileReader(FILENAME));
            ProductCipher.blockLength = Integer.parseInt(br.readLine());
            ProductCipher.numberOfRepeatCycles = Integer.parseInt(br.readLine());

             ProductCipher.plainText = br.readLine();
            while ((currentLine = br.readLine()) != null) {
                
                ProductCipher.plainText += " "+currentLine;
            }
            System.out.println("This is the block length specified: "+ ProductCipher.blockLength);
            System.out.println("This is the Plain Text input:");
            System.out.println(ProductCipher.plainText);
        } catch (Exception e) {
            System.out.println("Error "+ e);
        }
        
        String substitutionInput = ProductCipher.plainText;
        int n = ProductCipher.blockLength;
        int x = ProductCipher.numberOfRepeatCycles;

        StringBuffer transpositionOutput = new StringBuffer();
        
        for (int repeat = 1; repeat <= x; repeat++) {
            // Substitution encryption
            StringBuffer substitutionOutput = new StringBuffer();
            for(int i=0 ; i<substitutionInput.length() ; i++) {
                char c = substitutionInput.charAt(i);
                substitutionOutput.append((char) (c+5));
            }
            System.out.println("\nSubstituted text at the "+repeat+" cycle::: "+substitutionOutput);

            // Transposition encryption
            String transpositionInput = substitutionOutput.toString();
            int modulus;
            if((modulus = transpositionInput.length()%n) != 0) {
                modulus = n-modulus;
                // plain text input length must be divisible by blockLength. Therefore we have to add required extra chars
                while(modulus != 0){
                    transpositionInput += "/";
                    modulus--;
                }
            }
            StringBuffer tempTranspositionOutput = new StringBuffer();
            System.out.println("\nTransposition Matrix at the "+repeat+" cycle:::");
            for(int i=0 ; i<n ; i++) {
                for(int j=0 ; j<transpositionInput.length()/n ; j++) {
                    char c = transpositionInput.charAt(i+(j*n));
                    System.out.print(c);
                    tempTranspositionOutput.append(c);
                }
                System.out.println();
            }
            System.out.println("\nFinal encrypted text at the end of "+repeat+" cycle::: "+tempTranspositionOutput);
            
            substitutionInput = tempTranspositionOutput.toString();
            transpositionOutput = tempTranspositionOutput;
        }
        
        for (int repeat = x; repeat > 0; repeat--) {
            // Transposition decryption
            int temp = n;
            n = transpositionOutput.length()/n;
            StringBuffer transpositionPlaintext = new StringBuffer();
            for(int i=0 ; i<n ; i++) {
                for(int j=0 ; j<transpositionOutput.length()/n ; j++) {
                    char c = transpositionOutput.charAt(i+(j*n));
                    transpositionPlaintext.append(c);
                }
            }

            // Substitution decryption
            StringBuffer plaintext = new StringBuffer();
            for(int i=0 ; i<transpositionPlaintext.length() ; i++) {
                char c = transpositionPlaintext.charAt(i);
                plaintext.append((char) (c-5));
            }

            System.out.println("\nPlaintext at the end of "+(x - repeat+1)+ " decryption cycle::: "+plaintext);
            
            n = temp;
            transpositionOutput = plaintext;
        }  
    }
}
