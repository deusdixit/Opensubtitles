package id.gasper.opensubtitles.models.subtitles;

/**
 * The OrderOptions enumeration defines a set of criteria that can be used to order
 * the results when querying subtitles. These options represent various aspects
 * or attributes of subtitles that can serve as sorting keys during data fetching.
 */
public enum OrderOptions {
    AI_TRANSLATED,
    EPISODE_NUMBER,
    FOREIGN_PARTS_ONLY,
    HEARING_IMPAIRED,
    ID,
    IMDB_ID,
    LANGUAGES,
    MACHINE_TRANSLATED,
    DOWNLOAD_COUNT
}
