# Instructions:

Compile the files in cmd (Windows):  

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
     - pt - plaintext
     - ct - ciphertext

    For example:  
	java MDES -mode encrypt -key 11 -pt 1101  
	java MDES -mode decrypt -key 11 -ct 1100


## MDES (ECB & CBC Modes)

**Description:**  
This MDES implemented in two modes: ECB and CBC. Both modes ECB and CBC do
not need message padding and accept a key as 2-bit binary string, a message as hex
string and outputs a ciphertext as a hex string as parameters.

**Instruction:**
1. Compile file: javac MDES2Modes.java
2. Run program with the following parameters for different modes:

   - All keys and binary messages: java MDES2Modes -mode all
   - Different modes and hex messages:

    For example:  
    *ECB mode:*  
    java MDES2Modes -key 10 -mode ECB -encrypt f4a5a32
    java MDES2Modes -key 10 -mode ECB -decrypt 98a5ac1

    *CBC mode:*  
    java MDES2Modes -key 11 -mode CBC -iv a -encrypt 2a45def
    java MDES2Modes -key 00 -mode CBC -iv a -decrypt 4bfadcc
