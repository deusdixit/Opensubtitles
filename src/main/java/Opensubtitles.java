import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import models.Query;
import models.authentication.Credentials;
import models.authentication.LoginResult;
import models.authentication.LogoutResult;
import models.discover.DiscoverResult;
import models.discover.DiscoverQuery;
import models.features.*;
import models.infos.FormatsResult;
import models.infos.LanguagesResult;
import models.infos.UserResult;
import models.subtitles.SubtitlesResult;
import models.utilities.GuessItResult;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Opensubtitles {

    private final String key;
    private final Credentials credentials;
    private String token = null;
    private HttpClient client;
    private Gson gson;


    public Opensubtitles(String username,String password,String apikey) {
        credentials = new Credentials(username,password);
        this.key = apikey;
        client = HttpClient.newHttpClient();
        RuntimeTypeAdapterFactory<Feature> rta = RuntimeTypeAdapterFactory.of(
                        Feature.class,"type",true).registerSubtype(Movie.class,"movie")
                .registerSubtype(TvShow.class,"tvshow")
                .registerSubtype(Episode.class,"episode")
                .registerSubtype(Subtitle.class,"subtitle");
        gson = new GsonBuilder().registerTypeAdapterFactory(rta).create();
    }

    public LoginResult login() throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+Endpoints.LOGIN));
        builder.header("Content-Type","application/json");
        builder.header("Api-Key",key);
        String data = gson.toJson(credentials);
        builder.POST(HttpRequest.BodyPublishers.ofString(data));
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        LoginResult result = gson.fromJson(response.body(),LoginResult.class);
        token = result.token;
        return result;
    }

    public LogoutResult logout(String token) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+Endpoints.LOGOUT));
        builder.header("Content-Type","application/json");
        builder.header("Api-Key",key);
        builder.header("Authorization",token);
        builder.DELETE();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(),LogoutResult.class);
    }

    public LogoutResult logout() throws IOException, InterruptedException {
        if ( token != null ) {
            return logout(token);
        }
        return null;
    }

    public FormatsResult getFormats() throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+Endpoints.FORMATS));
        builder.header("Content-Type","application/json");
        builder.header("Api-Key",key);
        builder.header("Authorization",token);
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(),FormatsResult.class);
    }

    public LanguagesResult getLanguages() throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+Endpoints.LANGUAGES));
        builder.header("Content-Type","application/json");
        builder.header("Api-Key",key);
        builder.header("Authorization",token);
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(),LanguagesResult.class);
    }

    public UserResult getUserInfo() throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+Endpoints.USER));
        builder.header("Content-Type","application/json");
        builder.header("Api-Key",key);
        builder.header("Authorization",token);
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(),UserResult.class);
    }

    public DiscoverResult getLatest(Query query) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+Endpoints.POPULAR + "?" + query.toString()));
        builder.header("Content-Type","application/json");
        builder.header("Api-Key",key);
        builder.header("Authorization",token);
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), DiscoverResult.class);
    }

    public DiscoverResult getPopular(Query query) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+Endpoints.POPULAR + "?" + query.toString()));
        builder.header("Content-Type","application/json");
        builder.header("Api-Key",key);
        builder.header("Authorization",token);
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(),DiscoverResult.class);
    }

    public DiscoverResult getMostDownloaded(Query query) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+Endpoints.MOST_DOWNLOADED + "?" + query.toString()));
        builder.header("Content-Type","application/json");
        builder.header("Api-Key",key);
        builder.header("Authorization",token);
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(),DiscoverResult.class);
    }

    public SubtitlesResult getSubtitles(Query query) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+Endpoints.SUBTITLES + "?" + query.toString()));
        builder.header("Content-Type","application/json");
        builder.header("Api-Key",key);
        builder.header("Authorization",token);
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), SubtitlesResult.class);
    }

    public Feature[] getFeatures(Query query) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+Endpoints.FEATURES + "?" + query.toString()));
        builder.header("Content-Type","application/json");
        builder.header("Api-Key",key);
        builder.header("Authorization",token);
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        FeatureResult fr = gson.fromJson(response.body(), FeatureResult.class);
        return fr.data;
    }

    public GuessItResult guess(String filename) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+Endpoints.GUESSIT + "?filename=" + filename));
        builder.header("Content-Type","application/json");
        builder.header("Api-Key",key);
        builder.header("Authorization",token);
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), GuessItResult.class);
    }

}
