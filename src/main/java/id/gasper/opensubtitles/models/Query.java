package id.gasper.opensubtitles.models;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

public abstract class Query {

    private final TreeMap<String, String> data;

    private static class EmptyQuery extends id.gasper.opensubtitles.models.Query {
        @Override
        public Query build() {
            return this;
        }
    }

    private String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public static Query getEmptyQuery() {
        return new EmptyQuery();
    }

    public Query() {
        data = new TreeMap<>();
    }

    public void add(String key, String value) {
        data.put(key.toLowerCase(), encodeValue(value.toLowerCase()));
    }

    public String get(String key) {
        key = key.toLowerCase();
        return data.getOrDefault(key, null);
    }

    public abstract Query build();

    @Override
    public String toString() {
        if (data.size() <= 0) {
            return "";
        }
        StringBuilder result = new StringBuilder("?");
        for (String key : data.keySet()) {
            result.append(String.format("%s=%s&", key, data.get(key)));
        }
        if (result.toString().endsWith("&")) {
            result = new StringBuilder(result.substring(0, result.length() - 1));
        }
        return result.toString();
    }
}

