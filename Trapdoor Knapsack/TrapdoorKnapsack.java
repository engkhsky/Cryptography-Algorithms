/*
 * Trapdoor Knapsack
 * --------------------------
 * Author: Eng Kia Hui
 *
 */

import java.math.*;
import java.util.*;

public class TrapdoorKnapsack {
	private static BigInteger one = new BigInteger ("1");
	private static int padding = 0;

	public static void main (String [] args)
	{
		ArrayList<Integer> totals = new ArrayList<Integer>();

		System.out.println("\nTrapdoor Knapsack Encryption Scheme");
		System.out.println("===================================\n");
		int n = readInt ("Enter size of super-increasing knapsack: ");

		int[] privateKeys = new int[n];
		int[] publicKeys = new int[n];

		// privateKeys (2, 5. 9. 21. 45, 103, 215, 450)
		inputPrivateKeys(n, privateKeys);
		int m = inputModulus(privateKeys); // modulus = 851
		int w = inputMultiplier(m); // multiplier = 199

		// w * w^-1 = 1 mod m
		int wInverse = getMulitplierInverse(w, m);
		// Generate public keys b[i] = a[i] * w mod m
		generatePublicKeys(w, m, privateKeys, publicKeys);
		System.out.println("\n--------------Setup--------------");
		System.out.println("Size n: " + n);
		System.out.print("Private Keys a: ");
		printArray(privateKeys);
		System.out.println("Modulus m: " + m);
		System.out.println("Multiplier w: " + w);
		System.out.println("Inverse of Multiplier w^-1: " + wInverse);
		System.out.print("Public Keys b: ");
		printArray(publicKeys);

		// Menu
		int option;
		do {
			option = menu();

			if (option == 1)
			{
				System.out.println("\nKnapsack Encryption");
				System.out.println("-------------------");
				padding = 0;
				String msg = readString ("Enter message (in plaintext): ");
				ksEncryption(msg, publicKeys, totals, n);

				System.out.println("\nSize of Cipher Text: " + totals.size());
				System.out.println("Cipher Text: " + totals);

			}
			else if (option == 2)
			{
				System.out.println("\nKnapsack Decryption");
				System.out.println("-------------------");
				// num = 2
				int num = readInt("Enter number of totals (Size of CipherText): ");
				totals.clear();
				inputCipherTotals(num, totals);
				String msg = ksDecryption(privateKeys, totals, wInverse, m);
				System.out.println("\nUser Input cipher text set: " + totals);
				System.out.println("\nPlain text: " + msg);
			}
			else if (option == 3)
			{
				System.out.println("Quiting Trapdoor Knapsack Encryption Scheme...");
			}
		}while (option != 3);

	}
	//------------------------------------------------------------------------
	// User Input functions
	//------------------------------------------------------------------------

	// Input private keys set
	public static void inputPrivateKeys(int n, int[] privateKeys)
	{
		// privateKeys (2, 5. 9. 21. 45, 103, 215, 450)
		for (int i = 0; i < n; i++)
		{
			int a;
			do {
				a = readInt ("Enter private key a[" + i + "]: ");
				if (!isValidPrivateKey(privateKeys, a))
				{
					System.out.println("** Invalid value **\n");
				}
			}while (!isValidPrivateKey(privateKeys, a));
			privateKeys[i] = a;
		}
	}

	// Input modulus m
	public static int inputModulus(int[] privateKeys)
	{
		int m;
		do {
			m = readInt ("Enter a modulus m: ");
			if (!checkModulus (m, privateKeys))
			{
				System.out.println("** Invalid modulus **\n");
			}
		}while (!checkModulus (m, privateKeys));

		return m;
	}

	// Input Multiplier w, where gcd (w, m) =  1
	public static int inputMultiplier(int m)
	{
		int w;
		do {
			w = readInt ("Enter a multiplier w: ");
			if (!isRelativePrime(w, m))
			{
				System.out.println("** Invalid multiplier **\n");
			}
		}while (!isRelativePrime(w, m));

		return w;
	}

	// Check if each a is valid, a > sum of previous a
	public static boolean isValidPrivateKey (int[] privateKeys, int a)
	{
		int sum = 0;
		for (int i = 0; i < privateKeys.length; i++)
		{
			sum += privateKeys[i];
		}

		if (a <= sum)
		{
			return false;
		}

		return true;
	}

	// Check modulus m > sum of a (private keys)
	public static boolean checkModulus (int m, int[] privateKeys)
	{
		int sum = 0;
		for (int i = 0; i< privateKeys.length; i++)
		{
			sum += privateKeys[i];
		}

		if (m > sum)
		{
			return true;
		}

		return false;
	}

	// Check gcd (w, m) = 1
	public static boolean isRelativePrime (int w, int m)
	{
		if (w > m || w < 1) // Check if w is greater than m
		{
			return false;
		}
		BigInteger wBI = new BigInteger(Integer.toString(w));
		BigInteger mBI = new BigInteger(Integer.toString(m));
		BigInteger result = wBI.gcd(mBI);
		if (result.equals(one))
		{
			return true;
		}
		return false;
	}

	// Get inverse w
	public static int getMulitplierInverse (int w, int m)
	{
		BigInteger wBI = new BigInteger(Integer.toString(w));
		BigInteger mBI = new BigInteger(Integer.toString(m));
		BigInteger inverse = wBI.modInverse(mBI);
		return inverse.intValue();
	}

	// Generate public keys
	public static void generatePublicKeys (int w, int m, int[] privateKeys, int[] publicKeys)
	{
		// Generate public keys b[i] = a[i] * w mod m
		for (int i = 0; i< publicKeys.length; i++)
		{
			publicKeys[i] = (w * privateKeys[i]) % m;
		}
	}

