package id.gasper.opensubtitles.models.authentication;

public class LoginResult {
    public User user;
    public String token;
    public int status;

    public class User {
        public int allowed_downloads;
        public String level;
        public int user_id;
        public boolean ext_installed;
        public boolean vip;
    }
}

