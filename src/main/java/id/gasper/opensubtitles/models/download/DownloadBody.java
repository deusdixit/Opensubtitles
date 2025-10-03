package id.gasper.opensubtitles.models.download;

/**
 * The DownloadBody class represents the payload for requesting a file download.
 * It allows setting various parameters such as file ID, subtitle format, file name, frame rates,
 * time shift, and whether the download should be forced.
 */
public class DownloadBody {

    public int file_id;
    public String sub_format;
    public String file_name;
    public float in_fps;
    public float out_fps;
    public int timeshift;
    public boolean force_download;

    public DownloadBody setFileId(int id) {
        file_id = id;
        return this;
    }

    public DownloadBody setSubFormat(String value) {
        sub_format = value;
        return this;
    }

    public DownloadBody setFileName(String value) {
        file_name = value;
        return this;
    }

    public DownloadBody setInFps(float num) {
        in_fps = num;
        return this;
    }

    public DownloadBody setOutFps(float num) {
        out_fps = num;
        return this;
    }

    public DownloadBody setTimeshift(int num) {
        timeshift = num;
        return this;
    }

    public DownloadBody setForceDownload(boolean value) {
        force_download = value;
        return this;
    }
}
