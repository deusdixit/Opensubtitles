package id.gasper.opensubtitles.models.subtitles;

import id.gasper.opensubtitles.models.features.Subtitle;

public class SubtitlesResult {
    public int total_pages;
    public int total_count;
    public int page;
    public Subtitle[] data;

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_count() {
        return total_count;
    }

    public int getPage() {
        return page;
    }

    public Subtitle[] getData() {
        return data;
    }
}
