package models.discover;

import models.Query;

import java.util.HashSet;

public class DiscoverQuery extends Query {

    private Type t = null;
    private HashSet<String> languages = null;

    public DiscoverQuery() {
        languages = new HashSet<>();
    }

    @Override
    public Query build() {
        if ( languages.size() > 0 ) {
            String lang = String.join(",",languages);
            this.add("languages",lang);
        }
        if ( t != null ) {
            switch (t) {
                case MOVIE : this.add("type","movie");break;
                case TVSHOW: this.add("type","tvshow");break;
                default: break;
            }
        }
        return this;
    }

    public DiscoverQuery setType(Type type) {
        t = type;
        return this;
    }

    public DiscoverQuery addLanguage(String lang) {
        languages.add(lang);
        return this;
    }


    public enum Type {
        MOVIE,TVSHOW
    }
}
