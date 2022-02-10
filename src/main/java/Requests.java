import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import models.Query;
import models.features.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class Requests<T>{

    private static Gson gsonInstance = null;

    public static Gson getGson() {
        if ( gsonInstance == null ) {
            RuntimeTypeAdapterFactory<Feature> rta = RuntimeTypeAdapterFactory.of(
                            Feature.class, "type", true).registerSubtype(Movie.class, "movie")
                    .registerSubtype(TvShow.class, "tvshow")
                    .registerSubtype(Episode.class, "episode")
                    .registerSubtype(Subtitle.class, "subtitle");
            gsonInstance = new GsonBuilder().registerTypeAdapterFactory(rta).create();
        }
        return gsonInstance;
    }

    public static <T> T get(Map<String,String> header, String path, Query q, Class<T> cls) throws IOException, InterruptedException {
        Gson gson = getGson();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+path+q.toString()));
        for(String key : header.keySet()) {
            builder.header(key,header.get(key));
        }
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), cls);
    }

    public static <T> T post(Map<String,String> header, String path, String data, Class<T> cls) throws IOException, InterruptedException {
        Gson gson = getGson();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+path));
        for(String key : header.keySet()) {
            builder.header(key,header.get(key));
        }
        builder.POST(HttpRequest.BodyPublishers.ofString(data));
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(),cls);
    }

    public static <T> T delete(Map<String,String> header,String path,Query q,Class<T> cls) throws IOException, InterruptedException {
        Gson gson = getGson();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+path+q.toString()));
        for(String key : header.keySet()) {
            builder.header(key,header.get(key));
        }
        builder.DELETE();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(),cls);
    }
}
