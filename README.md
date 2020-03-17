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


## LDES
**Description:**
LDES is a mini example of a block cipher that has 2 rounds in the Feistel structure
It operates on 4-bit block and 2-bit key. The important feature of LDES is that the S-box has been replaced with a linear operation, which makes the whole cipher linear.
For each key = 00, 01, 10, 11, encrypt the following binary messages = 0000, 1000, 0100, 0010, 0001, 1100, 1010, 1001, 0110, 0101, 0011, 0111, 1011, 1101, 1110, 1111

<p align="center"><img src="/Images/LDES_SBox.jpg" width="400"></p>

**Instruction:**
1. Compile file: javac LDES.java

2. Run program with the following parameters for different modes:
   - All keys and binary messages: java LDES -mode all
   - Different keys and binary messages:
     pt - plaintext
     ct - ciphertext

   For example:
   java LDES -mode encrypt -key 11 -pt 1101
   java LDES -mode decrypt -key 11 -ct 1100

## MDES
**Description:**
MDES is a mini example of a block cipher that has 2 rounds in the Feistel structure similar to LDES except that the operation from (I1,I2,I3) to (J1,J2) is based on the S-box diagram

<table>
      <tr><td>I1I2I3</td><td>000</td><td>001</td><td>010</td><td>011</td><td>100</td><td>101</td><td>110</td><td>111</td></tr>
      <tr><td>J1J2</td><td>00</td><td>00</td><td>00</td><td>01</td><td>00</td><td>00</td><td>10</td><td>11</td></tr>
</table>

For each key = 00, 01, 10, 11, encrypt the following binary messages = 0000, 1000, 0100, 0010, 0001, 1100, 1010, 1001, 0110, 0101, 0011, 0111, 1011, 1101, 1110, 1111

**Instruction:**
1. Compile file: javac MDES.java
2. Run program with the following parameters for different modes:
   - All keys and binary messages: java MDES -mode all
   - Different keys and binary messages:
     pt - plaintext
     ct - ciphertext

   For example:
   java MDES -mode encrypt -key 11 -pt 1101
   java MDES -mode decrypt -key 11 -ct 1100

## MDES (ECB & CBC Modes)
**Description: **
This MDES implemented in two modes: ECB and CBC. Both modes ECB and CBC do
not need message padding and accept a key as 2-bit binary string, a message as hex
string and outputs a ciphertext as a hex string as parameters.


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
