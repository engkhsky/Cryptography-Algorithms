/*
 * Digital Signature Algorithm (DSA)
 * --------------------------
 * Author: Eng Kia Hui
 *
 */

import java.io.*;
import java.math.*;
import java.util.*;

public class DSA {
	private static BigInteger one = new BigInteger ("1");
	private static BigInteger zero = new BigInteger ("0");
	private static BigInteger p = zero;
	private static BigInteger q;
	private static BigInteger g;

	private static BigInteger x;
	private static BigInteger y;
	private static BigInteger k;
	private static BigInteger msgBI;
	private static BigInteger r;
	private static BigInteger s;


	public static void main(String [] args)
	{
		int option;

		System.out.println("\nDSA Signature");
		System.out.println("=============");

		// Menu
		do {
			option = menu();
			r = null;
			s = null;
			if (option == 1)
			{
				System.out.println("KeyGen: DSA key generation Function");
				System.out.println("===================================\n");
				keyGen();
			}
			else if (option == 2)
			{
				System.out.println("Sign: DSA Signature Function");
				System.out.println("============================\n");

				getKeyData();
				System.out.println("Prime p: " + p);
				System.out.println("Prime q: " + q);
				System.out.println("g: " + g);
				System.out.println("Private key x (Signing key): " + x);
				System.out.println("Public key y (Verification key): " + y);
				System.out.println ("-----------------------------------------------\n");

				// Get msg from input file
				String msg = getInput();
				System.out.println("Message: " + msg);
				// Hash msg
				String hashM = Hash.getHash(msg);
				System.out.println("H(msg): " + hashM);

				// Convert to binary
				String msgBin = convertHexToBinary(hashM);
//				String msgBin = convertStrToBinary(msg);

				// Convert to big integer
				msgBI = new BigInteger(msgBin, 2);
				System.out.println("Message in BigInteger: " + msgBI);

				// Signing
				System.out.println("\nSigning Signature");
				System.out.println("-----------------");
				// Pick k < q-1
				 k = getRandomNum(one, q.subtract(one));
				System.out.println("k: " + k);
				// Compute r = (g^k mod p) mod q
				r = g.modPow(k, p).mod(q);

				// Compute the s = (k^-1(msg + xr)) mod q
				BigInteger kInverse = k.modInverse(q);
				System.out.println("k^-1: " + kInverse);
				// kmxr = k^-1(msg + xr)
				BigInteger xr = x.multiply(r);
				BigInteger kmxr = kInverse.multiply((msgBI).add(xr)); // temp m = 6
				s = kmxr.mod(q);

				System.out.println("Signature (" + r + ", " + s + ")");

				String signature = "r:" + r + "\ns:" + s;

				// Write computed signature into sig.txt
				writeFile(signature, "sig.txt");
				System.out.println("\nWriting signature to sig.txt...\n");

			}
			else if (option == 3)
			{
				System.out.println("Verify: DSA Verify function");
				System.out.println("===========================\n");

				getSignature("sig.txt");
				getKeyData();

				System.out.println("Prime p: " + p);
				System.out.println("Prime q: " + q);
				System.out.println("g: " + g);
				System.out.println("Public key y (Verification key): " + y);
				System.out.println ("-----------------------------------------------\n");

				// Get msg from input file
				String msg = getInput();
				// Hash msg
				String hashM = Hash.getHash(msg);

				// Convert to binary
				String msgBin = convertHexToBinary(hashM);
				// String msgBin = convertStrToBinary(msg);

				// Convert to big integer
				msgBI = new BigInteger(msgBin, 2);

				// Only sig.txt exist then will verify
				if (r != null && s != null)
				{
					// Signature Verification
					// Step 1 Verify
					System.out.println("1. r < q and s < q: " + verifySignRange(q, r, s));
					// Step 2
					// Compare (g^ms^-1 * y^rs^-1 mod p) mod q to r
					BigInteger sInverse = s.modInverse(q);
					BigInteger msI = (msgBI.multiply(sInverse)).mod(q);
					BigInteger rsI = (r.multiply(sInverse)).mod(q);

					BigInteger v = (g.modPow(msI, p).multiply(y.modPow(rsI, p))).mod(p).mod(q);
					System.out.println("2. Compare v = (g^ms^-1 * y^rs^-1 mod p) mod q to r:");
					System.out.println("v = " + v + "\tr = " + r);
					System.out.println("Verify input and signature: " + verifyVToR(v, r));
				}

			}
			else if (option == 4)
			{
				System.out.println("Quitting the DSA program...");
			}

			System.out.println ("-----------------------------------------------");
		}while (option != 4);

	}

