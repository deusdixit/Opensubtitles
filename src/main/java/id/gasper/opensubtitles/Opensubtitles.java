package id.gasper.opensubtitles;

import id.gasper.opensubtitles.exception.OpensubtitlesTimeoutException;
import id.gasper.opensubtitles.models.Query;
import id.gasper.opensubtitles.models.authentication.Credentials;
import id.gasper.opensubtitles.models.authentication.LoginResult;
import id.gasper.opensubtitles.models.authentication.LogoutResult;
import id.gasper.opensubtitles.models.discover.DiscoverResult;
import id.gasper.opensubtitles.models.download.DownloadBody;
import id.gasper.opensubtitles.models.download.DownloadLinkResult;
import id.gasper.opensubtitles.models.features.Feature;
import id.gasper.opensubtitles.models.features.FeatureResult;
import id.gasper.opensubtitles.models.features.Subtitle;
import id.gasper.opensubtitles.models.infos.FormatsResult;
import id.gasper.opensubtitles.models.infos.LanguagesResult;
import id.gasper.opensubtitles.models.infos.UserResult;
import id.gasper.opensubtitles.models.subtitles.SubtitlesResult;
import id.gasper.opensubtitles.models.utilities.GuessItQuery;
import id.gasper.opensubtitles.models.utilities.GuessItResult;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.TreeMap;

import static id.gasper.opensubtitles.Requests.RETRIES;
import static id.gasper.opensubtitles.Requests.SLEEP_BEFORE_RETRY;

public class Opensubtitles {

