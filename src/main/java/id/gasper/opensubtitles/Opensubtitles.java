package id.gasper.opensubtitles;

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

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.TreeMap;

public class Opensubtitles {

    private final Credentials credentials;
    private final TreeMap<String,String> header;

    /**
     * Constructor
     * @param username <a href="Opensubtitles.com">Opensubtitles</a> username
     * @param password <a href="Opensubtitles.com">Opensubtitles</a> password
     * @param apikey <a href="Opensubtitles.com">Opensubtitles</a> API-KEY
     */
    public Opensubtitles(String username,String password,String apikey) {
        credentials = new Credentials(username,password);
        header = new TreeMap<>();
        header.put("Content-Type","application/json");
        header.put("Api-Key", apikey);
        header.put("Accept-Language","en-US,en;q=0.5");
        header.put("Accept","*/*");
    }

    /**
     * Logs into Opensubtitles API using the credentials provided in the constructor.
     * @return the response wrapped in a LoginResult Object
     * @see id.gasper.opensubtitles.models.authentication.LoginResult
     */
    public LoginResult login() throws IOException, InterruptedException {
        String data = Requests.getGson().toJson(credentials);
        LoginResult result = Requests.<LoginResult>post(header,Endpoints.LOGIN,data,LoginResult.class);
        header.put("Authorization","Bearer " + result.token);
        return result;
    }

    /**
     * Checks if an authorization token ist set. The token gets set when you successfully log into an account.
     * @see Opensubtitles#login()
     * @return a boolean value. True if a login token is set, false otherwise.
     */
    public boolean isLoggedIn() {
        return header.containsKey("Authorization") && header.get("Authorization").length() > 0;
    }

    /**
     * Logs out of the Opensubtitles API and removes the authorization token.
     * @return a LogoutResult Object which is a wrapper for the API response.
     * @see id.gasper.opensubtitles.models.authentication.LogoutResult
     */
    public LogoutResult logout() throws IOException, InterruptedException {
        LogoutResult lr = Requests.<LogoutResult>delete(header,Endpoints.LOGOUT,Query.getEmptyQuery(),LogoutResult.class);
        header.remove("Authorization");
        return lr;
    }

    /**
     * List subtitle formats recognized by the API
     * @return a FormatsResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.infos.FormatsResult
     */
    public FormatsResult getFormats() throws IOException, InterruptedException {
        return Requests.<FormatsResult>get(header,Endpoints.FORMATS,Query.getEmptyQuery(),FormatsResult.class);
    }

    /**
     * Get the languages information
     * @return a LanguagesResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.infos.LanguagesResult
     */
    public LanguagesResult getLanguages() throws IOException, InterruptedException {
        return Requests.<LanguagesResult>get(header,Endpoints.LANGUAGES,Query.getEmptyQuery(),LanguagesResult.class);
    }

    /**
     * Gather information about the user authenticated. User information are already sent when user is authenticated,
     * and the remaining downloads is returned with each download, but you can also get this information here.
     * @return a UserResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.infos.UserResult
     */
    public UserResult getUserInfo() throws IOException, InterruptedException {
        return Requests.<UserResult>get(header,Endpoints.USER,Query.getEmptyQuery(),UserResult.class);
    }

    /**
     * Lists 60 latest uploaded subtitles
     * @param query Query Parameters
     * @return a DiscoverResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.discover.DiscoverResult
     * @see id.gasper.opensubtitles.models.discover.DiscoverQuery
     */
    public DiscoverResult getLatest(Query query) throws IOException, InterruptedException {
        return Requests.<DiscoverResult>get(header,Endpoints.LATEST,query,DiscoverResult.class);
    }

    /**
     * Discover popular features on opensubtitles.com, according to last 30 days downloads.
     * @param query Query Parameters
     * @return a DiscoverResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.discover.DiscoverResult
     * @see id.gasper.opensubtitles.models.discover.DiscoverQuery
     */
    public DiscoverResult getPopular(Query query) throws IOException, InterruptedException {
        return Requests.<DiscoverResult>get(header,Endpoints.POPULAR,query,DiscoverResult.class);
    }