	// Display main menu
	public static int menu ()
	{
		int option = 0;

		while (option < 1 || option > 4)
		{
			System.out.println ("\n--------------Menu--------------");
			System.out.println ("1. KeyGen: DSA key generation Function");
			System.out.println ("2. Sign: DSA Signature Function");
			System.out.println ("3. Verify: DSA Verify function");
			System.out.println ("4. Quit");
			option = readInt ("\nEnter option: ");

			if (option < 1 || option > 4)
			{
				System.out.println("Invalid option (1 to 3 only). Please try again.");
			}

			System.out.println ("-----------------------------------------------");
		}
		return option;
	}

	// Function to generate keys
	public static void keyGen()
	{
		// Generate 512 bit p and 160 q
		getPrime(512, 160);
		g = getG(p, q);

		System.out.println("Prime p: " + p);
		System.out.println("Prime q: " + q);
		System.out.println("g: " + g);

		x = getPrivateKey(q);
		System.out.println("Private key x (Signing key): " + x);
		y = getPublicKey (g, x, p);
		System.out.println("Public key y (Verification key): " + y);

		// Store keys into file
		String keyGen = "p:" + p + "\nq:" + q + "\ng:" + g + "\nx:" + x + "\ny:" + y;
		writeFile(keyGen, "keys.txt");
		System.out.println("\nWrite keys into keys.txt");
	}

	// Function to generate a prime modulus p in 512 bits
	public static void getPrime(int bits, int qBits)
	{
		BigInteger k = zero;
		int length;
		int bitDiff = bits - qBits;
		q = BigInteger.probablePrime(qBits, new Random());

		do
		{
			if (!p.isProbablePrime(1))
			{
				q = q.nextProbablePrime();
			}
			k = BigInteger.probablePrime(bitDiff, new Random());
			k = k.subtract(one);

			p = (k.multiply(q)).add(one);
			length = p.bitLength();

		}while (!p.isProbablePrime(1)||length != bits);

	}

	// Function to generate a prime modulus q in 160 bits
	public static BigInteger getPrimeQ(int bits)
	{
		BigInteger q = BigInteger.probablePrime(bits, new Random());

		return q;
	}
	// Generate g = h ^(p-1)/q mod p
	public static BigInteger getG(BigInteger p, BigInteger q)
	{
		boolean isValid = false;
		BigInteger exp, g = zero;
		BigInteger h = zero;
		while (!isValid)
		{
			h = getRandomNum(one, p.subtract(one));
			// exp = (p-1)/q
			exp = (p.subtract(one)).divide(q);
			g = h.modPow(exp, p);
			// g > 1
			int greater = g.compareTo(one);
			if (greater == 1)
			{
				isValid = true;
			}
		}
		return g;
	}

	// Get private key from x < q
	public static BigInteger getPrivateKey(BigInteger q)
	{
		BigInteger x = getRandomNum(one, q);
		return x;
	}

	// Get public key y = g^x mod p
	public static BigInteger getPublicKey(BigInteger g, BigInteger x, BigInteger p)
	{
		// y = g^x mod p
		BigInteger y = g.modPow(x, p);
		return y;
	}

	// Function to generate a random number for BigInteger
	public static BigInteger getRandomNum(BigInteger min, BigInteger max)
	{
		// if the min == max, return min as the randNum
		if (min.compareTo(max) == 0)
		{
			return min;
		}

		// Get the range between min and max
		BigInteger range = max.subtract(min).add(one);

		// Convert the range to bit length format
		int length = range.bitLength();

		// Random generate a number n using the range
		// if the n greater or equal to range, repeat till 0 <= n < range
		// n cannot be == range as later n need to be added to min
		// which will be greater than max if n == range
		BigInteger n;
		do
		{
			n = new BigInteger(length, new Random());

		}while (n.compareTo(range) >= 0);

		// Add n to min to get a random generate randNum
		BigInteger randNum = min.add(n);

		return randNum;
	}

