import java.io.*;
import java.util.*;
import java.util.regex.*;
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
    
    // Gobals variables for use in functions
    // File path to allocate the password sheet
    // In Window VS Code: "src/Alice/pwSheets.txt"
    // In Lunix VM: "Alice/pwSheets.txt"
    final private static String FILE_PATH = "src/Alice/pwSheets.txt";
    // Regex to check if the password contained at least 6 alphanumeric characters
    // Pattern Check: https://www.w3schools.com/tags/att_input_pattern.asp
    // Main Reference: https://www.geeksforgeeks.org/dsa/how-to-check-string-is-alphanumeric-or-not-using-regular-expression/
    final private static String REGEX = "^[A-Za-z0-9]{6,}$"; 
    
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

    // Function to check is the password meet requrements
    // Contained at least 6 alphanumeric characters characters 
    // (A-Z, both uppercase and lowercase) and numbers (0-9)
    // https://www.geeksforgeeks.org/dsa/how-to-check-string-is-alphanumeric-or-not-using-regular-expression/
    private static boolean isPwMeetReq(String inputString) {
        // If input string is empty, return false
        if (inputString == null) return false;
        
        Pattern p = Pattern.compile(REGEX);
        // Pattern class contains matcher() method to: 
        // find matching between given string and
        // regular expression
        Matcher m = p.matcher(inputString);
        return m.matches(); // return if the string matched the ReGex
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
            // Create directory if missing
            new File("Alice").mkdirs();

            FileWriter writer = new FileWriter(FILE_PATH);
            // Write into file
            writer.write(client + ", " + P + ", " + G + ", " + hashedStr + "\n");
            writer.close();
            System.out.println(client + "'s p, g and H(pw) saved successfully in " + FILE_PATH);
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

        while(!isPwMeetReq(pw)) {   // While the selected password does not meet requirement...
            // Prompt error
            System.out.println("The password must contained at least 6 alphanumeric characters...");
            // Prompt user input again
            System.out.print("Choose a password for Bob: ");
            pw = input.nextLine(); // Read user input
        }

        String hashedStr = hashPw(pw); // Hash the password with SHA-1
        // 3. Save (p, g, H(PW)) into a text file under the directory of Alice
        writeToFile("Bob", publicKeyPair[0], publicKeyPair[1], hashedStr);
        System.out.println("Host setup completed, please proceed to Host and Client terminal for the rest execution.");
        System.exit(0); // Terminate Program
    }
}