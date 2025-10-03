package id.gasper.opensubtitles.exception;

public class OpensubtitlesTimeoutException extends Exception {

    public OpensubtitlesTimeoutException(String message) {
        super(message);
    }

    public OpensubtitlesTimeoutException(Throwable throwable) {
        super(throwable);
    }

    public OpensubtitlesTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
