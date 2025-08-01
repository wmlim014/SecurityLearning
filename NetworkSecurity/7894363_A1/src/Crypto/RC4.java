/* UOW ID: 7894363 */
package Crypto;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

// Understanding on crypto library: https://www.geeksforgeeks.org/java/encrypt-and-decrypt-string-file-using-java/
// Understanding on RC4: https://www.geeksforgeeks.org/dsa/implementation-of-rc4-algorithm/
public class RC4 {
    
    // Modified to use H(PW) as key per your protocol
    public static byte[] encryptRC4(String key, byte[] plaintext) {
        try {
            byte[] byteKey = hexStringToByteArray(key);
            Cipher cipher = Cipher.getInstance("RC4");
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, "RC4");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(plaintext);
        } catch (Exception e) {
            throw new RuntimeException("RC4 encryption failed", e);
        }
    }

    public static byte[] decryptRC4(String key, byte[] ciphertext) {
        return encryptRC4(key, ciphertext); // RC4 decryption is same as encryption
    }

    // Convert hex string to byte array (for SHA-1 hashes)
    public static byte[] hexStringToByteArray(String hex) {
        // Remove any whitespace or hyphens
        hex = hex.replaceAll("[\\s-]", "");
        
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have even length");
        }
        
        byte[] data = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            int firstDigit = Character.digit(hex.charAt(i), 16);
            int secondDigit = Character.digit(hex.charAt(i+1), 16);
            
            if (firstDigit == -1 || secondDigit == -1) {
                throw new IllegalArgumentException("Invalid hex digit");
            }
            
            data[i/2] = (byte) ((firstDigit << 4) + secondDigit);
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(byte b: bytes){
            sb.append(String.format("%02x", b));
        }
        return sb.toString().trim();
    }
}