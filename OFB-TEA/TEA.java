/*
 * 4-bit OFB TEA
 * --------------------------
 * Author: Eng Kia Hui
 *
 */


import java.io.*;
import java.math.*;
import java.util.*;


public class TEA {
	private static ArrayList<String> inputBlockList = new ArrayList<String>();
	private static ArrayList<String> outputBlockList = new ArrayList<String>();
	private static long MASK32 = (1L << 32) -1;

	public static void main(String args[]) {

		System.out.println("\n4-Bits OFB");
		System.out.println("==============================================================\n");

		long[] blocks = new long[2];
		long[] keys = new long[4];

		String studNo = "5986187";

		BigInteger iv = new BigInteger("16887581474592791356");
		BigInteger key = new BigInteger("314823123807783850795370303762222712261");

		System.out.println("iv: " + iv);
		System.out.println("Student Number: " + studNo);

		// Setup of the program
		// Convert Stud no to binary
		convertToBin(studNo, inputBlockList);

		// Divide key to 4 parts
		splitKey(key, keys);

		// Divide iv to 2 parts
		splitIV(iv, blocks);

		System.out.println("\nChoose a option");

		// Menu to choose encrypt/decrypt for 4-bits and c-bits
		int option = menu();

		if (option == 1)
		{
			// Encryption of student number
			System.out.println("\nRunning 4-bit OFB Encryption...");
			outputBlockList.clear();
			long startTime = System.nanoTime();
			String cipherTxt = encryption (iv, blocks, keys);
			long endTime = System.nanoTime();
			System.out.println("\n\nAfter encryption, CipherText: " + cipherTxt);

			System.out.println("\nStart Time (Nanoseconds): " + startTime);
			System.out.println("End Time (Nanoseconds): " + endTime);
			System.out.println("Time taken (Nanoseconds): " + (endTime - startTime) + " nanoseconds");

		}
		else if (option == 2)
		{
			// Need to run encrypt first
			String cipherTxt = encryptionNoMsg (iv, blocks, keys);
			System.out.println("CipherText for Student number: " + cipherTxt);

			// Decryption of student number
			System.out.println("\nRunning 4-bit OFB Decryption...");
			inputBlockList.clear();
			// Copy output to inputBlockList
			for (String aBlock:outputBlockList) {
				inputBlockList.add(aBlock);
			}
			outputBlockList.clear();
			String plainTxt = "";
			splitIV(iv, blocks); // Split the iv again
			plainTxt = decryption(iv, blocks, keys);
			System.out.println("\n\nAfter decryption, PlainText: " + plainTxt);
		}
		else if (option == 3)
		{
			// Getting cBits
			int cBits = getCBits(studNo);
			System.out.println("Cbits: " + cBits);

			// Convert Stud no to binary
			inputBlockList.clear();
			convertCBitBin(cBits, studNo, inputBlockList);

			System.out.println("\nRunning C-bit OFB Encryption...");
			long startTime = System.nanoTime();
			String cipherTxt = cBitsEncryption(cBits, iv, blocks, keys);
			long endTime = System.nanoTime();
			System.out.println("\n\nAfter encryption, CipherText: " + cipherTxt);

			System.out.println("\nStart Time (Nanoseconds): " + startTime);
			System.out.println("End Time (Nanoseconds): " + endTime);
			System.out.println("Time taken (Nanoseconds): " + (endTime - startTime) + " nanoseconds");
		}
		else if (option == 4)
		{
			// Getting cBits
			int cBits = getCBits(studNo);
			System.out.println("Cbits: " + cBits);
			// Convert Stud no to binary
			inputBlockList.clear();
			convertCBitBin(cBits, studNo, inputBlockList);

			// Need to run encrypt first
			String cipherTxt = cBitsEncryptionNoMsg (cBits, iv, blocks, keys);
			System.out.println("CipherText for Student number: " + cipherTxt);

			// Decryption of student number
			System.out.println("\nRunning c-bit OFB Decryption...");
			inputBlockList.clear();
			// Copy output to inputBlockList
			for (String aBlock:outputBlockList) {
				inputBlockList.add(aBlock);
			}
			outputBlockList.clear();
			String plainTxt = "";
			splitIV(iv, blocks); // Split the iv again
			plainTxt = cBitsDecryption(cBits, iv, blocks, keys);
			System.out.println("\n\nAfter decryption, PlainText: " + plainTxt);
		}

	}

