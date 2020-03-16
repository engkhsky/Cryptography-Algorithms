/*
 * LDES
 * --------------------------
 * Author: Eng Kia Hui
 * Description: LDES is a mini example of a block cipher that has 2 rounds in the Feistel structure
 * For each key = 00, 01, 10, 11,
 * Encrypt the following binary messages = 0000, 1000, 0100, 0010, 0001, 1100, 1010, 1001, 0110, 0101, 0011, 0111, 1011, 1101, 1110, 1111
 * Instruction:
 * 1. Compile file: javac AffineCipher.java
 * 2. Run program with the following parameters for different modes: 
 *    - All keys and binary messages: java LDES -mode all
 *    - Different keys and binary messages:
 *      pt - plaintext
 *      ct - ciphertext
 * Eg:
 * java LDES -mode encrypt -key 11 -pt 1101
 * java LDES -mode decrypt -key 11 -ct 1100
 *
 */


import java.util.ArrayList;

public class LDES {
	private static String aBit = "";
	private static String bBit = "";
	private static String k1 = "";
	private static String k2 = "";

	public static void main(String[] args)
	{


		System.out.println("\nLDES");
		System.out.println("==============================================================\n");

		if (args[1].equals("all"))
		{
			runAllInputsKeys();
		}
		else
		{
			String key = args[3];
			String input = args[5];

			if (checkValidBinary(key, 2) && checkValidBinary(input, 4))
			{
				System.out.println("Input: " + input + "\nKey: " + key);

				// Split key to k1 and k2 which each consist 3 bits
				// k1: k1k1k1
				// k2: k2k2k2
				convertKey(key);

				System.out.println("\nSplit Key and expand to 3-bit:\nk1: " + k1 + "\tk2: " + k2);

				// Rotate left
				input = input.substring(1) + input.substring(0,1);
				System.out.println("\nAfter left rotate, Input: " + input);

				// Split msg into 2 part A0 and B0
				aBit = input.substring(0,2);
				bBit = input.substring(2,4);

				System.out.println("\nSplit the input into 2 2-bit:\nA0: " + aBit + "\tB0: " + bBit);
				if (args[1].equals("encrypt"))
				{
					String cipherTxt = encryption (k1, k2);
				}
				else if (args[1].equals("decrypt"))
				{
					String plainTxt = decryption (k1, k2);
				}
			}
			else
			{
				System.out.println("Key or input is not valid.");
				System.out.println(args[3] + "\t" + args[5]);
			}
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

	// Function to run mode all
	public static void runAllInputsKeys ()
	{
		String input = "";
		String key = "";

		ArrayList<String> keyList = new ArrayList<String>();
		ArrayList<String> inputList = new ArrayList<String>();

		for (int i = 0; i < 4; i++) {
			key = Integer.toBinaryString(i);
			if (key.length() < 2) {
				key = "0" + key;
			}
			keyList.add(key);
		}

		for (int i = 0; i < 16; i++) {
			input = Integer.toBinaryString(i);
			while (input.length() < 4) {
				input = "0" + input;
			}
			inputList.add(input);
		}

		for (String aKey : keyList)
		{
			System.out.println("Using Key = " + aKey + ":\n");

			convertKey(aKey);

			for (String aInput : inputList)
			{
				System.out.printf("%8s", "E(" + aInput + ")");
			}

			System.out.println("\n");

			for (String aInput : inputList)
			{
				// Rotate left
				String m = aInput.substring(1) + aInput.substring(0,1);
				// Split msg into 2 part A0 and B0
				aBit = m.substring(0,2);
				bBit = m.substring(2,4);

				// Round 1
				roundAllMode(k1);
				// Round 2
				roundAllMode(k2);

				// Swap A2 and B2
				// B2A1
				String temp = aBit;
				aBit = bBit;
				bBit = temp;
				String cipherTxt = aBit + bBit;

				// Rotate right
				cipherTxt = cipherTxt.substring(cipherTxt.length()-1) + cipherTxt.substring(0, cipherTxt.length()-1);
				System.out.printf("%8s", cipherTxt + " ");
			}
			System.out.println("\n\n");
		}

	}

	// Function to split the key to 2 3bits keys
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

	// Function to each round operation (mode all)
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

	// Function to encryption
	public static String encryption (String k1, String k2)
	{

		System.out.println("\nEncrypting....");
		System.out.println("--------------");
		// Round 1
		// f function of b0 and k1
		// XOR operation, z xor A0 = B1
		// Swap B0 to A1
		System.out.println("\nRound 1: ");
		roundOperation(k1);
		System.out.println("Result of round 1 \nA1: " + aBit + "\tB1: " + bBit);

		// Round 2
		// f function of b1 and k2
		// XOR operation, z xor A1 = B2
		// Swap B1 to A2
		System.out.println("\nRound 2: ");
		roundOperation(k2);
		System.out.println("Result of round 2 \nA2: " + aBit + "\tB2: " + bBit);

		// Swap A2 and B2
		// B2A1
		String temp = aBit;
		aBit = bBit;
		bBit = temp;
		System.out.println("\nAfter swap, A2: " + aBit + "\tB2: " + bBit);

		String cipherTxt = aBit + bBit;
		System.out.println("Combined A2 and B2: " + cipherTxt);
		// Rotate right
		cipherTxt = cipherTxt.substring(cipherTxt.length()-1) + cipherTxt.substring(0, cipherTxt.length()-1);
		System.out.println("After right rotate, CipherText: " + cipherTxt);

		return cipherTxt;
	}

	// Function to decryption
	public static String decryption (String k1, String k2)
	{
		System.out.println("\nDecrypting....");
		System.out.println("--------------");
		// Round 1
		// f function of b0 and k1
		// XOR operation, z xor A0 = B1
		// Swap B0 to A1
		System.out.println("\nRound 1: ");
		roundOperation(k2);
		System.out.println("Result of round 1 \nA1: " + aBit + "\tB1: " + bBit);

		// Round 2
		// f function of b1 and k2
		// XOR operation, z xor A1 = B2
		// Swap B1 to A2
		System.out.println("\nRound 2: ");
		roundOperation(k1);
		System.out.println("Result of round 2 \nA2: " + aBit + "\tB2: " + bBit);

		// Swap A2 and B2
		// B2A1
		String temp = aBit;
		aBit = bBit;
		bBit = temp;
		System.out.println("\nAfter Swap, A2: " + aBit + "\tB2: " + bBit);

		String plainTxt = aBit + bBit;
		System.out.println("Combined A2 and B2: " + plainTxt);
		// Rotate right
		plainTxt = plainTxt.substring(plainTxt.length()-1) + plainTxt.substring(0, plainTxt.length()-1);
		System.out.println("After right rotate, PlainText: " + plainTxt);

		return plainTxt;
	}

	// Function for round operation
	public static void roundOperation (String key)
	{
		String zResult = fFunction(bBit, key);
		System.out.println("\nAfter f Function: " + zResult);

		// XOR operation, z xor A0 = B1
		String newbBit = xorOperation(zResult, aBit);
		System.out.println("After XOR Operation, New B: " + newbBit);

		// Swap B to A
		aBit = bBit;
		// Assign new bBit to aBit
		bBit = newbBit;
	}

	// Function for f function
	public static String fFunction (String x, String y)
	{
		// Expansion for x (2 bit msg)
		x = x + x.substring(0,1);
		//System.out.println("After Expansion Operation \nX1X2X1: " + x + "\tY1Y2Y3: " + y);

		// XOR operation, i is result
		String i = xorOperation(x, y);

		//System.out.println("After XOR Operation \nI1I2I3: " + i);

		// Linear operation
		// j1 = i2 + 1
		// j2 = i3 + 1
		int i2 = Integer.parseInt(i.substring(1, 2), 2);
		int i3 = Integer.parseInt(i.substring(2), 2);

		String j1 = Integer.toBinaryString(i2 + 1);
		String j2 = Integer.toBinaryString(i3 + 1);

		//System.out.println("j1: " + j1 + " j2: " + j2);

		// Just get rightmost bit of j1 and j2
		if (j1.length() > 1)
		{
			j1 = j1.substring(1);
		}

		if (j2.length() > 1)
		{
			j2 = j2.substring(1);
		}

		String j = j1 + j2;
		//System.out.println("After linear operation: \nj1: " + j1 + " j2: " + j2 + " j: " + j);

		// Rotate left
		String z = j.substring(1) + j.substring(0,1);
		//System.out.println("\nAfter left rotate\n z: " + z);

		return z;
	}

	// Function for xor operation
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

}
