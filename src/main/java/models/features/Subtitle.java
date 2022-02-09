package models.features;

public class Subtitle extends Feature {
    public Attributes attributes;

    public class Attributes {
        public String subtitle_id;
        public String language;
        public int download_count;
        public int new_download_count;
        public boolean hearing_impaired;
        public boolean hd;
        public String format;
        public float fps;
        public int votes;
        public float points;
        public float ratings;
        public boolean from_trusted;
        public boolean foreign_parts_only;
        public boolean ai_translated;
        public boolean machine_translated;
        public String upload_date;
        public String release;
        public String comments;
        public float legacy_subtitle_id;
        public Uploader uploader;
        public FeatureDetails feature_details;
        public String url;
        public RelatedLink[] related_links;
        public FileObject[] files;
        
    }

    public class FeatureDetails {
        public float feature_id;
        public String feature_type;
        public float year;
        public String title;
        public String movie_name;
        public int imdb_id;
        public int tmdb_id;
        public int season_number;
        public int episode_number;
        public int parent_imdb_id;
        public String parent_title;
        public int parent_tmdb_id;
        public int parent_feature_id;
    }

    public class Uploader {
        public float uploader_id;
        public String name;
        public String rank;
    }

    public class RelatedLink {
        public String label;
        public String url;
        public String img_url;
    }

    public class FileObject {
        public int file_id;
        public int cd_number;
        public String file_name;
    }
}