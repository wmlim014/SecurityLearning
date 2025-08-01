/* UOW ID: 7894363 */
package Alice;

public class User {
    private String user;
    private int p;
    private int g;
    private String hashedPw;

    // Default constructor
    public User(String user, int p, int g, String hashedPW) {
        this.user = user;
        this.p = p;
        this.g = g;
        this.hashedPw = hashedPW;
    }

    // Other constructor
    public User(User u) {
        this.user = u.getUser();
        this.p = u.getP();
        this.g = u.getG();
        this.hashedPw = u.getHashedPw();
    }

    // Accessor methods
    public String getUser(){
        return this.user;
    }

    public int getP(){
        return this.p;
    }

    public int getG(){
        return this.g;
    }

    public String getHashedPw(){
        return this.hashedPw;
    }

    // Display method
    public String toString() {
        return this.hashedPw + ", " + this.p + ", " + this.g;
    }
}