	// To get c-bits from adding all digits of student number and mod 8
	public static int getCBits (String studNo)
	{
		System.out.println("Processing c-bits using student number...");
		int sum = 0;
		for (char ch: studNo.toCharArray())
		{
			// Sum all digits and mod 8 to get cBits
			sum += Character.getNumericValue(ch);

		}
		// Sum = 44 mod 8
		int cBits = sum % 8; // cbits = 4

		// Add 1 to cbits as 4 is not valid
		if (cBits == 0 || cBits == 4)
		{
			System.out.println("C-Bits is " + cBits + ", which is invalid thus plus 1 to the current c-bits.\n");
			cBits += 1;
		}

		// Cbits = 5
		return cBits;
	}

	// 4-bits encryption
	public static String encryption(BigInteger iv, long[] blocks, long[] keys) {
		String sBits = "";
		String cipherBin = "";
		String nextIvBin = iv.toString(2);

		int round = 1;
		for (String aBlock:inputBlockList)
		{
			System.out.println("\nRound " + round);
			// if is round 2 and above then have to add 4-bit to the iv
			if (round > 1)
			{
				nextIvBin = nextIvBin.substring(4) + sBits;
				System.out.println("New IV: " + nextIvBin);
				BigInteger nextIV = new BigInteger (nextIvBin, 2);
				splitIV(nextIV, blocks);
			}
			teaEncrypt(blocks, keys);
			System.out.println("After TEA encryption");

			String output = Long.toBinaryString((blocks[0] & MASK32) << 32 | (blocks[1] & MASK32));
					//Long.toBinaryString(blocks[0]) + Long.toBinaryString(blocks[1]);
			System.out.println("Output from TEA in binary: " + output);

			// Output xor with plaintext (only 4-bits)
			sBits = output.substring(0, 4);
			System.out.println("sBits: " + sBits);
			System.out.println("PlainText in 4-bits: " + aBlock);
			cipherBin = xorOperation(sBits, aBlock);
			outputBlockList.add(cipherBin);
			System.out.println("After sBits XOR PlainText:");
			System.out.println("CipherText in binary: " + cipherBin);
			round ++;
		}

		String cipherTxt = "";
		// need to add all ouputs
		for (String aBlock: outputBlockList)
		{
			// Convert ciphertxt to int and back to a string
			cipherTxt += Integer.parseInt(aBlock, 2);
		}

		return cipherTxt;
	}

	// For decryption - need to do encryption first so encryption with no display msg
	public static String encryptionNoMsg(BigInteger iv, long[] blocks, long[] keys) {
		String sBits = "";
		String cipherBin = "";
		String nextIvBin = iv.toString(2);

		int round = 1;
		for (String aBlock:inputBlockList)
		{

			// if is round 2 and above then have to add 4-bit to the iv
			if (round > 1)
			{
				nextIvBin = nextIvBin.substring(4) + sBits;
				BigInteger nextIV = new BigInteger (nextIvBin, 2);
				splitIV(nextIV, blocks);
			}
			teaEncrypt(blocks, keys);
			String output = Long.toBinaryString((blocks[0] & MASK32) << 32 | (blocks[1] & MASK32));

			// Output xor with plaintext (only 4-bits)
			sBits = output.substring(0, 4);
			cipherBin = xorOperation(sBits, aBlock);
			outputBlockList.add(cipherBin);
			round ++;
		}

		String cipherTxt = "";
		// need to add all ouputs
		for (String aBlock: outputBlockList)
		{
			// Convert ciphertxt to int and back to a string
			cipherTxt += Integer.parseInt(aBlock, 2);
		}

		return cipherTxt;
	}

	// 4-bits encryption
	public static String decryption(BigInteger iv, long[] blocks, long[] keys) {
		String sBits = "";
		String plainBin = "";
		String nextIvBin = iv.toString(2);

		int round = 1;
		for (String aBlock : inputBlockList)
		{
			System.out.println("\nRound " + round);
			// if is round 2 and above then have to add 4-bit to the iv
			if (round > 1) {
				nextIvBin = nextIvBin.substring(4) + sBits;
				System.out.println("New IV: " + nextIvBin);
				BigInteger nextIV = new BigInteger(nextIvBin, 2);
				splitIV(nextIV, blocks);
			}
			teaEncrypt(blocks, keys);
			System.out.println("After TEA encryption");

			String output = Long.toBinaryString((blocks[0] & MASK32) << 32 | (blocks[1] & MASK32));
			System.out.println("Output from TEA in binary: " + output);

			// Output xor with plaintext (only 4-bits)
			sBits = output.substring(0, 4);

			System.out.println("sBits: " + sBits);
			System.out.println("CipherText in 4-bits: " + aBlock);
			plainBin = xorOperation(sBits, aBlock);
			outputBlockList.add(plainBin);
			System.out.println("After sBits XOR CipherText:");
			System.out.println("PlainText in binary: " + plainBin);
			round++;
		}

		String plainTxt = "";
		// need to add all ouputs
		for (String aBlock: outputBlockList)
		{
			// Convert ciphertxt to int and back to a string
			plainTxt += Integer.parseInt(aBlock, 2);
		}

		return plainTxt;
	}

