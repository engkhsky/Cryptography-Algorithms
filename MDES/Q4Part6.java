/*
 * CSCI361 Assignment 1
 * --------------------------
 * File name: Q4Part6.java
 * Author: Eng Kia Hui
 * Student Number: 5986187 (UOW)
 */

import java.util.ArrayList;

public class Q4Part6 {

	private static String aBit = "";
	private static String bBit = "";
	private static String k1 = "";
	private static String k2 = "";

	public static void main(String[] args)
	{
		// java Q4Part6 -key 10 -mode ECB -encrypt f4a5a32
		// java Q4Part6 -key 10 -mode ECB -decrypt 98a5ac1
		// java Q4Part6 -key 11 -mode CBC -iv a -encrypt 2a45def
		// java Q4Part6 -key 00 -mode CBC -iv a -decrypt 4bfadcc

		System.out.println("\nMDES (ECB and CBC Mode)");
		System.out.println("==============================================================\n");

		ArrayList<String> inputBlockList = new ArrayList<String>();
		ArrayList<String> outputBlockList = new ArrayList<String>();

		String key = args[1];

		if (checkValidBinary(key, 2))
		{
			// Split key to k1 and k2 which each consist 3 bits
			// k1: k1k1k1
			// k2: k2k2k2
			convertKey(key);

			if (args[3].equals("ECB"))
			{
				String mode = args[4];
				String input = args[5]; // is in hex string
				System.out.println("Input: " + input + "\nKey: " + key);
				// Convert hex input to binary input
				// Each hex character to a binary block
				for (char ch: input.toCharArray())
				{
					// Need to convert hex to a 4-bit binary string
					inputBlockList.add(convertTo4BitBinary(ch));
				}
				ECBMode(mode, inputBlockList, outputBlockList);
			}
			else if (args[3].equals("CBC"))
			{
				String mode = args[6];
				String iv = args[5];
				String input = args[7]; // is in hex string
				System.out.println("Input: " + input + "\nKey: " + key);
				System.out.println("IV: " + iv);
				// Convert hex input to binary input
				// Each hex character to a binary block
				for (char ch: input.toCharArray())
				{
					// Need to convert hex to a 4-bit binary string
					inputBlockList.add(convertTo4BitBinary(ch));
				}
				CBCMode(mode, iv, inputBlockList, outputBlockList);
			}
		}
		else
		{
			System.out.println("Key is not valid.");
		}

	}


	// Function to check if the key and input is a valid binary with the correct bitsize
	public static boolean checkValidBinary (String binary, int bitSize)
	{
		if (binary.length() != bitSize)
		{
			return false;
		}

		for (int i = 0; i < bitSize; i++)
		{
			if (binary.charAt(i) != '0' && binary.charAt(i) != '1')
			{
				return false;
			}
		}

		return true;
	}

	// Function to convert a character to 4-bit binary
	public static String convertTo4BitBinary (char ch)
	{
		String hex = String.valueOf(ch);
		int hexInt = (Integer.parseInt(hex, 16));
		String strBlock = Integer.toBinaryString(hexInt);

		while (strBlock.length() != 4)
		{
			strBlock = "0" + strBlock;
		}

		return strBlock;
	}

	// Function to convert binary to hex string
	public static String convertToHex (String bin)
	{
		int binInt = Integer.parseInt(bin,2);
		String hexStr = Integer.toString(binInt,16);

		return hexStr;
	}

