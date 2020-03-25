# Instructions:

Compile the files in cmd (Windows):

## RSA

**Description:**  
Implementation of RSA signature using Java.

1. KeyGen: The RSA key generations function.
2. Sign: The RSA signing function.
3. Verify: The RSA verication function.

- The key generation function (KeyGen) should take the bit-length (up to 32) of p and q as
input, and output the public key (N, e) and the corresponding private key (N, p, q, d) into two
separate files pk.txt and sk.txt respectively, where p, q are distinct primer numbers, N = p * q
(i.e., N is up to 64 bits), d * e = 1mod((p - 1) * (q - 1)).
- The signing function (Sign) should take the private (secret) key from sk.txt and a message M
(a positive integer smaller than N) from a file msg.txt as input, and output the corresponding
signature S = M^d (mod N) into another file sig.txt.
- The verification function (Verify) should take the public key from pk.txt and a signature S (a
positive integer smaller than N) from sig.txt and the message M as input, and output (display)
the verication result "True" or "False" on the screen (terminal). When your program is
executed, a menu with these three functions should be displayed and a user can choose to
invoke any of these functions for multiple times.

**Instructions:**

1. Compile file: javac RSA.java

2. Run program: java RSA

**Note:**
- Message is only in digit (in msg.txt)
- Option 4 and 5 are optional. They are for RSA encryption and decryption.
- RSA signature scheme are option 2 and 3.
