import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.authentication.Credentials;
import models.authentication.LoginResult;
import models.authentication.LogoutResult;
import models.discover.PopularQuery;
import models.discover.PopularResult;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
        gson = new GsonBuilder().create();
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

    public PopularResult popular(PopularQuery pQuery) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(Endpoints.BASE+Endpoints.POPULAR + "?" + pQuery.toString()));
        builder.header("Content-Type","application/json");
        builder.header("Api-Key",key);
        builder.header("Authorization",token);
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(),PopularResult.class);
    }

}
