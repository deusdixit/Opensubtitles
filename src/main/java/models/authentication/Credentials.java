package models.authentication;

public class Credentials {

    public String username;
    public String password;

    public Credentials(String username,String password) {
        this.password = password;
        this.username = username;
    }
}
