package id.gasper.opensubtitles.models.features;

import java.util.Map;

public class TvShow extends Feature {
    public Attributes attributes;

    public class Attributes {
        public String title;
        public String original_title;
        public String year;
        public int imdb_id;
        public int tmdb_id;
        public String[] title_aka;
        public String feature_id;
        public String url;
        public String img_url;
        public Map<String, Long> subtitles_counts;
        public int subtitles_count;
        public Season[] seasons;
    }


    public class Season {
        public int season_number;
        public Episode[] episodes;
    }

    public class Episode {
        public int episode_number;
        public String title;
        public int feature_id;
        public int feature_imdb_id;
    }
}