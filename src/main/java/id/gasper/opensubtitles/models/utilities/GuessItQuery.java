package id.gasper.opensubtitles.models.utilities;

import id.gasper.opensubtitles.models.Query;

public class GuessItQuery extends Query {

    public void setFileName(String filename) {
        this.add("filename", filename);
    }

    @Override
    public Query build() {
        return this;
    }
}
