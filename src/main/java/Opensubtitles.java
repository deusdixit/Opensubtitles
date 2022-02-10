import models.Query;
import models.authentication.Credentials;
import models.authentication.LoginResult;
import models.authentication.LogoutResult;
import models.discover.DiscoverResult;
import models.download.DownloadBody;
import models.download.DownloadLinkResult;
import models.features.*;
import models.infos.FormatsResult;
import models.infos.LanguagesResult;
import models.infos.UserResult;
import models.subtitles.SubtitlesResult;
import models.utilities.GuessItQuery;
import models.utilities.GuessItResult;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.TreeMap;

public class Opensubtitles {

    private final String key;
    private final Credentials credentials;
    private TreeMap<String,String> header;

    public Opensubtitles(String username,String password,String apikey) {
        credentials = new Credentials(username,password);
        this.key = apikey;
        header = new TreeMap<>();
        header.put("Content-Type","application/json");
        header.put("Api-Key",key);
        header.put("Accept-Language","en-US,en;q=0.5");
        header.put("Accept","*/*");
    }

    public LoginResult login() throws IOException, InterruptedException {
        String data = Requests.getGson().toJson(credentials);
        LoginResult result = Requests.<LoginResult>post(header,Endpoints.LOGIN,data,LoginResult.class);
        header.put("Authorization","Bearer " + result.token);
        return result;
    }

    public LogoutResult logout() throws IOException, InterruptedException {
        return Requests.<LogoutResult>delete(header,Endpoints.LOGOUT,Query.EMPTY_QUERY,LogoutResult.class);
    }


    public FormatsResult getFormats() throws IOException, InterruptedException {
        return Requests.<FormatsResult>get(header,Endpoints.FORMATS,Query.EMPTY_QUERY,FormatsResult.class);
    }

    public LanguagesResult getLanguages() throws IOException, InterruptedException {
        return Requests.<LanguagesResult>get(header,Endpoints.LANGUAGES,Query.EMPTY_QUERY,LanguagesResult.class);
    }

    public UserResult getUserInfo() throws IOException, InterruptedException {
        return Requests.<UserResult>get(header,Endpoints.USER,Query.EMPTY_QUERY,UserResult.class);
    }

    public DiscoverResult getLatest(Query query) throws IOException, InterruptedException {
        return Requests.<DiscoverResult>get(header,Endpoints.LATEST,query,DiscoverResult.class);
    }

    public DiscoverResult getPopular(Query query) throws IOException, InterruptedException {
        return Requests.<DiscoverResult>get(header,Endpoints.POPULAR,query,DiscoverResult.class);
    }

    public DiscoverResult getMostDownloaded(Query query) throws IOException, InterruptedException {
        return Requests.<DiscoverResult>get(header,Endpoints.MOST_DOWNLOADED,query,DiscoverResult.class);
    }

    public SubtitlesResult getSubtitles(Query query) throws IOException, InterruptedException {
        return Requests.<SubtitlesResult>get(header,Endpoints.SUBTITLES,query,SubtitlesResult.class);
    }

    public Feature[] getFeatures(Query query) throws IOException, InterruptedException {
        FeatureResult fr = Requests.<FeatureResult>get(header,Endpoints.FEATURES,query,FeatureResult.class);
        return fr.data;
    }

    public GuessItResult guess(GuessItQuery query) throws IOException, InterruptedException {
        return Requests.<GuessItResult>get(header,Endpoints.GUESSIT,query,GuessItResult.class);
    }

    public DownloadLinkResult getDownloadLink(DownloadBody body) throws IOException, InterruptedException {
        //String data = gson.toJson(body);
        // The official body isn't working... Not sure why
        String data = "{\"file_id\": " + body.file_id + "}";
        return Requests.<DownloadLinkResult>post(header,Endpoints.DOWNLOAD,data,DownloadLinkResult.class);
    }

    public DownloadLinkResult getDownloadLink(Subtitle.FileObject subFile) throws IOException, InterruptedException {
        DownloadBody body = new DownloadBody().setForceDownload(true).setFileId(subFile.file_id).setFileName(subFile.file_name).setSubFormat("srt").setTimeshift(0);
        return getDownloadLink(body);
    }

    public void download(DownloadLinkResult link, Path location) throws IOException {
        String save = location.toString();
        InputStream inputStream = new URL(link.link).openStream();
        Files.copy(inputStream, Paths.get(save), StandardCopyOption.REPLACE_EXISTING);
    }


}
