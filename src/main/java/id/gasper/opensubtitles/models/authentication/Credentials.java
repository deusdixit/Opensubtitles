package id.gasper.opensubtitles.models.authentication;

/**
 * A class representing user credentials with a username and password.
 * This class is used to store and represent the necessary authentication
 * information for a user.
 */
public class Credentials {

    public String username;
    public String password;

    public Credentials(String username, String password) {
        this.password = password;
        this.username = username;
    }
}
