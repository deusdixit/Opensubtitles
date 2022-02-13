package id.gasper.opensubtitles.models.infos;

public class UserResult {

    public Data data;

    public class Data {
        public int allowed_downloads;
        public String level;
        public int user_id;
        public boolean ext_installed;
        public boolean vip;
        public int downloads_count;
        public int remaining_downloads;
    }
}
