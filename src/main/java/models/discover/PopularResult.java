package models.discover;

import java.util.ArrayList;

public class PopularResult {
    public Feature[] data;

    public class Feature {
        public String id;
        public String type;
        Attributes attributes;
    }

    public class Attributes {
        public String subtitle_id;
        public String language;
        public float download_count;
        public float new_download_count;
        public boolean hearing_impaired;
        public boolean hd;
        public String format;
        public float fps;
        public float votes;
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
        public String url;
        Uploader UploaderObject;
        FeatureDetails feature_details;
        ArrayList<Object> related_links = new ArrayList<Object>();
        FileObject[] files;

    }

    public class FileObject {
        public int file_id;
        public int cd_number;
        public String file_name;
    }

    public class FeatureDetails {
        public float feature_id;
        public String feature_type;
        public float year;
        public String title;
        public String movie_name;
        public float imdb_id;
        public float tmdb_id;

    }

    public class Uploader {
        public float uploader_id;
        public String name;
        public String rank;

    }
}