    /**
     * Discover popular subtitles, according to last 30 days downloads on opensubtitles.com. This list can be filtered by language code or feature type (movie, episode)
     * @param query Query Parameters
     * @return a DiscoverResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.discover.DiscoverResult
     * @see id.gasper.opensubtitles.models.discover.DiscoverQuery
     */
    public DiscoverResult getMostDownloaded(Query query) throws IOException, InterruptedException {
        return Requests.<DiscoverResult>get(header,Endpoints.MOST_DOWNLOADED,query,DiscoverResult.class);
    }

    /**
     * Find subtitle for a video file.
     * @param query a SubtitlesQuery
     * @return a SubtitlesResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.subtitles.SubtitlesQuery
     * @see id.gasper.opensubtitles.models.subtitles.SubtitlesResult
     */
    public SubtitlesResult getSubtitles(Query query) throws IOException, InterruptedException {
        return Requests.<SubtitlesResult>get(header,Endpoints.SUBTITLES,query,SubtitlesResult.class);
    }

    /**
     * Search for features using a FeatureQuery
     * @param query a FeatureQuery
     * @return a Feature Array containing the matching Features
     * @see id.gasper.opensubtitles.models.features.FeatureQuery
     * @see id.gasper.opensubtitles.models.features.FeatureResult
     */
    public Feature[] getFeatures(Query query) throws IOException, InterruptedException {
        FeatureResult fr = Requests.<FeatureResult>get(header,Endpoints.FEATURES,query,FeatureResult.class);
        if ( fr == null ) {
            return new Feature[0];
        }
        return fr.data;
    }

    /**
     * Extracts as much information as possible from a video filename.
     *
     * It has a very powerful matcher that allows to guess properties from a video using its filename only. This matcher works with both movies and tv shows episodes.
     *
     * This is a simple implementation of the python guessit library. <a href="https://doc.guessit.io">https://doc.guessit.io</a>
     *
     * Find examples of the returned data. <a href="https://doc.guessit.io/properties/">https://doc.guessit.io/properties/</a>
     * @param query GuessItQuery
     * @return a GuessItResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.utilities.GuessItQuery
     * @see id.gasper.opensubtitles.models.utilities.GuessItResult
     */
    public GuessItResult guess(GuessItQuery query) throws IOException, InterruptedException {
        return Requests.<GuessItResult>get(header,Endpoints.GUESSIT,query,GuessItResult.class);
    }

    /**
     * Request a download url for a subtitle.
     * @param body DownloadBody
     * @return a DownloadLinkResult Object which is a wrapper for the API response
     * @see DownloadBody
     * @see DownloadLinkResult
     */
    public DownloadLinkResult getDownloadLink(DownloadBody body) throws IOException, InterruptedException {
        //String data = gson.toJson(body);
        // The official body isn't working... Not sure why
        String data = "{\"file_id\": " + body.file_id + "}";
        return Requests.<DownloadLinkResult>post(header,Endpoints.DOWNLOAD,data,DownloadLinkResult.class);
    }

    /**
     * Request a download url for a subtitle.
     * @param subFile FileObject of the subtitle
     * @return a DownloadLinkResult Object which is a wrapper for the API response
     * @see id.gasper.opensubtitles.models.features.Subtitle.FileObject
     * @see DownloadLinkResult
     */
    public DownloadLinkResult getDownloadLink(Subtitle.FileObject subFile) throws IOException, InterruptedException {
        DownloadBody body = new DownloadBody().setForceDownload(true).setFileId(subFile.file_id).setFileName(subFile.file_name).setSubFormat("srt").setTimeshift(0);
        return getDownloadLink(body);
    }

    /**
     * Download a subtitle using a DownloadLinkResult Object.
     * @param link DownloadLinkResult
     * @param location The path where the subtitle gets stored
     */
    public void download(DownloadLinkResult link, Path location) throws IOException {
        String save = location.toString();
        InputStream inputStream = new URL(link.link).openStream();
        Paths.get(save).getParent().toFile().mkdirs();
        Files.copy(inputStream, Paths.get(save), StandardCopyOption.REPLACE_EXISTING);
    }


}
