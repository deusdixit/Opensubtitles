package models.discover;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

public class PopularQuery {
    private Type t = null;
    private HashSet<String> languages = null;

    public PopularQuery() {
        languages = new HashSet<>();
    }

    public PopularQuery setType(Type type) {
        t = type;
        return this;
    }

    public PopularQuery addLanguage(String lang) {
        languages.add(lang);
        return this;
    }

    @Override
    public String toString() {
        String result = "";
        if ( languages.size() > 0 ) {
            String langStr = "";
            for(String l : languages) {
                langStr += l + ",";
            }
            result += "languages=" + langStr.substring(0,langStr.length()-1);
        }
        if ( t != null ) {
            switch (t) {
                case MOVIE: result += "&type=movie"; break;
                case TVSHOW: result += "&type=tvshow"; break;
                default: break;
            }
        }
        return URLEncoder.encode(result, StandardCharsets.UTF_8);
    }

    public enum Type {
        MOVIE,TVSHOW
    }
}
