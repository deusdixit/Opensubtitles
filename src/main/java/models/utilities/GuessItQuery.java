package models.utilities;

import models.Query;

public class GuessItQuery extends Query {

    public void setFileName(String filename) {
        this.add("filename",filename);
    }

    @Override
    public Query build() {
        return this;
    }
}
