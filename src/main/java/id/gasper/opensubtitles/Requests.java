package id.gasper.opensubtitles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import id.gasper.opensubtitles.models.Query;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * Class for the HTTP-Requests ( GET,POST,DELETE )
 * The requests get interpreted by the Gson Library and converted into the corresponding classes.
 * @param <T> generic type for the corresponding class
 */
public class Requests<T>{

    private static Gson gsonInstance = null;

    /**
     * Singleton-Function for the Gson Object
     * @return a Gson Object
     */
    public static Gson getGson() {
        if ( gsonInstance == null ) {
            RuntimeTypeAdapterFactory<id.gasper.opensubtitles.models.features.Feature> rta = RuntimeTypeAdapterFactory.of(
                            id.gasper.opensubtitles.models.features.Feature.class, "type", true).registerSubtype(id.gasper.opensubtitles.models.features.Movie.class, "movie")
                    .registerSubtype(id.gasper.opensubtitles.models.features.TvShow.class, "tvshow")
                    .registerSubtype(id.gasper.opensubtitles.models.features.Episode.class, "episode")
                    .registerSubtype(id.gasper.opensubtitles.models.features.Subtitle.class, "subtitle");
            gsonInstance = new GsonBuilder().registerTypeAdapterFactory(rta).create();
        }
        return gsonInstance;
    }

    /**
     * Function for a GET-Request.
     * @param header Map containing the Header-Data
     * @param path The Url
     * @param q The Query
     * @param cls The Wrapper Class
     * @return A Wrapper Class Object
     * @param <T> Wrapper Class Type
     */
    public static <T> T get(Map<String,String> header, String path, Query q, Class<T> cls) throws IOException, InterruptedException {
        Gson gson = getGson();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+path+q.toString()));
        builder.timeout(Duration.ofSeconds(20));
        for(String key : header.keySet()) {
            builder.header(key,header.get(key));
        }
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), cls);
    }

    /**
     * Function for a POST-Request.
     * @param header Map containing the Header-Data
     * @param path The Url
     * @param data The Query-Data
     * @param cls The Wrapper Class
     * @return A Wrapper Class Object
     * @param <T> Wrapper Class Type
     */
    public static <T> T post(Map<String,String> header, String path, String data, Class<T> cls) throws IOException, InterruptedException {
        Gson gson = getGson();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+path));
        builder.timeout(Duration.ofSeconds(20)); // timeout of 20 Seconds
        for(String key : header.keySet()) {
            builder.header(key,header.get(key));
        }
        builder.POST(HttpRequest.BodyPublishers.ofString(data));
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(),cls);
    }

    /**
     * Function for a DELETE-Request.
     * @param header Map containing the Header-Data
     * @param path The Url
     * @param q The Query
     * @param cls The Wrapper Class
     * @return A Wrapper Class Object
     * @param <T> Wrapper Class Type
     */
    public static <T> T delete(Map<String,String> header,String path,Query q,Class<T> cls) throws IOException, InterruptedException {
        Gson gson = getGson();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+path+q.toString()));
        builder.timeout(Duration.ofSeconds(20));
        for(String key : header.keySet()) {
            builder.header(key,header.get(key));
        }
        builder.DELETE();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(),cls);
    }
}