	// C-bits encryption
	public static String cBitsEncryption(int cBits, BigInteger iv, long[] blocks, long[] keys) {
		String sBits = "";
		String cipherBin = "";
		String nextIvBin = iv.toString(2);

		int round = 1;
		for (String aBlock:inputBlockList)
		{
			System.out.println("\nRound " + round);
			// if is round 2 and above then have to add c-bit to the iv
			if (round > 1)
			{
				nextIvBin = nextIvBin.substring(cBits) + sBits;
				System.out.println("New IV: " + nextIvBin);
				BigInteger nextIV = new BigInteger (nextIvBin, 2);
				splitIV(nextIV, blocks);
			}
			teaEncrypt(blocks, keys);
			System.out.println("After TEA encryption");
			String output = Long.toBinaryString((blocks[0] & MASK32) << 32 | (blocks[1] & MASK32));
			System.out.println("Output from TEA in binary: " + output);

			// Output xor with plaintext (C-bits which is 5)
			sBits = output.substring(0, cBits);
			System.out.println("sBits: " + sBits);
			System.out.println("PlainText in c-bits: " + aBlock);
			cipherBin = xorOperation(sBits, aBlock);
			outputBlockList.add(cipherBin);
			System.out.println("After sBits XOR PlainText:");
			System.out.println("CipherText in binary: " + cipherBin);
			round ++;
		}

		String cipherTxt = "";
		// need to add all ouputs
		for (String aBlock: outputBlockList)
		{
			// Convert ciphertxt to int and back to a string
			cipherTxt += Integer.parseInt(aBlock, 2);
		}

		return cipherTxt;
	}

	// C-bits decryption with no display msg
	public static String cBitsEncryptionNoMsg(int cBits, BigInteger iv, long[] blocks, long[] keys) {
		String sBits = "";
		String cipherBin = "";
		String nextIvBin = iv.toString(2);

		int round = 1;
		for (String aBlock:inputBlockList)
		{
			// if is round 2 and above then have to add c-bit to the iv
			if (round > 1)
			{
				nextIvBin = nextIvBin.substring(cBits) + sBits;

				BigInteger nextIV = new BigInteger (nextIvBin, 2);
				splitIV(nextIV, blocks);
			}
			teaEncrypt(blocks, keys);
			String output = Long.toBinaryString((blocks[0] & MASK32) << 32 | (blocks[1] & MASK32));

			// Output xor with plaintext (C-bits which is 5)
			sBits = output.substring(0, cBits);

			cipherBin = xorOperation(sBits, aBlock);
			outputBlockList.add(cipherBin);

			round ++;
		}

		String cipherTxt = "";
		// need to add all ouputs
		for (String aBlock: outputBlockList)
		{
			// Convert ciphertxt to int and back to a string
			cipherTxt += Integer.parseInt(aBlock, 2);
		}

		return cipherTxt;
	}

	// C-bits decryption
	public static String cBitsDecryption(int cBits, BigInteger iv, long[] blocks, long[] keys) {
		String sBits = "";
		String plainBin = "";
		String nextIvBin = iv.toString(2);

		int round = 1;
		for (String aBlock : inputBlockList)
		{
			System.out.println("\nRound " + round);
			// if is round 2 and above then have to add 4-bit to the iv
			if (round > 1) {
				nextIvBin = nextIvBin.substring(cBits) + sBits;
				System.out.println("New IV: " + nextIvBin);
				BigInteger nextIV = new BigInteger(nextIvBin, 2);
				splitIV(nextIV, blocks);
			}
			teaEncrypt(blocks, keys);
			System.out.println("After TEA encryption");
			String output = Long.toBinaryString((blocks[0] & MASK32) << 32 | (blocks[1] & MASK32));
			System.out.println("Output from TEA in binary: " + output);

			// Output xor with plaintext (only 4-bits)
			sBits = output.substring(0, cBits);

			System.out.println("sBits: " + sBits);
			System.out.println("CipherText in c-bits: " + aBlock);
			plainBin = xorOperation(sBits, aBlock);
			outputBlockList.add(plainBin);
			System.out.println("After sBits XOR CipherText:");
			System.out.println("PlainText in binary: " + plainBin);
			round++;
		}

		String plainTxt = "";
		// need to add all ouputs
		for (String aBlock: outputBlockList)
		{
			// Convert ciphertxt to int and back to a string
			plainTxt += Integer.parseInt(aBlock, 2);
		}

		return plainTxt;
	}

