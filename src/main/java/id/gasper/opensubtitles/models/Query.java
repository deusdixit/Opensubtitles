package id.gasper.opensubtitles.models;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

public abstract class Query {

    static class EmptyQuery extends id.gasper.opensubtitles.models.Query{

        @Override
        public Query build() {
            return this;
        }
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            return value;
        }
    }

    private TreeMap<String,String> data;
    public static Query EMPTY_QUERY = new EmptyQuery();

    public Query() {
        data = new TreeMap<>();
    }

    public void add(String key,String value) {
        data.put(key.toLowerCase(),encodeValue(value.toLowerCase()));
    }

    public String get(String key) {
        key = key.toLowerCase();
        if ( data.containsKey(key)) {
            return data.get(key);
        } else {
            return null;
        }
    }

    public abstract Query build();

    @Override
    public String toString() {
        if ( data.size() <= 0 ) {
            return "";
        }
        String result = "?";
        for(String key : data.keySet()) {
            result += String.format("%s=%s&",key,data.get(key));
        }
        if ( result.endsWith("&")) {
            result = result.substring(0,result.length()-1);
        }
        return result;
    }
}

