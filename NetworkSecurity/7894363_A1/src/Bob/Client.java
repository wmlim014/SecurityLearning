/* UOW ID: 7894363 */
package Bob;
import Crypto.RC4;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Base64;

public class Client {
    // Initialize socket and input/output streams
    private static final String IP = "127.0.0.1";
    private static final int PORT = 363;
    private static final int MIN = 1; // Set minimum a
    private static final int MAX = 500; // Set maximum a

    // Initialize socket and input stream for client-side program
    private Socket s = null; // Client socket
    private DataInputStream in = null; // Client input from terminal
    private DataInputStream dataIn = null; // Get input from server socket
    private DataOutputStream out = null; // To sent output to server socket
    private BufferedReader serverIn = null; // To receive server input

    // Default Constructor
    public Client(String addr, int port) {
        // Establish a connection
        try {
            s = new Socket(addr, port);
            System.out.println("Connected to server successfully...");
            System.out.print("Enter your username: ");

            // Takes input from terminal
            in = new DataInputStream(System.in);

            // Get data input from server socket
            dataIn = new DataInputStream(s.getInputStream());

            // Sends output to the server socket
            out = new DataOutputStream(s.getOutputStream());
            
            // Receive server input
            serverIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }

        // String to read message from input
        String m = "";

        // Keep reading until "exit" is input
        while (!m.equals("exit")) {
            try {
                m = in.readLine();
                out.writeUTF(m);
                out.flush(); // Ensure message sent immediately

                System.out.println("1:\tB -> A: " + m);

                String serverFoundUsr = serverIn.readLine();
                System.out.println("\n" + serverFoundUsr); // Print server response

                System.out.print("Please input your password: ");
                String pw = in.readLine(); // Waiting for user input password
                out.writeUTF(pw); // Send password to socket
                out.flush();
                
                System.out.println(serverIn.readLine());

                String hashedPw = dataIn.readUTF();
                int p = dataIn.readInt();
                int g = dataIn.readInt();
                // int p = Integer.parseInt(serverIn.readLine());
                // int g = Integer.parseInt(serverIn.readLine());
                // System.out.println(hashedPw + ", " + p + ", " + g);
                // Read encrypted data: E(H(PW), p, g, g^a mod p)
                int length = dataIn.readInt();
                byte[] encStr2 = new byte[length]; 
                dataIn.readFully(encStr2);
                System.out.println("2:\tA -> B: E(H(PW), p, g, g^a mod p): " + RC4.bytesToHex(encStr2)); 
                
                // Decrypt the received
                // Test vector
                byte[] decStr2 = RC4.decryptRC4(hashedPw, encStr2);
                // System.out.println(RC4.bytesToHex(decStr2));

                // Process to send: E(H(PW), g^b mod p)
                int b = randomB();
                out.writeInt(b);
                long nb = power(g, b, p);
                // Convert nb to bytes
                BigInteger nbBigInteger = BigInteger.valueOf(nb);
                byte[] nbBytes = nbBigInteger.toByteArray();

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    bos.write(nbBytes);
                    byte[] byteToEnc = bos.toByteArray();
                    // Use user hashed password as key
                    byte[] encStr3 = RC4.encryptRC4(hashedPw, byteToEnc);
                    out.writeInt(encStr3.length);
                    out.write(encStr3);
                    out.flush();

                    System.out.println("3:\tB -> A: E(H(PW), g^b mod p): " + RC4.bytesToHex(encStr3));
                    String hashedK = dataIn.readUTF();
                    
                    length = dataIn.readInt();
                    byte[] encStr4 = new byte[length];
                    dataIn.readFully(encStr4);
                    System.out.println("4:\tA -> B: E(K, Na): " +  RC4.bytesToHex(encStr4));
                    
                    // Process on B -> A: E(K, Na + 1, Nb)
                    // Reset bos
                    bos = new ByteArrayOutputStream();
                    // Concatenate the parameters
                    bos.writeBytes(encStr3);
                    bos.writeBytes(encStr4);
                    byteToEnc = bos.toByteArray();
                    byte[] encStr5 = RC4.encryptRC4(hashedK, byteToEnc);
                    out.writeInt(encStr5.length);
                    out.write(encStr5);
                    out.flush();
                    System.out.println("5:\tB -> A: E(K, Na + 1, Nb): " + RC4.bytesToHex(encStr5));
                    
                    // Process on A -> B: E(K, Nb+1)
                    length = dataIn.readInt();
                    byte[] encStr6 = new byte[length];
                    dataIn.readFully(encStr6);
                    System.out.println("6:\tA -> B: E(K, Nb + 1): " +  RC4.bytesToHex(encStr6));
                } catch (Exception e) {
                    System.out.println(e);
                }
            } catch (IOException i) {
                System.out.println(i);
            }
        }
        System.out.println("Existing...");

        // Close the connection
        try {
            in.close();
            out.close();
            s.close();
        } catch (IOException i) {
            System.out.println(i);
        }
        System.out.println("Existed Successfully");
    }

    // Generate random b
    private int randomB(){
        return (int)((Math.random() * (MAX - MIN)) + MIN);
    }

    // Power function to return value of g^a mod p
    private static long power(long g, long b, long p){
        if(b == 1) return b;
        else 
            return (((long)Math.pow(g, b)) % p);
    }
    
    public static void main(String[] args) {
        Client c = new Client(IP, PORT);
    }
}
