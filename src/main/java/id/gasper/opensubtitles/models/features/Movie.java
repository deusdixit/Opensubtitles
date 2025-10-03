package id.gasper.opensubtitles.models.features;

import java.util.Map;

/**
 * Represents a movie and its associated metadata attributes.
 * This class extends the {@code Feature} class and contains a nested {@code Attributes} class,
 * which holds detailed information about the movie.
 */
public class Movie extends Feature {
    public Attributes attributes;

    public Attributes getAttributes() {
        return attributes;
    }

    public class Attributes {
        public String title;
        public String original_title;
        public String year;
        public Map<String, Long> subtitles_counts;
        public Integer subtitles_count;
        public Integer seasons_count;
        public String parent_title;
        public Integer season_number;
        public EpisodeNumber episode_number;
        public Integer imdb_id;
        public Integer tmdb_id;
        public String parent_imdb_id;
        public String feature_id;
        public String[] title_aka;
        public String feature_type;
        public String url;
        public String img_url;

        public String getTitle() {
            return title;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public String getYear() {
            return year;
        }

        public Map<String, Long> getSubtitles_counts() {
            return subtitles_counts;
        }

        public Integer getSubtitles_count() {
            return subtitles_count;
        }

        public Integer getSeasons_count() {
            return seasons_count;
        }

        public String getParent_title() {
            return parent_title;
        }

        public Integer getSeason_number() {
            return season_number;
        }

        public EpisodeNumber getEpisode_number() {
            return episode_number;
        }

        public Integer getImdb_id() {
            return imdb_id;
        }

        public Integer getTmdb_id() {
            return tmdb_id;
        }

        public String getParent_imdb_id() {
            return parent_imdb_id;
        }

        public String getFeature_id() {
            return feature_id;
        }

        public String[] getTitle_aka() {
            return title_aka;
        }

        public String getFeature_type() {
            return feature_type;
        }

        public String getUrl() {
            return url;
        }

        public String getImg_url() {
            return img_url;
        }
    }

    public class EpisodeNumber {
    }
}