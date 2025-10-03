package id.gasper.opensubtitles.models.features;

import id.gasper.opensubtitles.models.Query;

/**
 * The FeatureQuery class provides methods to construct and manage
 * query parameters for searching features such as movies, TV shows, or episodes.
 * This class extends the abstract Query class and allows setting various parameters
 * like feature ID, IMDB ID, TMDB ID, query values, and more, to build a comprehensive query.
 */
public class FeatureQuery extends Query {

    /**
     * Sets the feature ID parameter for the query.
     * The feature ID is used to identify specific features (e.g., movies, TV shows, or episodes).
     *
     * @param id the numerical ID representing the feature
     * @return the updated FeatureQuery instance with the feature ID set
     */
    public FeatureQuery setFeatureId(int id) {
        this.add("feature_id", String.valueOf(id));
        return this;
    }

    /**
     * IMDB ID, delete leading zeroes
     *
     * @param id IMDB ID, delete leading zeroes
     * @return Feature Query
     */
    public FeatureQuery setImdbId(String id) {
        id = id.replaceAll("tt0*", "");
        this.add("imdb_id", id);
        return this;
    }

    /**
     * Sets the query parameter for the search operation.
     * The query parameter is used to specify search terms for looking up features
     * such as movies, TV shows, or episodes based on a textual search.
     *
     * @param value the search term or query string to set
     * @return the updated FeatureQuery instance with the query parameter set
     */
    public FeatureQuery setQuery(String value) {
        this.add("query", value);
        return this;
    }

    /**
     * Sets the query match parameter for the search operation.
     * The query match parameter determines the type of matching
     * applied to the search query, such as starting matches,
     * word matches, or exact matches.
     *
     * @param queryMatch the type of query match, represented by the {@link QueryMatch} enum
     * @return the updated FeatureQuery instance with the query match parameter set
     */
    public FeatureQuery setQueryMatch(QueryMatch queryMatch) {
        this.add("query_match", queryMatch.name().toLowerCase());
        return this;
    }

    /**
     * TheMovieDB ID - combine with type to avoid errors
     *
     * @param id TheMovieDB ID
     * @return Feature Query
     */
    public FeatureQuery setTmdbId(String id) {
        this.add("tmdb_id", String.valueOf(id));
        return this;
    }

    /**
     * Sets the type parameter for the feature query.
     * The type parameter specifies the category of the feature,
     * such as a movie, TV show, or episode.
     *
     * @param t the feature type to set, represented by the {@code Type} enum
     *          (e.g., {@code Type.MOVIE}, {@code Type.TVSHOW}, {@code Type.EPISODE})
     * @return the updated {@code FeatureQuery} instance with the type parameter set
     */
    public FeatureQuery setType(Type t) {
        switch (t) {
            case MOVIE:
                this.add("type", "movie");
                break;
            case TVSHOW:
                this.add("type", "tvshow");
                break;
            case EPISODE:
                this.add("type", "episode");
                break;
            default:
                break;
        }
        return this;
    }

    /**
     * Sets the year parameter for the query.
     * The year parameter is used to specify the release year of features
     * such as movies, TV shows, or episodes.
     *
     * @param year the release year of the feature to set
     * @return the updated FeatureQuery instance with the year parameter set
     */
    public FeatureQuery setYear(int year) {
        this.add("year", String.valueOf(year));
        return this;
    }

    @Override
    public Query build() {
        return this;
    }

}
