/*
 * Affine Cipher
 * --------------------------
 * Author: Eng Kia Hui
 * Instruction:
 * 1. Compile file: javac AffineCipher.java
 * 2. Run program with the following parameters:
 *    - a flag to indicate encryption or decryption, a secret key, an input file name, and an output file name.
 * Eg:
 * java AffineCipher -key 3 9 -encrypt -in input.txt -out cipher.txt
 * java AffineCipher -key 3 9 -decrypt -in cipher.txt -out output.txt
 *
 */


import java.io.*;
import java.math.*;
import java.util.Scanner;

public class AffineCipher {

    private static BigInteger aBi;
    private static BigInteger bBi;

	public static void main(String[] args)
	{
        String cTxt = "";
        String pTxt = "";

        int a = Integer.parseInt(args[1]);
        int b = Integer.parseInt(args[2]);
        aBi = new BigInteger(args[1]);
        bBi = new BigInteger(args[2]);

        String inFile = args[5];
        String outFile = args[7];

        System.out.println("Affine Cipher");
        System.out.println("=============\n");

        // mod n, n = 26
        BigInteger nBi = new BigInteger("26");

        // Check whether the key is a valid key
        if (aBi.gcd(nBi).compareTo(BigInteger.ONE) != 0)
        {
            System.out.println("The key (" + aBi + ", " + bBi + ") is not a valid key.");
            System.out.println("a is not coprime with 26.");
        }
        else
        {
            Scanner sc;
            PrintWriter pw;

            if (args[3].equals("-encrypt"))
            {
                System.out.println("Encrypting...");
                System.out.println("-------------");
				System.out.println("\nUsing key (" + a + ", " + b + ")");
                System.out.println("\nPlainText:");

                try {

                    sc = new Scanner(new File(inFile));

                    while (sc.hasNextLine ())
                    {
                        pTxt = sc.nextLine ();
                        cTxt += encryption(pTxt,a,b) + "\n";
                        System.out.println(pTxt);
                    }

                    sc.close ();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                System.out.println("\nCipherText: \n" + cTxt);

                try {
                    pw = new PrintWriter (outFile);
                    pw.print(cTxt);

                    pw.close ();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
            else if (args[3].equals("-decrypt"))
            {
                System.out.println("Decrypting...");
                System.out.println("-------------");
				System.out.println("\nUsing key (" + a + ", " + b + ")");
                System.out.println("\nCipherText:");

                try {

                    sc = new Scanner(new File(inFile));

                    while (sc.hasNextLine ())
                    {
                        cTxt = sc.nextLine ();
                        pTxt += decryption(cTxt, a, b) + "\n";
                        System.out.println(cTxt);
                    }

                    sc.close ();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                System.out.println("\nPlainText: \n" + pTxt);

                try {
                    pw = new PrintWriter (outFile);
                    pw.print(pTxt);

                    pw.close ();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

	}

	// Function to encrypt
	public static String encryption(String msg, int a, int b) {
		String str = "";
		for (char ch: msg.toCharArray())
		{
            // Check if is alphabet
			if (Character.isLetter(ch))
			{
				// c = aM + b mod 26
				if (Character.isUpperCase(ch))
				{
					ch = (char) ((a * (int)(ch - 'A') + b) % 26 + 'A');
				}
				else // if is lowercase
				{
                    // convert to uppercase first then encrypt and convert back to lowercase
					ch = Character.toUpperCase(ch);
					ch = (char) ((a * (int)(ch - 'A') + b) % 26 + 'A');
					ch = Character.toLowerCase(ch);
				}
			}

			str += ch;
		}

		return str;
	}

	// Function to decrypt
	public static String decryption(String msg, int a, int b) {
		String str = "";
		int inverse = 0;

		BigInteger modBi = new BigInteger("26");

        // Get a^-1 mod 26
		BigInteger inverseBi = aBi.modInverse(modBi);
		inverse = inverseBi.intValue();

		for (char ch: msg.toCharArray())
		{
		    // Check if is alphabet
			if (Character.isLetter(ch)) {
				// M = a^-1 * (c - b) mod 26
				if (Character.isUpperCase(ch))
				{
					ch = (char)(inverse * ((int)(ch + 'A') - b) % 26 + 'A');
				}
				else // if is lowercase
				{
                    // convert to uppercase first then decrypt and convert back to lowercase
					ch = Character.toUpperCase(ch);
					ch = (char)(inverse * ((int)(ch + 'A') - b) % 26 + 'A');
					ch = Character.toLowerCase(ch);
				}

			}
			str += ch;
		}

		return str;
	}
}
