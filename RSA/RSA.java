/*
 * RSA
 * --------------------------
 * Author: Eng Kia Hui
 *
 */




import java.io.*;
import java.math.*;
import java.util.*;

public class RSA {

	private static BigInteger one = new BigInteger ("1");
	private static BigInteger p;
	private static BigInteger q;
	private static BigInteger n;
	private static BigInteger m;
	private static BigInteger e;
	private static BigInteger d;
	private static BigInteger cipherBI;

	public static void main(String [] args)
	{
		cipherBI = new BigInteger("0");
		int option;

		System.out.println("\nRSA Algorithm");
		System.out.println("=============\n");

		// Menu
		do {
			option = menu();

			if (option == 1)
			{
				System.out.println("KeyGen: RSA generation Function");
				System.out.println("===============================\n");
				int bit = readInt ("Enter bit-length: ");
				keyGen(bit);
			}
			else if (option == 2)
			{
				System.out.println("Sign: RSA signing function");
				System.out.println("==========================\n");
				storeSKData();
				String msg = getInput(option);
				BigInteger msgBI = new BigInteger(msg);;
				System.out.println("Message: " + msgBI);

				// Sign the message
				BigInteger sign = signFunction(msgBI);
				System.out.println("Signature: " + sign);
				// Write computed signature into sig.txt
				writeFile(sign.toString(), "sig.txt");
				System.out.println("\nWriting signature to sig.txt...\n");
			}
			else if (option == 3)
			{
				System.out.println("Verify: RSA verification function");
				System.out.println("=================================\n");
				storePKData();

				String sig = getInput(option);
				if (sig.length()==0)
				{
					option = 6;
				}
				else
				{
					BigInteger sigBI = new BigInteger(sig);
					System.out.println("Signature: " + sigBI);

					// Retrieve original message from msg.txt
					String msg = getInput(2);
					BigInteger msgBI = new BigInteger(msg);
					System.out.println("Message: " + msgBI);
					// Do verification of the signature and message
					boolean verifySign = verifyFunction(sigBI, msgBI);
					System.out.println("Verification result: " + verifySign + "\n");
				}

			}
			else if (option == 4) // RSA encryption
			{
				System.out.println("Encrypt: RSA Encryption function");
				System.out.println("================================\n");
				storePKData();

				String plaintext = getInput(option);
				BigInteger msgBI = new BigInteger(plaintext);
				System.out.println("Plaintext Message: " + msgBI);
				cipherBI = RSAencrypt(msgBI);
				System.out.println("Encrypted Ciphertext: " + cipherBI + "\n");
			}
			else if (option == 5) // RSA Decryption
			{
				System.out.println("Decrypt: RSA Decryption function");
				System.out.println("================================\n");
				storeSKData();

				if (cipherBI.equals(new BigInteger("0")))
				{
					System.out.println("Please encrypt a message first (Choose option 4)...\n");
				}
				else
				{
					// Decrypt the ciphertext
					BigInteger msgBI = RSAdecrypt(cipherBI);
					System.out.println("Ciphertext: " + cipherBI);
					System.out.println("Decrypted Message: " + msgBI + "\n");
				}
			}
			else if (option == 6)
			{
				System.out.println("Quitting the RSA program...");
			}

			System.out.println ("-----------------------------------------------");
		}while (option != 6);

	}

	//------------------------------------------------------------
	// Convert and input Functions
	//------------------------------------------------------------
	// Get user input from msg.txt and sig.txt
	public static String getInput(int option)
	{
		Scanner sc;
		String text = "";
		if (option == 2)
		{
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
		}
		else if (option == 3)
		{
			try {
				sc = new Scanner (new File ("sig.txt"));
				while(sc.hasNextLine())
				{
					text += sc.nextLine();
				}
				sc.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("File not found, please select signing option.");
			}
		}
		else if (option == 4)
		{
			text = readString ("Enter plaintext message: ");
		}

		return text;
	}

	// Using bytes to convert string to biginteger
	public static BigInteger convertStrToBI(String str)
	{

		//Convert String to BigInteger using bytes
		byte[] bytes = str.getBytes();
		BigInteger strBI = new BigInteger(bytes);

		return strBI;
	}

	// Convert bytes to string
	public static String convertBytetoStr(BigInteger str)
	{
		//Convert byte[] to String
		byte[] b = str.toByteArray();
		String s = new String(b);

		System.out.println(s);
		return s;
	}

	//------------------------------------------------------------
	// Signing and verifying Functions
	//------------------------------------------------------------

