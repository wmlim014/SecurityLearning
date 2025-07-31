import java.io.*;
import java.util.*;
import java.math.BigInteger;
import java.security.*;

/*
 * This java file is to completes the setup of the Host. Target to: 
 * 1. Generate the Diffie-Hellman parameters (p, g)
 * 2. Choose a password "PW" for Bob
 * 3. Save (p, g, H(PW)) into a text file under the directory of Alice
 * Main Reference: https://www.geeksforgeeks.org/computer-networks/implementation-diffie-hellman-algorithm/
 */
public class Setup {
    // 1. Generate the Diffie-Hellman Parameters (p, g)
    // P = random prime number
    // G = a primitive root of P (random int in range 1 < G < P)
    private static int[] generateDHPara() {
        int[] paraPair = new int[2];
        paraPair[0] = generatePrime(); // P
        // G = random int in range 1 < G < P
        final int MAX = paraPair[0] - 1;
        final int MIN = 1;
        paraPair[1] = (int)((Math.random() * (MAX - MIN)) + MIN); // G

        return paraPair;
    }

    // Generate prime number "P"
    // https://stackoverflow.com/questions/24006143/generating-a-random-prime-number-in-java
    private static int generatePrime(){
        final int MAX = 100;
        final int MIN = 1; 
        // Generate a prime number within range min to max
        // https://www.baeldung.com/java-generating-random-numbers-in-range
        int prime = (int)((Math.random() * (MAX - MIN)) + MIN);

        while (!isPrime(prime)) { // While current prime is not prime number
            // Generate random int again
            prime = (int)((Math.random() * (MAX - MIN)) + MIN);
        }
        return prime;
    }
    // Check is the number a prime
    // https://www.mygreatlearning.com/blog/prime-number-program-in-java/
    private static boolean isPrime(int inputNum){
        if (inputNum <= 1) return false;
        if (inputNum == 2) return true;
        if (inputNum % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(inputNum); i += 2) {
            if (inputNum % i == 0) return false;
        }
        return true;
    }

    // Hash the selected password
    // https://www.geeksforgeeks.org/java/sha-1-hash-in-java/
    private static String hashPw(String inputString) {
        try{
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            // digest() method is called to calculate message 
            // digest of the input string returned as array of byte
            byte[] messageDigest = md.digest(inputString.getBytes());
            // Convert byte array into signum representation
            BigInteger num = new BigInteger(1, messageDigest);
            // Convert message digest into hex value
            String hashtext = num.toString(16);
            // Add preceding 0s to make it 40 digits long
            while(hashtext.length() < 40){
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // https://www.geeksforgeeks.org/java/java-program-to-write-into-a-file/
    // Save (p, g, H(PW)) into a text file under the directory of Alice
    private static void writeToFile(String client, int P, int G, String hashedStr){
        try {
            // Get project path dynamically
            // final String DIR_PATH = PROJECT_ROOT + File.separator + "Alice";
            final String PATH = "src/Alice/pwSheets.txt";
            
            // Create directory if missing
            new File("Alice").mkdirs();

            FileWriter writer = new FileWriter(PATH);
            // Write into file
            writer.write(client + ", " + P + ", " + G + ", " + hashedStr + "\n");
            writer.close();
            System.out.println(client + "'s p, g and H(pw) saved successfully in " + PATH);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        // https://www.w3schools.com/java/java_user_input.asp
        // Declare Scanner variable for get user input for Bob's password
        Scanner input = new Scanner(System.in); // Create a Scanner object
        
        int[] publicKeyPair = generateDHPara(); // Generate Diffie-Hellman Parameters
        System.out.println("Generated Diffie-Hellman Parameters: " 
                            + "P = " + publicKeyPair[0] 
                            + ", G = " + publicKeyPair[1] + "\n"); // Debugging line
        
        // 2. Choose a password "PW" for Bob
        System.out.print("Choose a password for Bob: ");
        String pw = input.nextLine(); // Read user input
        String hashedStr = hashPw(pw); // Hash the password with SHA-1
        // 3. Save (p, g, H(PW)) into a text file under the directory of Alice
        writeToFile("Bob", publicKeyPair[0], publicKeyPair[1], hashedStr);
    }
}