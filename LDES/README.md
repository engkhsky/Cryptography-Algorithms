# Instructions:


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

	  - pt - plaintext

	  - ct - ciphertext

    For example:

    java LDES -mode encrypt -key 11 -pt 1101

	java LDES -mode decrypt -key 11 -ct 1100
