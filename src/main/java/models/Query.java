package models;

import java.util.HashMap;

public abstract class Query {

    private HashMap<String,String> data;

    public Query() {
        data = new HashMap<>();
    }

    public void add(String key,String value) {
        data.put(key,value);
    }

    public abstract Query build();

    @Override
    public String toString() {
        String result = "";
        for(String key : data.keySet()) {
            result += String.format("%s=%s&",key,data.get(key));
        }
        if ( result.endsWith("&")) {
            result = result.substring(0,result.length()-1);
        }
        return result;
    }

}
