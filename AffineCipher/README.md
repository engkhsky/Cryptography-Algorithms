# Instructions:

Compile the files in cmd (Windows):

## Affine Cipher

**Description:**

Consider the Affine Cipher on the alphabet Z26 = {A, B, . . . , Z} where A = 0, B = 1, . . . , Z = 25,
and the encryption function with key (a, b): C = aM + b (mod 26)

**Instructions:**

1. Compile file: javac AffineCipher.java

2. Run program with the following parameters:

    - a flag to indicate encryption or decryption, a secret key, an input file name, and an output file name.

    For example:

	java AffineCipher -key 3 9 -encrypt -in input.txt -out cipher.txt

	java AffineCipher -key 3 9 -decrypt -in cipher.txt -out output.txt
