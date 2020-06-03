package chatserver;

import java.math.BigInteger;
import java.util.Random;
 
public class RSA
{
    private final BigInteger p, q, n, phi, e, d;
    private final Random random;
    private final int bitlength;
 
    public RSA (int bitLength)
    {
        bitlength = bitLength;
        random = new Random();                                                  // Crea un numero random R
        p = BigInteger.probablePrime(bitlength, random);                        // Probabile numero primo, segreto
        q = BigInteger.probablePrime(bitlength, random);                        // Probabile numero primo, segreto
        n = p.multiply(q);                                                      // Prima parte della chiave pubblica
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));  // phi = (p-1)(q-1)
        e = BigInteger.probablePrime(bitlength / 2, random);                    // Seconda parte della chiave pubblica
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
            e.add(BigInteger.ONE);                                              // Finché MCD(phi, e) > 1 ed e < phi, e += 1
        }
        d = e.modInverse(phi);                                                  // (1/e) * mod phi, chiave privata
    }
 
    /**
     * Method: encrypt
     * It encrypts the message string passed as parameter.
     * @param message
     * @return 
     */
    public byte[] encrypt(byte[] message)
    {
        BigInteger bigint, num;
        bigint = new BigInteger(message);
        num = bigint.modPow(e, n);
        return num.toByteArray();
    }
    
     /**
     * Metodo: encrypt
     * Ritorna un array di byte criptato del messaggio passato come parametro.
     * @param message (byte[]): stringa in array di byte da criptare.
     * @param numberE (BigInteger): parametro E della chiave pubblica del destinatario.
     * @param numberN (Biginteger): parametro N della chiave del destinatario.
     * @return byte[]: array di byte criptato.
     */
    public byte[] encrypt(byte[] message, BigInteger numberE, BigInteger numberN)
    {
        BigInteger bigint, num;
        bigint = new BigInteger(message);
        num = bigint.modPow(numberE, numberN);
        return num.toByteArray();
    }
 
    /**
     * Method: decrypt
     * It decrypts the message string passed as parameter.
     * @param message
     * @return 
     */
    public byte[] decrypt(byte[] message)
    {
        BigInteger bigint, num;
        bigint = new BigInteger(message);
        num = bigint.modPow(d, n);
        return num.toByteArray();
    }
    
    /**
     * Metodo: decrypt
     * Ritorna un array di byte decriptato del messaggio passato come parametro.
     * @param message (byte[]): stringa in array di byte da criptare.
     * @param numberD (BigInteger): parametro E della chiave privata di se stesso.
     * @param numberN (BigInteger): parametro N della chiave del destinatario.
     * @return byte[]: stringa decriptata.
     */
    public byte[] decrypt(byte[] message, BigInteger numberD, BigInteger numberN)
    {
        BigInteger bigint, num;
        bigint = new BigInteger(message);
        num = bigint.modPow(numberD, numberN);
        return num.toByteArray();
    }
    
    public BigInteger getE() {
        return this.e;
    }
    
    public BigInteger getD() {
        return this.d;
    }
    
    public BigInteger getN() {
        return this.n;
    }
}