	// Input ciphertext set
	public static void inputCipherTotals(int num, ArrayList<Integer> totals)
	{
		// totals (589, 873)
		for (int i = 0; i < num; i++)
		{
			int t = readInt ("Enter Cipher Text t[" + i + "]: ");
			totals.add(t);
		}
	}

	// Print array
	public static void printArray (int[] arr)
	{
		System.out.print("(");
		for (int i = 0; i < arr.length; i++)
		{
			if (i == arr.length-1)
			{
				System.out.println( arr[i] + ")");
			}
			else
			{
				System.out.print( arr[i] + ",");
			}
		}
	}

	//------------------------------------------------------------------------
	// Encryption and Decryption functions
	//------------------------------------------------------------------------

	// Convert Ascii string to binary string
	public static String convertStrToBinary(String str)
	{
		String strBin = "";
		for (char ch: str.toCharArray())
		{
			// Space and symbols are converted too
			String chBin = String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0');
			strBin += chBin;
		}
		return strBin;
	}

	// Convert binary string to Ascii string
	public static String convertBinaryToStr(String bin)
	{
		String str = "";
		// binary for each char is [0, 7], [8, 15], etc (increment index by 8 ( 9 if there are space-delimited)
		for(int i = 0; i <= bin.length()-8; i += 8)
		{
			int ascii = Integer.parseInt(bin.substring(i, i+8), 2);
			char ch = (char)ascii;

			str += ch;
		}
		return str;
	}

	// Divide plaintext binary into blocks
	public static void divideDataBlocks(String bin, int n, ArrayList<String> blocks)
	{
		// divided into blocks of n (size)
		for(int i = 0; i <= bin.length()-n; i += n)
		{
			blocks.add(bin.substring(i, i+n));
		}

		if (bin.length() % n != 0)
		{
			padding = n - (bin.length() % n);
			int start = bin.length() - (bin.length() % n);
			String str = bin.substring(start);
			for (int i = 0; i < padding; i++)
			{
				str += "0";
			}
			blocks.add(str);
		}
	}

	// Encryption
	public static void ksEncryption(String msg, int[] publicKeys, ArrayList<Integer> totals, int n)
	{
		ArrayList<String> blocks = new ArrayList<String>();
		totals.clear(); // Reset and clear arraylist

		// Convert msg to binary
		String msgBin = convertStrToBinary(msg);
		// Divide binary into blocks
		divideDataBlocks(msgBin, n, blocks);

		for (String aBlock : blocks)
		{
			int j = 0;
			int total = 0;
			for (char ch : aBlock.toCharArray())
			{
				int num = Character.getNumericValue(ch);
				int result = num * publicKeys[j];
				total += result;
				j++;
			}
			totals.add(total);
		}
	}

	// Decryption
	public static String ksDecryption(int[] privateKeys, ArrayList<Integer> totals, int wInverse, int m)
	{
		ArrayList<String> binaryList = new ArrayList<String>();
		ArrayList<Integer> yList = new ArrayList<Integer>();
		// compute y = w^-1 * T mod m
		for (Integer t : totals)
		{
			yList.add((wInverse * t) % m);
		}

		String binaryStr = "";
		// Recover binary plaintext using easy knapsack a (private keys) to find x (plaintext) since y = a.x
		for (Integer y : yList)
		{
			binaryStr = "";
			// Check y to privatekeys in reverse
			for (int i = privateKeys.length-1; i >= 0; i--)
			{
				if (y >= privateKeys[i])
				{
					binaryStr = "1" + binaryStr;
					y -= privateKeys[i];
				}
				else
				{
					binaryStr = "0" + binaryStr;
				}
			}
			// Store in binaryList
			binaryList.add(binaryStr);
		}

		// Remove padding (if there is) and combine into a string
		binaryStr = getPlaintextBinaryStr(binaryList);

		return convertBinaryToStr(binaryStr);

	}

	// Combine the binary arraylist into a string and remove padding if there is
	public static String getPlaintextBinaryStr(ArrayList<String> binaryList)
	{

		// Combine the binaryList into a string
		String binaryStr = "";
		for (String s : binaryList)
		{
			binaryStr += s;

		}

		// if there is padding, have to remove the padding
		if (padding != 0)
		{
			int end = (binaryStr.length() - padding);
			binaryStr = binaryStr.substring(0, end);
		}

		return binaryStr;
	}

	//------------------------------------------------------------------------
	// Utility functions
	//------------------------------------------------------------------------

	// Display main menu
	public static int menu ()
	{
		int option = 0;

		while (option < 1 || option > 3)
		{
			System.out.println ("\n--------------Menu--------------");
			System.out.println ("1. Trapdoor Knapsack Encryption");
			System.out.println ("2. Trapdoor Knapsack Decryption");
			System.out.println ("3. Quit");

			option = readInt ("\nEnter option: ");

			if (option < 1 || option > 3)
			{
				System.out.println("Invalid option (1 to 3 only). Please try again.");
			}

			System.out.println ("-----------------------------------------------");
		}
		return option;
	}

	public static int readInt(String prompt) {
		int input = 0;
		boolean valid = false;
		while (!valid) {
			try {
				input = Integer.parseInt(readString(prompt));
				valid = true;
			} catch (NumberFormatException e) {
				System.out.println("*** Please enter an integer ***");
			}
		}
		return input;
	}

	public static String readString(String prompt) {
		System.out.print(prompt);
		return new Scanner(System.in).nextLine();
	}
}