	// Read secret keys data from file
	public static void storeSKData()
	{
		// Read the info from the txt file and store into a ArrayList
		ArrayList<String> infoList = new ArrayList<String>();
		readFile(infoList, "sk.txt");

		// Split the infoList into different var: N, p, q, d
		for (String line : infoList)
		{
			String[] info = line.split(":");

			switch (info[0])
			{
				case "N":
					n = new BigInteger(info[1]);
					break;
				case "p":
					p = new BigInteger(info[1]);
					break;
				case "q":
					q = new BigInteger(info[1]);
					break;
				case "d":
					d = new BigInteger(info[1]);
					break;
				default:
					System.out.println("** No match found **");
			}
		}
		System.out.println("Private key: (" + n + ", " + p + ", " + q + ", " + d + ")\n");
	}

	// Read public keys data from file
	public static void storePKData()
	{
		// Read the info from the txt file and store into a ArrayList
		ArrayList<String> infoList = new ArrayList<String>();
		readFile(infoList, "pk.txt");

		// Split the infoList into different var: N, p, q, d
		for (String line : infoList)
		{
			String[] info = line.split(":");

			switch (info[0])
			{
				case "N":
					n = new BigInteger(info[1]);
					break;
				case "e":
					e = new BigInteger(info[1]);
					break;
				default:
					System.out.println("** No match found **");
			}
		}

		System.out.println("Public key: (" + n + ", " + e + ")\n");
	}

	// RSA sign function
	public static BigInteger signFunction(BigInteger msg)
	{
		// S = M^d mod n
		BigInteger s = msg.modPow(d, n);
		return s;
	}

	// RSA verify function
	public static boolean verifyFunction(BigInteger sign, BigInteger msg)
	{
		// M = S^e mod n
		BigInteger mResult = sign.modPow(e, n);

		if (mResult.equals(msg))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// Function to generate keys
	public static void keyGen(int bit)
	{
		// Generate p, q and n
		p = getPrime(bit);
		q = getPrime(bit);
//		p = new BigInteger("59");
//		q = new BigInteger("47");
		n = p.multiply(q);
		System.out.println("Prime p: " + p + "\tPrime q: " + q + "\tPrime N: " + n);

		// Compute m = Euler's totient function = (p-1)*(q-1)
		m = (p.subtract(one)).multiply(q.subtract(one));
		System.out.println("m: " + m + "\n");

		// Find e, 0 < e < m, gcd(e, m) = 1
		e = getPublicE();
//		e = new BigInteger("15");
		System.out.println("Public key: (" + n + ", " + e + ")");

		// Find d, ed = 1 mod m
		d = e.modInverse(m);
		System.out.println("Private key: (" + n + ", " + p + ", " + q + ", " + d + ")");

		// Write public key to file
		String publicKey = "N:" + n + "\ne:" + e;
		// Write a string sharedInfo into files
		writeFile(publicKey, "pk.txt");

		// Write private key to file
		String privateKey = "N:" + n + "\np:" + p + "\nq:" + q + "\nd:" + d;
		// Write private key into file
		writeFile(privateKey, "sk.txt");

		System.out.println("\nWrite public key and private key into pk.txt and sk.txt...");
	}

	// Function to generate a prime number in 32 bits
	public static BigInteger getPrime(int bit)
	{
		BigInteger p = BigInteger.probablePrime(bit, new Random());

		return p;
	}

	// Generate public key
	public static BigInteger getPublicE()
	{
		// Find e, 0 < e < m, gcd(e, m) = 1
		do
		{
			e = getRandomNum(one, m.subtract(one));

		}while(!e.gcd(m).equals(one));
		return e;
	}

	//------------------------------------------------------------
	// RSA encrypting and decrypting Functions
	//------------------------------------------------------------
	public static BigInteger RSAencrypt(BigInteger msg)
	{
		// C = M^e mod n
		BigInteger cipher = msg.modPow(e, n);
		return cipher;
	}

	public static BigInteger RSAdecrypt(BigInteger cipher)
	{
		// M = C^d mod n
		BigInteger msg = cipher.modPow(d, n);
		return msg;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//------------------------------------------------------------------------
	// Utility functions
	//------------------------------------------------------------------------

	// Display main menu
	public static int menu ()
	{
		int option = 0;

		while (option < 1 || option > 6)
		{
			System.out.println ("1. KeyGen: RSA generation Function");
			System.out.println ("2. Sign: RSA signing function");
			System.out.println ("3. Verify: RSA verification function");
			System.out.println ("4. Encrypt: RSA Encryption function");
			System.out.println ("5. Decrypt: RSA Decryption function");
			System.out.println ("6. Quit");
			option = readInt ("\nEnter option: ");

			if (option < 1 || option > 6)
			{
				System.out.println("Invalid option (1 to 6 only). Please try again.");
			}

			System.out.println ("-----------------------------------------------");
		}
		return option;
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
