package models.features;

// Welcome8.java

import java.util.Map;

public class Movie extends Feature {
    public Attributes attributes;
    
    public class Attributes {
        public String title;
        public String original_title;
        public String year;
        public Map<String, Long> subtitles_counts;
        public int subtitles_count;
        public int seasons_count;
        public String parent_title;
        public int season_number;
        public EpisodeNumber episode_number;
        public int imdd_id;
        public int tmdb_id;
        public String parent_imdb_id;
        public String feature_id;
        public String[] title_aka;
        public String feature_type;
        public String url;
        public String img_url;

    }

    public class EpisodeNumber {
    }
}