package id.gasper.opensubtitles.models.features;

import java.util.Map;

public class Episode extends Feature {

    public Attributes attributes;

    public class Attributes {
        public String title;
        public String original_title;
        public String year;
        public String parent_imdb_id;
        public String parent_title;
        public int season_number;
        public int episode_number;
        public int imdb_id;
        public int tmdb_id;
        public String[] title_aka;
        public String feature_id;
        public String url;
        public String img_url;
        public Map<String, Long> subtitles_counts;
        public int subtitles_count;
    }

}