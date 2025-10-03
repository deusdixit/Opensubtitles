package id.gasper.opensubtitles.models.features;

/**
 * Enum QueryMatch defines the types of query matching patterns that can be applied
 * when searching for subtitles or media-related data.
 * <p>
 * The possible values are:
 * <p>
 * - START: Matches strings based on whether they start with a particular sequence.
 * - WORD: Matches strings based on full word matches in the query.
 * - EXACT: Matches strings that exactly match the given query input.
 */
public enum QueryMatch {
    START,
    WORD,
    EXACT
}