	// Convert student number into c-bits binary
	public static void convertCBitBin (int cBits, String studNo, ArrayList<String> inputBlockList)
	{
		System.out.println("Student Number in binary: ");
		for (char ch: studNo.toCharArray())
		{
			// Need to convert each char to a c-bit binary string
			String inputBin = Integer.toBinaryString(Character.getNumericValue(ch));
			while (inputBin.length() < cBits)
			{
				inputBin = "0" + inputBin;
			}
			inputBlockList.add(inputBin);
			System.out.print(inputBin + "\t");
		}
		System.out.println("\n");
	}

	// TEA Encryption
	public static void teaEncrypt(long[] block, long[] key) {
		long k0 = key[0];
		long k1 = key[1];
		long k2 = key[2];
		long k3 = key[3];
		long v0 = block[0];
		long v1 = block[1];

		long sum = 0l;
		long delta = 0x9e3779b9;

		for (int i = 0; i < 32; ++i)
		{
			sum += delta;

			v0 += (v1 << 4) + (long)Math.pow(k0,v1) + (long)Math.pow(sum,(v1 >>> 5)) + k1;
			v1 += (v0 << 4) + (long)Math.pow(k2,v0) + (long)Math.pow(sum,(v0 >>> 5)) + k3;
		}


		block[0] = v0;
		block[1] = v1;

	}

	// TEA Decryption
	public static void teaDecrypt(long[] block, long[] key) {
		long k0 = key[0];
		long k1 = key[1];
		long k2 = key[2];
		long k3 = key[3];
		long v0 = block[0];
		long v1 = block[1];
		long sum = 0xC6EF3720;
		long delta = 0x9e3779b9;

		for (int i = 0; i < 32; ++i)
		{
			v1 -= (v0 << 4) + (long)Math.pow(k2,v0) + (long)Math.pow(sum,(v0 >>> 5)) + k3;
			v0 -= (v1 << 4) + (long)Math.pow(k0,v1) + (long)Math.pow(sum,(v1 >>> 5)) + k1;

			sum -= delta;
		}

		block[0] = v0;
		block[1] = v1;
	}

	// Convert student number to binary and store into inputBlockList
	public static void convertToBin (String studNo, ArrayList<String> inputBlockList)
	{
		System.out.println("Student Number in binary: ");
		for (char ch: studNo.toCharArray())
		{
			// Need to convert each char to a 4-bit binary string
			String inputBin = Integer.toBinaryString(Character.getNumericValue(ch));
			while (inputBin.length() < 4)
			{
				inputBin = "0" + inputBin;
			}
			inputBlockList.add(inputBin);
			System.out.print(inputBin + "\t");
		}
		System.out.println("\n");
	}

	// xor operation
	public static String xorOperation (String x, String y)
	{
		// XOR operation
		// f(a, b) = a + b - 2 * a * b
		String rStr = "";
		int a, b, result;
		int size = x.length(); // get the length of the string
		for (int r = 0; r < size; r++)
		{
			a = Character.getNumericValue(x.charAt(r));
			b = Character.getNumericValue(y.charAt(r));
			result = a + b - (2 * a * b);
			rStr += result;
		}

		return rStr;
	}

	// Split keys into 4 keys
	public static void splitKey(BigInteger key, long[] keys) {
		String binKey = key.toString(2);
		int indexNum = 0;
		int KeyQuadSize = binKey.length()/4;

		for (int i = 0; i < 4; i++)
		{
			keys[i] = Long.parseLong(binKey.toString().substring(indexNum, indexNum+KeyQuadSize), 2);

			indexNum += KeyQuadSize;
			System.out.println("key " + i + " " + keys[i]);
		}
	}

	// Split iv into 2 blocks
	public static void splitIV (BigInteger iv, long[] blocks)
	{
		String ivStr = "";

		for (int i = 0; i < 2; i++)
		{
			ivStr = iv.toString();
			if (i == 0)
			{
				blocks[i] = Long.parseLong(ivStr.substring(i, ivStr.length() / 2));
			}
			else
			{
				blocks[i] = Long.parseLong(ivStr.substring(ivStr.length() / 2, ivStr.length()));
			}

		}

	}


	//------------------------------------------------------------------------
	// Utility functions
	//------------------------------------------------------------------------

	// Display main menu
	public static int menu ()
	{
		int option = 0;

		while (option < 1 || option > 4)
		{
			System.out.println ("1. 4-bit OFB Encryption");
			System.out.println ("2. 4-bit OFB Decryption");
			System.out.println ("3. C-bit OFB Encryption");
			System.out.println ("4. C-bit OFB Decryption");
			option = readInt ("\nEnter option: ");

			if (option < 1 || option > 4)
			{
				System.out.println("Invalid option (1 to 4 only). Please try again.");
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
