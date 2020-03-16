# Instructions:

Compile the files in cmd (Windows):

## Affine Cipher
--------------------------
Description:
Consider the Affine Cipher on the alphabet Z26 = {A, B, . . . , Z} where A = 0, B = 1, . . . , Z = 25,
and the encryption function with key (a, b): C = aM + b (mod 26)

Instructions:
1. Compile file: javac AffineCipher.java

2. Run program with the following parameters:
   - a flag to indicate encryption or decryption, a secret key, an input file name, and an output file name.

   For example:
   java AffineCipher -key 3 9 -encrypt -in input.txt -out cipher.txt
   java AffineCipher -key 3 9 -decrypt -in cipher.txt -out output.txt


LDES
--------------------------
Description: LDES is a mini example of a block cipher that has 2 rounds in the Feistel structure
It operates on 4-bit block and 2-bit key. The important feature of LDES is that the S-box has been replaced with a linear operation, which makes the whole cipher linear.
For each key = 00, 01, 10, 11, encrypt the following binary messages = 0000, 1000, 0100, 0010, 0001, 1100, 1010, 1001, 0110, 0101, 0011, 0111, 1011, 1101, 1110, 1111
Instruction:
1. Compile file: javac LDES.java

2. Run program with the following parameters for different modes:
   - All keys and binary messages: java LDES -mode all
   - Different keys and binary messages:
     pt - plaintext
     ct - ciphertext

   For example:
   java LDES -mode encrypt -key 11 -pt 1101
   java LDES -mode decrypt -key 11 -ct 1100

MDES
--------------------------
Description: MDES is a mini example of a block cipher that has 2 rounds in the Feistel structure similar to LDES.
except that the operation from (I1,I2,I3) to (J1,J2) is based on the S-box diagram
For each key = 00, 01, 10, 11, encrypt the following binary messages = 0000, 1000, 0100, 0010, 0001, 1100, 1010, 1001, 0110, 0101, 0011, 0111, 1011, 1101, 1110, 1111
Instruction:


Q4. Compile file first

    javac Q4Part4.java
    javac Q4Part6.java

    Run program in different modes like:

    For Q4 part 4 and Q4 part 6, run program for all keys and binary messages:

    java Q4Part4 -mode all
    java Q4Part6 -mode all

    For different keys and binary messages:
    pt - plaintext
    ct - ciphertext

    Q4Part4:
    java Q4Part4 -mode encrypt -key 11 -pt 1101
    java Q4Part4 -mode decrypt -key 11 -ct 1101

    Q4Part6:
    To run Q4 Part 6 program in different modes and different hex messages:

    For ECB mode:
    java Q4Part6 -key 10 -mode ECB -encrypt f4a5a32
    java Q4Part6 -key 10 -mode ECB -decrypt 98a5ac1

    For CBC mode:
    java Q4Part6 -key 11 -mode CBC -iv a -encrypt 2a45def
    java Q4Part6 -key 00 -mode CBC -iv a -decrypt 4bfadcc


Q5. Compile file first

    javac Q5.java

    Run program and input the option according to the menu displayed:

    java Q5


Q6. Compile file first

    javac Q6Partb.java

    Run program in different keys and messages like:

    pt - plaintext
    ct - ciphertext

    java Q6Partb -key 3 -encrypt -pt HelloWorld!
    java Q6Partb -key 3 -decrypt -ct RcsmuMrixb!
    java Q6Partb -key 3 -encrypt -pt WOLLONGONG
    java Q6Partb -key 3 -decrypt -ct MQJJ
