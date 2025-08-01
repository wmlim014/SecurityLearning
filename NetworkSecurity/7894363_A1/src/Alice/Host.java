/* UOW ID: 7894363 */
package Alice;
import Crypto.RC4;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

// Main Reference: https://www.geeksforgeeks.org/java/socket-programming-in-java/
public class Host {
    // Gobals variables for use in functions
    private static final String FILENAME = "src/Alice/pwSheets.txt";
    private static final int MIN = 1; // Set minimum a
    private static final int MAX = 500; // Set maximum a
    static ArrayList<User> users = new ArrayList<User>();

    // Initialize socket and input stream for server-side program
    public static final int PORT = 363; // Select a port for the host
    private Socket s = null; // Client socket
    private ServerSocket ss = null; // Server socket
    private DataInputStream in = null; // Input from client socket
    private DataOutputStream dataOut = null; // Output the data from server to client
    private PrintStream out = null; // Print on client socket

    // Default Constructor with Port
    public Host(int port) {
        readFile(); // Read file 1st before starting the server-side program
        // display(); // Debugging
        // Starts server and waits for a connection
        try {
            ss = new ServerSocket(port);
            System.out.println("Host server started.");
            System.out.println("Waiting for a client...");

            s = ss.accept();
            System.out.println("Client accepted...");

            // Takes input from the client socket
            in = new DataInputStream(
                    new BufferedInputStream(
                        s.getInputStream()
                ));

            dataOut = new DataOutputStream(s.getOutputStream());
            // https://www.geeksforgeeks.org/java/creating-a-socket-to-display-message-to-a-single-client-in-java/
            // Make output to send data to client
            out = new PrintStream(s.getOutputStream());
            // br = new BufferedReader(new InputStreamReader((System.in)));

            String m = "";

            // Reads message from client until "exit" is sent
            while (!m.equals("exit")) {
                try {
                    m = in.readUTF();

                    if(m.equals("exit")) break; // Immediate exit

                    String username = m.toLowerCase(); // Convert user input to lower case
                    User currUsr = validateUsername(username);

                    if(currUsr != null) {
                        // Waiting for password in
                        String pw = in.readUTF(); 
                        String hashedPw = hashPw(pw);
                        out.println("Password received, generating key for Diffie-Hellman Exchange...");
                        out.flush();

                        // Generate random a 
                        int a = randomA();
                        // System.out.println("Random 'a' generated: " + a);
                        // let g^a mod p = na
                        long na = power(currUsr.getG(), a, currUsr.getP());

                        if (hashedPw.equals(currUsr.getHashedPw())) {
                            dataOut.writeUTF(currUsr.getHashedPw());
                            dataOut.writeInt(currUsr.getP());
                            dataOut.writeInt(currUsr.getG());
                            dataOut.flush();
                            
                            // Start encryption
                            // https://www.geeksforgeeks.org/java/bytearrayoutputstream-tobytearray-method-in-java-with-examples/
                            // Process encrypt: E(H(PW), p, g, g^a mod p)
                            // Convert p to bytes
                            BigInteger p = BigInteger.valueOf(currUsr.getP());
                            byte[] pBytes = p.toByteArray();
                            // Convert g to bytes
                            BigInteger g = BigInteger.valueOf(currUsr.getG());
                            byte[] gBytes = g.toByteArray();
                            // Convert na to bytes
                            BigInteger naBigInteger = BigInteger.valueOf(na);
                            byte[] naBytes = naBigInteger.toByteArray();

                            // Concatenate the parameters
                            // https://www.geeksforgeeks.org/java/bytearrayoutputstream-tobytearray-method-in-java-with-examples/
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            try {
                                bos.write(pBytes);
                                bos.write(gBytes);
                                bos.write(naBytes);
                            
                                byte[] byteToEnc = bos.toByteArray();
                                // Use user hashed password as key
                                byte[] encrypted2 = RC4.encryptRC4(currUsr.getHashedPw(), byteToEnc);
                                dataOut.writeInt(encrypted2.length);
                                dataOut.write(encrypted2);
                                dataOut.flush();
                                System.out.println("2:\tA -> B: E(H(PW), p, g, g^a mod p): " + RC4.bytesToHex(encrypted2)); 
                            } catch (Exception e) {
                                System.out.println(e);
                            }

                            int b = in.readInt();

                            int length = in.readInt();
                            byte[] encStr3 = new byte[length];
                            in.readFully(encStr3);
                            System.out.println("3:\tB -> A: E(H(PW), g^b mod p): " + RC4.bytesToHex(encStr3));
                            
                            long ab = a * b;
                            long k = power(currUsr.getG(), ab, currUsr.getP());
                            String hashedK = hashPw(String.valueOf(k));
                            dataOut.writeUTF(hashedK);
                            dataOut.flush();
                            
                            // Process on A -> B: E(K, Na)
                            byte[] encStr4 = RC4.encryptRC4(hashedK, encStr3);
                            dataOut.writeInt(encStr4.length);
                            dataOut.write(encStr4);
                            dataOut.flush();
                            System.out.println("4:\tA -> B: E(K, Na): " +  RC4.bytesToHex(encStr4));
                            
                            // Process on B -> A: E(K, Na + 1, Nb)
                            length = in.readInt();
                            byte[] encStr5 = new byte[length];
                            in.readFully(encStr5);
                            System.out.println("5:\tB -> A: E(K, Na + 1, Nb): " + RC4.bytesToHex(encStr5));
                            
                            // Process on A -> B: E(K, Nb+1)
                            byte[] encStr6 = RC4.encryptRC4(hashedK, encStr5);
                            dataOut.writeInt(encStr6.length);
                            dataOut.write(encStr6);
                            dataOut.flush();
                            System.out.println("6:\tA -> B: E(K, Nb + 1): " + RC4.bytesToHex(encStr6));
                        }
                    }
                } catch (IOException i) {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection...");

            // Close connection
            s.close();
            in.close();
            System.out.println("Connection Closed Successfully");
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    // Reads parameters and the hashed password from the file
    // https://www.w3schools.com/java/java_files_read.asp
    private void readFile() {
        try {
            File file = new File(FILENAME);
            Scanner read = new Scanner(file);

            while(read.hasNextLine()){ // While not empty line
                String line = read.nextLine();
                // System.out.println(line);   // Debugging
                String[] data = line.split(", ");

                String user = data[0].trim();
                int p = Integer.valueOf(data[1].trim());
                int g = Integer.valueOf(data[2].trim());
                String hashedPw = data[3].trim();
                // Declare User
                User u = new User(user, p, g, hashedPw);
                users.add(u); // Add into array list
            }
            read.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    // Validate user
    private User validateUsername(String input) {
        User currentUser = null;
        for(User u: users) { // For each user
            if(input.equals(u.getUser().toLowerCase())){ // If current user = input
                currentUser = new User(u); // return user and store in server
                break;
            }
        }

        if (currentUser != null) {
            // Send output to client
            out.println("Welcome " + currentUser.getUser());
            out.flush(); // Ensure message send immediately

            System.out.println("Waiting for " + currentUser.getUser() + " input password...");
        }
        return currentUser;
    }

    // Generate random a
    private int randomA(){
        return (int)((Math.random() * (MAX - MIN)) + MIN);
    }

    // Power function to return value of g^a mod p
    private static long power(long g, long a, long p){
        if(a == 1) return a;
        else 
            return (((long)Math.pow(g, a)) % p);
    }

    // Hash the selected password
    // https://www.geeksforgeeks.org/java/sha-1-hash-in-java/
    public static String hashPw(String inputString) {
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

    // Debugging user method
    private void display() {
        for(User u: users){
            System.out.println(u.toString());
        }
    }

    public static void main(String[] args) throws Exception {
        Host h = new Host(PORT);
    }
}