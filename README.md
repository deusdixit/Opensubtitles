# Requirements
- An Account at https://www.opensubtitles.com
- API-Key
- More documentation : https://opensubtitles.stoplight.io/docs/opensubtitles-api/
# INSTALL
# EXAMPLES
### LOGIN
```java
class LoginLogoutExample {
    public static void main(String[] args) {
        Opensubtitles os = new Opensubtitles("username", "password", "token");
        LoginResult lr = os.login();
        if ( lr.status == 200 ) {
            System.out.println("Logged in..");
            os.logout();
        } else {
            System.out.println("Error Code : " + status);
        }
    }
}
```
### Search for features

```java
class SearchFeaturesExample {
    public static void searchFeature(Opensubtitles os) {
        FeatureQuery fq = new FeatureQuery().setQuery("Supernatural")
                .setYear(2011);
        Feature[] features = os.getFeatures(fq.build());
        
        for (Feature f : features) {
            if (f instanceof TvShow show) {
                System.out.println(show.attributes.title);
            } else if (f instanceof Episode episode) {
                System.out.println(episode.attributes.title);
            }
        }
    }
}
```
### Search for subtitles

```java
class SearchSubtitleExample {
    public static void searchSubtitle(Opensubtitles os) {
        SubtitlesQuery sq = new SubtitlesQuery().setQuery("Supernatural")
                .setEpisodeNumber(3)
                .setSeasonNumber(3);
        SubtitlesResult subs = os.getSubtitles(sq.build());
        
        for (Subtitle sub : subs.data) {
            System.out.println(sub.attributes.uploader.name);
        }
    }
}
```
### Download subtitles

```java
class DownloadSubtitlesExample {
    public static void downloadSubtitles(Opensubtitles os, Subtitle sub) {
        for (Subtitle.FileObject file : sub.attributes.files) {
            DownloadLinkResult dlr = os.getDownloadLink(file);
            os.download(dlr, Paths.get("/location/"));
        }
    }
}
```