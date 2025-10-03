package id.gasper.opensubtitles.models.features;

/**
 * The Subtitle class represents the metadata and attributes associated with
 * a subtitle. This class extends the Feature class and provides details
 * related to a specific subtitle, including its attributes and related data
 * about the feature, uploader, and associated files or links.
 */
public class Subtitle extends Feature {
    public Attributes attributes;

    public Attributes getAttributes() {
        return attributes;
    }

    public class Attributes {
        public String subtitle_id;
        public String language;
        public Integer download_count;
        public Integer new_download_count;
        public boolean hearing_impaired;
        public boolean hd;
        public String format;
        public float fps;
        public Integer votes;
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

        public String getSubtitle_id() {
            return subtitle_id;
        }

        public String getLanguage() {
            return language;
        }

        public Integer getDownload_count() {
            return download_count;
        }

        public Integer getNew_download_count() {
            return new_download_count;
        }

        public boolean isHearing_impaired() {
            return hearing_impaired;
        }

        public boolean isHd() {
            return hd;
        }

        public String getFormat() {
            return format;
        }

        public float getFps() {
            return fps;
        }

        public Integer getVotes() {
            return votes;
        }

        public float getPoints() {
            return points;
        }

        public float getRatings() {
            return ratings;
        }

        public boolean isFrom_trusted() {
            return from_trusted;
        }

        public boolean isForeign_parts_only() {
            return foreign_parts_only;
        }

        public boolean isAi_translated() {
            return ai_translated;
        }

        public boolean isMachine_translated() {
            return machine_translated;
        }

        public String getUpload_date() {
            return upload_date;
        }

        public String getRelease() {
            return release;
        }

        public String getComments() {
            return comments;
        }

        public float getLegacy_subtitle_id() {
            return legacy_subtitle_id;
        }

        public Uploader getUploader() {
            return uploader;
        }

        public FeatureDetails getFeature_details() {
            return feature_details;
        }

        public String getUrl() {
            return url;
        }

        public RelatedLink[] getRelated_links() {
            return related_links;
        }

        public FileObject[] getFiles() {
            return files;
        }
    }

    public class FeatureDetails {
        public Integer feature_id;
        public String feature_type;
        public Integer year;
        public String title;
        public String movie_name;
        public Integer imdb_id;
        public int tmdb_id;
        public Integer season_number;
        public Integer episode_number;
        public Integer parent_imdb_id;
        public String parent_title;
        public Integer parent_tmdb_id;
        public Integer parent_feature_id;

        public Integer getFeature_id() {
            return feature_id;
        }

        public String getFeature_type() {
            return feature_type;
        }

        public Integer getYear() {
            return year;
        }

        public String getTitle() {
            return title;
        }

        public String getMovie_name() {
            return movie_name;
        }

        public Integer getImdb_id() {
            return imdb_id;
        }

        public int getTmdb_id() {
            return tmdb_id;
        }

        public Integer getSeason_number() {
            return season_number;
        }

        public Integer getEpisode_number() {
            return episode_number;
        }

        public Integer getParent_imdb_id() {
            return parent_imdb_id;
        }

        public String getParent_title() {
            return parent_title;
        }

        public Integer getParent_tmdb_id() {
            return parent_tmdb_id;
        }

        public Integer getParent_feature_id() {
            return parent_feature_id;
        }
    }

    public class Uploader {
        public float uploader_id;
        public String name;
        public String rank;

        public float getUploader_id() {
            return uploader_id;
        }

        public String getName() {
            return name;
        }

        public String getRank() {
            return rank;
        }
    }

    public class RelatedLink {
        public String label;
        public String url;
        public String img_url;

        public String getLabel() {
            return label;
        }

        public String getUrl() {
            return url;
        }

        public String getImg_url() {
            return img_url;
        }
    }

    public class FileObject {
        public Integer file_id;
        public Integer cd_number;
        public String file_name;

        public Integer getFile_id() {
            return file_id;
        }

        public Integer getCd_number() {
            return cd_number;
        }

        public String getFile_name() {
            return file_name;
        }
    }
}