    private final Credentials credentials;
    private final TreeMap<String, String> header;

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL) // Wichtig für Links, die weiterleiten
            .build();

    /**
     * Constructor
     *
     * @param username <a href="Opensubtitles.com">Opensubtitles</a> username
     * @param password <a href="Opensubtitles.com">Opensubtitles</a> password
     * @param apikey   <a href="Opensubtitles.com">Opensubtitles</a> API-KEY
     */
    public Opensubtitles(String username, String password, String apikey) {
        credentials = new Credentials(username, password);
        header = new TreeMap<>();
        header.put("Content-Type", "application/json");
        header.put("Api-Key", apikey);
        header.put("Accept-Language", "en-US,en;q=0.5");
        header.put("Accept", "*/*");
        header.put("User-Agent", "OpensubtitlesAPI v1.0.1");
    }

    /**
     * Constructor with user-agent
     *
     * @param username  <a href="Opensubtitles.com">Opensubtitles</a> username
     * @param password  <a href="Opensubtitles.com">Opensubtitles</a> password
     * @param apikey    <a href="Opensubtitles.com">Opensubtitles</a> API-KEY
     * @param useragent The User-Agent for the application
     */
    public Opensubtitles(String username, String password, String apikey, String useragent) {
        this(username, password, apikey);
        header.put("User-Agent", useragent);
    }

    /**
     * Logs into Opensubtitles API using the credentials provided in the constructor.
     *
     * @return the response wrapped in a LoginResult Object
     * @see id.gasper.opensubtitles.models.authentication.LoginResult
     */
    public LoginResult login() throws IOException, InterruptedException {
        String data = Requests.getGson().toJson(credentials);
        LoginResult result = Requests.post(header, Endpoints.LOGIN, data, LoginResult.class);
        header.put("Authorization", "Bearer " + result.token);
        return result;
    }

    /**
     * Checks if an authorization token ist set. The token gets set when you successfully log into an account.
     *
     * @return a boolean value. True if a login token is set, false otherwise.
     * @see Opensubtitles#login()
     */
    public boolean isLoggedIn() {
        return header.containsKey("Authorization") && !header.get("Authorization").isEmpty();
    }

    /**
     * Logs out of the Opensubtitles API and removes the authorization token.
     *
     * @return a LogoutResult Object which is a wrapper for the API response.
     * @see id.gasper.opensubtitles.models.authentication.LogoutResult
     */
    public LogoutResult logout() throws IOException, InterruptedException {
        LogoutResult lr = Requests.delete(header, Endpoints.LOGOUT, Query.getEmptyQuery(), LogoutResult.class);
        header.remove("Authorization");
        return lr;
    }

    /**
     * List subtitle formats recognized by the API
     *
     * @return a FormatsResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.infos.FormatsResult
     */
    public FormatsResult getFormats() throws IOException, InterruptedException {
        return Requests.get(header, Endpoints.FORMATS, Query.getEmptyQuery(), FormatsResult.class);
    }

    /**
     * Get the languages information
     *
     * @return a LanguagesResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.infos.LanguagesResult
     */
    public LanguagesResult getLanguages() throws IOException, InterruptedException {
        return Requests.get(header, Endpoints.LANGUAGES, Query.getEmptyQuery(), LanguagesResult.class);
    }

    /**
     * Gather information about the user authenticated. User information are already sent when user is authenticated,
     * and the remaining downloads is returned with each download, but you can also get this information here.
     *
     * @return a UserResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.infos.UserResult
     */
    public UserResult getUserInfo() throws IOException, InterruptedException {
        return Requests.get(header, Endpoints.USER, Query.getEmptyQuery(), UserResult.class);
    }

    /**
     * Lists 60 latest uploaded subtitles
     *
     * @param query Query Parameters
     * @return a DiscoverResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.discover.DiscoverResult
     * @see id.gasper.opensubtitles.models.discover.DiscoverQuery
     */
    public DiscoverResult getLatest(Query query) throws IOException, InterruptedException {
        return Requests.get(header, Endpoints.LATEST, query, DiscoverResult.class);
    }

    /**
     * Discover popular features on opensubtitles.com, according to last 30 days downloads.
     *
     * @param query Query Parameters
     * @return a DiscoverResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.discover.DiscoverResult
     * @see id.gasper.opensubtitles.models.discover.DiscoverQuery
     */
    public DiscoverResult getPopular(Query query) throws IOException, InterruptedException {
        return Requests.get(header, Endpoints.POPULAR, query, DiscoverResult.class);
    }

    /**
     * Discover popular subtitles, according to last 30 days downloads on opensubtitles.com. This list can be filtered by language code or feature type (movie, episode)
     *
     * @param query Query Parameters
     * @return a DiscoverResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.discover.DiscoverResult
     * @see id.gasper.opensubtitles.models.discover.DiscoverQuery
     */
    public DiscoverResult getMostDownloaded(Query query) throws IOException, InterruptedException {
        return Requests.get(header, Endpoints.MOST_DOWNLOADED, query, DiscoverResult.class);
    }

    /**
     * Find subtitle for a video file.
     *
     * @param query a SubtitlesQuery
     * @return a SubtitlesResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.subtitles.SubtitlesQuery
     * @see id.gasper.opensubtitles.models.subtitles.SubtitlesResult
     */
    public SubtitlesResult getSubtitles(Query query) throws IOException, InterruptedException {
        return Requests.get(header, Endpoints.SUBTITLES, query, SubtitlesResult.class);
    }

    /**
     * Search for features using a FeatureQuery
     *
     * @param query a FeatureQuery
     * @return a Feature Array containing the matching Features
     * @see id.gasper.opensubtitles.models.features.FeatureQuery
     * @see id.gasper.opensubtitles.models.features.FeatureResult
     */
    public Feature[] getFeatures(Query query) throws IOException, InterruptedException {
        FeatureResult fr = Requests.get(header, Endpoints.FEATURES, query, FeatureResult.class);
        if (fr == null) {
            return new Feature[0];
        }
        return fr.data;
    }

    /**
     * Extracts as much information as possible from a video filename.
     * <p>
     * It has a very powerful matcher that allows to guess properties from a video using its filename only. This matcher works with both movies and tv shows episodes.
     * <p>
     * This is a simple implementation of the python guessit library. <a href="https://doc.guessit.io">https://doc.guessit.io</a>
     * <p>
     * Find examples of the returned data. <a href="https://doc.guessit.io/properties/">https://doc.guessit.io/properties/</a>
     *
     * @param query GuessItQuery
     * @return a GuessItResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.utilities.GuessItQuery
     * @see id.gasper.opensubtitles.models.utilities.GuessItResult
     */
    public GuessItResult guess(GuessItQuery query) throws IOException, InterruptedException {
        return Requests.get(header, Endpoints.GUESSIT, query, GuessItResult.class);
    }

    /**
     * Request a download url for a subtitle.
     *
     * @param body DownloadBody
     * @return a DownloadLinkResult Object which is a wrapper for the API response
     * @see DownloadBody
     * @see DownloadLinkResult
     */
    public DownloadLinkResult getDownloadLink(DownloadBody body) throws IOException, InterruptedException {
        //String data = gson.toJson(body);
        // The official body isn't working... Not sure why
        String data = "{\"file_id\": " + body.file_id + "}";
        return Requests.post(header, Endpoints.DOWNLOAD, data, DownloadLinkResult.class);
    }

    /**
     * Request a download url for a subtitle.
     *
     * @param subFile FileObject of the subtitle
     * @return a DownloadLinkResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.features.Subtitle.FileObject
     * @see DownloadLinkResult
     */
    public DownloadLinkResult getDownloadLink(Subtitle.FileObject subFile) throws IOException, InterruptedException {
        DownloadBody body = new DownloadBody().setForceDownload(true).setFileId(subFile.file_id).setFileName(subFile.file_name).setSubFormat("srt").setTimeshift(0);
        return getDownloadLink(body);
    }

    public void download(DownloadLinkResult link, Path location) throws OpensubtitlesTimeoutException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(link.link))
                .timeout(Duration.ofSeconds(10)) // Timeout für die gesamte Anfrage
                .build();

        location.getParent().toFile().mkdirs();

        for (int retryCounter = 1; retryCounter <= RETRIES; retryCounter++) {
            try {
                HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(location));

                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    System.out.println("Download erfolgreich! Status: " + response.statusCode());
                    return;
                } else {
                    System.err.println("Download fehlgeschlagen mit HTTP-Status: " + response.statusCode() + ", Versuch " + retryCounter + "/" + RETRIES);
                }

            } catch (HttpTimeoutException e) {
                System.err.println("Timeout! Versuch " + retryCounter + "/" + RETRIES + " für: " + link.link);
            } catch (IOException e) {
                System.err.println("Netzwerkfehler! Versuch " + retryCounter + "/" + RETRIES + ". Fehler: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new OpensubtitlesTimeoutException("Download wurde unterbrochen.", e);
            }

            if (retryCounter < RETRIES) {
                try {
                    Thread.sleep(SLEEP_BEFORE_RETRY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new OpensubtitlesTimeoutException("Wartezeit zwischen Retries wurde unterbrochen.", e);
                }
            }
        }

        throw new OpensubtitlesTimeoutException("Download nach " + RETRIES + " Versuchen endgültig fehlgeschlagen.");
    }


}