	// Function for ECB mode
	public static void ECBMode (String mode, ArrayList<String> inputBlockList, ArrayList<String> outputBlockList)
	{
		System.out.println("\nECB Mode");
		System.out.println("--------");

		System.out.println("\nConvert each hex character to 4-bit binary string: ");
		for (String aBlock: inputBlockList)
		{
			System.out.print(aBlock + "\t");
		}

		if (mode.equals("-encrypt"))
		{
			System.out.println("\n\nRunning Block Cipher Encryption (MDES)...");

			System.out.println("\nEach cipher character in binary:");
			for (String aBlock: inputBlockList) {

				// Rotate left
				aBlock = aBlock.substring(1) + aBlock.substring(0, 1);

				// Split msg into 2 part A0 and B0
				aBit = aBlock.substring(0, 2);
				bBit = aBlock.substring(2, 4);

				String cipherChar = encryption(k1, k2);
				System.out.print(cipherChar + "\t");

				// Convert binary string to hex string
				outputBlockList.add(convertToHex(cipherChar));

			}

			String cipherTxt = "";
			System.out.println("\n\nConvert each binary character to hex string: ");
			for (String aBlock: outputBlockList)
			{
				cipherTxt += aBlock;
				System.out.print(aBlock + "\t");
			}

			System.out.println("\n\nCipherText: " + cipherTxt);
		}
		else if (mode.equals("-decrypt"))
		{
			System.out.println("\n\nRunning Block Cipher Decryption (MDES)...");
			System.out.println("\nEach cipher character in binary:");
			for (String aBlock: inputBlockList) {

				// Rotate left
				aBlock = aBlock.substring(1) + aBlock.substring(0, 1);

				// Split msg into 2 part A0 and B0
				aBit = aBlock.substring(0, 2);
				bBit = aBlock.substring(2, 4);

				String plainChar = decryption(k1, k2);
				System.out.print(plainChar + "\t");

				// Convert binary string to hex string
				outputBlockList.add(convertToHex(plainChar));
			}

			String plainTxt = "";
			System.out.println("\n\nConvert each binary character to hex string: ");
			for (String aBlock: outputBlockList)
			{
				plainTxt += aBlock;
				System.out.print(aBlock + "\t");
			}

			System.out.println("\n\nPlainText: " + plainTxt);
		}

	}

	// Function for CBC mode
	public static void CBCMode (String mode, String iv, ArrayList<String> inputBlockList, ArrayList<String> outputBlockList)
	{
		System.out.println("\nCBC Mode");
		System.out.println("--------");

		System.out.println("\nConvert each hex character to 4-bit binary string: ");
		for (String aBlock: inputBlockList)
		{
			System.out.print(aBlock + "\t");
		}

		// Convert iv to 4 bit binary string, assume only IV only 1 character
		String ivBin = convertTo4BitBinary(iv.charAt(0));

		if (mode.equals("-encrypt"))
		{
			int counter = 0;
			String xorInput = "";

			System.out.println("\n\nRunning Block Cipher Encryption (MDES)...");

			System.out.println("\nEach cipher character in binary:");
			for (String aBlock: inputBlockList)
			{
				// Running CFB Xor operation using iv or last output
				if (counter == 0)
				{
					xorInput = xorOperation(ivBin, aBlock);
				}
				else
				{
					String prevOutput = convertTo4BitBinary(outputBlockList.get(counter-1).charAt(0));
					xorInput = xorOperation(prevOutput, aBlock);
				}

				// Starting MDES process
				// Rotate left
				xorInput = xorInput.substring(1) + xorInput.substring(0, 1);

				// Split msg into 2 part A0 and B0
				aBit = xorInput.substring(0, 2);
				bBit = xorInput.substring(2, 4);

				String cipherChar = encryption(k1, k2);
				System.out.print(cipherChar + "\t");

				// Convert binary string to hex string
				outputBlockList.add(convertToHex(cipherChar));
				counter++;
			}

			String cipherTxt = "";
			System.out.println("\n\nConvert each binary character to hex string: ");
			for (String aBlock: outputBlockList)
			{
				cipherTxt += aBlock;
				System.out.print(aBlock + "\t");
			}

			System.out.println("\n\nCipherText: " + cipherTxt);
		}
		else if (mode.equals("-decrypt"))
		{
			int counter = 0;
			String xorOutput = "";

			System.out.println("\n\nRunning Block Cipher Decryption (MDES)...");
			System.out.println("\nEach cipher character in binary after XOR:");
			for (String aBlock: inputBlockList)
			{
				// Rotate left
				aBlock = aBlock.substring(1) + aBlock.substring(0, 1);

				// Split msg into 2 part A0 and B0
				aBit = aBlock.substring(0, 2);
				bBit = aBlock.substring(2, 4);

				String plainChar = decryption(k1, k2);

				// Running CFB Xor operation using iv or last output
				if (counter == 0)
				{
					xorOutput = xorOperation(ivBin, plainChar);
				}
				else
				{
					String prevInput = inputBlockList.get(counter-1);
					xorOutput = xorOperation(prevInput, plainChar);
				}

				System.out.print(xorOutput + "\t");

				// Convert binary string to hex string
				outputBlockList.add(convertToHex(xorOutput));
				counter++;
			}

			String plainTxt = "";
			System.out.println("\n\nConvert each binary character to hex string: ");
			for (String aBlock: outputBlockList)
			{
				plainTxt += aBlock;
				System.out.print(aBlock + "\t");
			}

			System.out.println("\n\nPlainText: " + plainTxt);
		}
	}