	//------------------------------------------------------------
	// Signing / Verification Functions
	//------------------------------------------------------------
	public static void getSignature(String filename)
	{
		ArrayList<String> infoList = new ArrayList<String>();
		readFile(infoList, filename);

		// Split the infoList into different var: N, p, q, d
		for (String line : infoList)
		{
			String[] info = line.split(":");

			switch (info[0])
			{
				case "r":
					r = new BigInteger(info[1]);
					break;
				case "s":
					s = new BigInteger(info[1]);
					break;
				default:
					System.out.println("** No match found **");
			}
		}
	}


	// Verify r and s within q
	public static boolean verifySignRange(BigInteger q, BigInteger r, BigInteger s)
	{
		int valid = r.compareTo(q);
		if (valid >= 0)
			return false;

		valid = s.compareTo(q);
		if (valid >= 0)
			return false;

		return true;
	}

	// Verify v = r
	public static boolean verifyVToR(BigInteger v, BigInteger r)
	{
		int valid = v.compareTo(r);

		if (valid == 0)
			return true;

		return false;
	}

	// Read secret keys data from file
	public static void getKeyData()
	{
		// Read the info from the txt file and store into a ArrayList
		ArrayList<String> infoList = new ArrayList<String>();
		readFile(infoList, "keys.txt");

		// Split the infoList into different var: p, q, g, y
		for (String line : infoList)
		{
			String[] info = line.split(":");

			switch (info[0])
			{
				case "p":
					p = new BigInteger(info[1]);
					break;
				case "q":
					q = new BigInteger(info[1]);
					break;
				case "g":
					g = new BigInteger(info[1]);
					break;
				case "x":
					x = new BigInteger(info[1]);
					break;
				case "y":
					y = new BigInteger(info[1]);
					break;
				default:
					System.out.println("** No match found **");
			}
		}


	}

	//------------------------------------------------------------
	// Read and write file Functions
	//------------------------------------------------------------
	// Get user input from msg.txt and sig.txt
	public static String getInput()
	{
		Scanner sc;
		String text = "";
		try {
			sc = new Scanner (new File ("msg.txt"));
			while(sc.hasNextLine())
			{
				text += sc.nextLine();
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return text;
	}

	// Function to read info from file and store into a ArrayList
	private static void readFile(ArrayList<String> infoList, String filename)
	{
		// Read file into a arrayList infoList
		Scanner sc;
		try {
			sc = new Scanner (new File (filename));
			while(sc.hasNextLine())
			{
				String text = sc.nextLine();
				infoList.add(text);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("sig.txt not found, please select signing option first.");
		}
	}

	//------------------------------------------------------------------------
	// Converting Binary/Ascii functions
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

	// Pad 0 to the front of binary if not in mulitiples in 8
	public static String padZeroBinary(String bin)
	{
		int loop = 8 - (bin.length() % 8);

		for (int i = 0; i < loop; i++)
		{
			bin = "0" + bin;
		}
		return bin;
	}

	// Convert hex to binary string
	public static String convertHexToBinary (String hex)
	{
		String bin = new BigInteger(hex, 16).toString(2);

		// Pad leading zeros to binary string
		if (bin.length() % 4 != 0) {
			bin = "0000".substring(bin.length() % 4) + bin;
		}

		return bin;
	}

	//------------------------------------------------------------
	// Read and write file Functions
	//------------------------------------------------------------

	// Function to write info to file
	public static void writeFile(String info, String filename)
	{
		try
		{
			File file;
			PrintWriter outfile;

			file = new File(filename);
			outfile = new PrintWriter(file);
			outfile.println(info);
			outfile.close();

		}catch(FileNotFoundException e)
		{
			System.out.println("Error writing to file");
		}
	}


	//------------------------------------------------------------------------
	// Utility functions
	//------------------------------------------------------------------------

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