	//------------------------------------------------------------------------
	// MDES functions
	//------------------------------------------------------------------------
	public static void convertKey(String key)
	{
		// Split key to k1 and k2 which each consist 3 bits
		// k1: k1k1k1
		// k2: k2k2k2
		k1 = "";
		k2 = "";
		for (int i = 0; i < 3; i++)
		{
			k1 += key.substring(0, 1);
			k2 += key.substring(1);
		}
	}

	public static void roundAllMode (String key)
	{
		String zResult = fFunction(bBit, key);

		// XOR operation, z xor A0 = B1
		String newbBit = xorOperation(zResult, aBit);

		// Swap B to A
		aBit = bBit;
		// Assign new bBit to aBit
		bBit = newbBit;
	}

	public static String encryption (String k1, String k2)
	{
		// Round 1
		// f function of b0 and k1
		// XOR operation, z xor A0 = B1
		// Swap B0 to A1
		roundOperation(k1);

		// Round 2
		// f function of b1 and k2
		// XOR operation, z xor A1 = B2
		// Swap B1 to A2
		roundOperation(k2);

		// Swap A2 and B2
		// B2A1
		String temp = aBit;
		aBit = bBit;
		bBit = temp;

		String cipherTxt = aBit + bBit;
		// Rotate right
		cipherTxt = cipherTxt.substring(cipherTxt.length()-1) + cipherTxt.substring(0, cipherTxt.length()-1);

		return cipherTxt;
	}

	public static String decryption (String k1, String k2)
	{
		// Round 1
		// f function of b0 and k1
		// XOR operation, z xor A0 = B1
		// Swap B0 to A1
		roundOperation(k2);

		// Round 2
		// f function of b1 and k2
		// XOR operation, z xor A1 = B2
		// Swap B1 to A2
		roundOperation(k1);

		// Swap A2 and B2
		// B2A1
		String temp = aBit;
		aBit = bBit;
		bBit = temp;

		String plainTxt = aBit + bBit;
		// Rotate right
		plainTxt = plainTxt.substring(plainTxt.length()-1) + plainTxt.substring(0, plainTxt.length()-1);

		return plainTxt;
	}

	public static void roundOperation (String key)
	{
		String zResult = fFunction(bBit, key);

		// XOR operation, z xor A0 = B1
		String newbBit = xorOperation(zResult, aBit);

		// Swap B to A
		aBit = bBit;
		// Assign new bBit to aBit
		bBit = newbBit;
	}

	public static String fFunction (String x, String y)
	{
		// Expansion for x (2 bit msg)
		x = x + x.substring(0,1);
		//System.out.println("After Expansion Operation \nX1X2X1: " + x + "\tY1Y2Y3: " + y);

		// XOR operation, i is result
		String i = xorOperation(x, y);

//		System.out.println("After XOR Operation \nI1I2I3: " + i);

		// S-Box (MDES version)
		// Converting I1I2I3 to J1J2 operation

		String j = sBox(i);
//		System.out.println("After SBox operation: \nj: " + j);

		// Rotate left
		String z = j.substring(1) + j.substring(0,1);
		//System.out.println("\nAfter left rotate\n z: " + z);

		return z;
	}

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

	public static String sBox (String i)
	{
		String j = "";
		if (i.equals("000") || i.equals("001") || i.equals("010") || i.equals("100") || i.equals("101"))
		{
			j = "00";
		}
		else if (i.equals("011"))
		{
			j = "01";
		}
		else if (i.equals("110"))
		{
			j = "10";
		}
		else if (i.equals("111"))
		{
			j = "11";
		}

		return j;
	}
}
