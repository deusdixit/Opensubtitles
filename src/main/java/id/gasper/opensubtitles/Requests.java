package id.gasper.opensubtitles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import id.gasper.opensubtitles.models.Query;

import java.io.IOException;
import java.net.HttpRetryException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

/**
 * Class for the HTTP-Requests ( GET,POST,DELETE )
 * The requests get interpreted by the Gson Library and converted into the corresponding classes.
 *
 * @param <T> generic type for the corresponding class
 */
public class Requests<T> {
    private static Gson gsonInstance = null;
    protected static final int RETRIES = 5;
    protected static final int SLEEP_BEFORE_RETRY = 3000;
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

    /**
     * Singleton-Function for the Gson Object
     *
     * @return a Gson Object
     */
    public static Gson getGson() {
        if (gsonInstance == null) {
            RuntimeTypeAdapterFactory<id.gasper.opensubtitles.models.features.Feature> rta = RuntimeTypeAdapterFactory.of(
                            id.gasper.opensubtitles.models.features.Feature.class, "type", true)
                    .registerSubtype(id.gasper.opensubtitles.models.features.Movie.class, "movie")
                    .registerSubtype(id.gasper.opensubtitles.models.features.TvShow.class, "tvshow")
                    .registerSubtype(id.gasper.opensubtitles.models.features.Episode.class, "episode")
                    .registerSubtype(id.gasper.opensubtitles.models.features.Subtitle.class, "subtitle");
            gsonInstance = new GsonBuilder().registerTypeAdapterFactory(rta).serializeNulls().create();
        }
        return gsonInstance;
    }

    /**
     * Builds the URI for the request
     *
     * @param path  The URL path
     * @param query The query parameters (null for POST requests)
     * @return The complete URI
     */
    private static URI buildUri(String path, Query query) {
        String queryString = query != null ? query.toString() : "";
        return URI.create(Endpoints.BASE + path + queryString);
    }

    /**
     * Adds headers from the map to the request builder
     *
     * @param builder The HTTP request builder
     * @param headers Map containing the header data
     */
    private static void addHeaders(HttpRequest.Builder builder, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Executes an HTTP request with retry logic
     * <p>
     * Opensubtitles API doesn't seem stable at this point of time. This makes a retry logic necessary.
     *
     * @param requestSupplier Supplier that creates the HTTP request
     * @param responseHandler Function that handles the successful response
     * @param <T>             Response type
     * @return The parsed response object
     */
    private static <T> T executeWithRetry(
            HttpRequestSupplier requestSupplier,
            ResponseHandler<T> responseHandler) throws IOException, InterruptedException {
        int retryCounter = 0;
        int lastStatusCode = -1;
        String lastBody = "";
        try (HttpClient client = HttpClient.newHttpClient()) {

            while (retryCounter < RETRIES) {
                HttpRequest request = requestSupplier.get();
                HttpResponse<?> response = client.send(request, responseHandler.bodyHandler());

                int code = response.statusCode();
                if (code >= 400) {
                    lastStatusCode = code;
                    lastBody = bodyToString(response.body());
                    // Bei 429 (Rate-Limit) keinen Retry: erneute Versuche verschlimmern
                    // das Limit nur und verbraehen Zeit. Sofort melden, damit der Aufrufer
                    // reagieren kann (z. B. pausieren oder abbrechen).
                    if (code == 429) {
                        throw buildRetryException(code, lastBody);
                    }
                    retryCounter++;
                    if (retryCounter < RETRIES) {
                        Thread.sleep(SLEEP_BEFORE_RETRY);
                    }
                    continue;
                }

                return responseHandler.parse(response);
            }
        }

        throw buildRetryException(lastStatusCode, lastBody);
    }

    /**
     * Baut eine aussagekraeftige {@link HttpRetryException}: HTTP-Statuscode plus die vom API
     * gelieferte {@code message} (falls im JSON-Body enthalten), sonst ein kurzer Body-Auszug.
     * So sieht der Aufrufer WHY eine Anfrage fehlgeschlagen ist (Rate-Limit, 0 Downloads
     * verbleibend, nicht autorisiert, …) statt nur „Retries failed with status code".
     */
    private static HttpRetryException buildRetryException(int statusCode, String body) {
        String meaning = extractMessage(body);
        if (meaning == null || meaning.isBlank()) {
            // Bekannte Statuscodes mit Kurzform, falls der Body keine message enthaelt.
            meaning = switch (statusCode) {
                case 401 -> "Nicht autorisiert (Login/Token fehlt oder ungueltig)";
                case 403 -> "Verboten";
                case 406 -> "Nicht akzeptiert (oft: keine Downloads mehr verbleibend)";
                case 429 -> "Rate-Limit ueberschritten (Anfragen pro Sekunde begrenzen)";
                case -1 -> "Keine Antwort (Netzwerk-/Timeout-Fehler)";
                default -> "";
            };
            if (meaning.isBlank() && body != null && !body.isBlank()) {
                meaning = "Body: " + body.substring(0, Math.min(body.length(), 200));
            }
        }
        String detail = meaning.isBlank() ? "" : " – " + meaning;
        String codeText = statusCode < 0 ? "ohne Antwort" : String.valueOf(statusCode);
        return new HttpRetryException(
                "OpenSubtitles HTTP " + codeText + detail, statusCode);
    }

    /**
     * Liest den Response-Body (byte[] oder String) als UTF-8-String – unabhängig vom
     * konkreten {@link HttpResponse.BodyHandler}.
     */
    private static String bodyToString(Object body) {
        if (body == null) {
            return "";
        }
        if (body instanceof byte[] b) {
            return new String(b, StandardCharsets.UTF_8);
        }
        if (body instanceof String s) {
            return s;
        }
        return body.toString();
    }

    /**
     * Extrahiert das {@code message}-Feld aus einem OpenSubtitles-Fehler-JSON, z. B.
     * {@code {"message":"API rate limit exceeded","status":429}}. Bewusst ohne
     * Gson (regex), damit die Fehlermeldung auch bei kaputtem JSON noch greift.
     */
    private static String extractMessage(String body) {
        if (body == null || body.isBlank()) {
            return "";
        }
        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("\"message\"\s*:\s*\"([^\"]*)\"").matcher(body);
        return m.find() ? m.group(1) : "";
    }

    /**
     * Function for a GET-Request.
     *
     * @param header Map containing the Header-Data
     * @param path   The Url
     * @param q      The Query
     * @param cls    The Wrapper Class
     * @param <T>    Wrapper Class Type
     * @return A Wrapper Class Object
     */
    public static <T> T get(Map<String, String> header, String path, Query q, Class<T> cls) throws IOException, InterruptedException {
        Gson gson = getGson();

        HttpRequestSupplier requestSupplier = () -> {
            HttpRequest.Builder builder = HttpRequest.newBuilder(buildUri(path, q));
            builder.timeout(REQUEST_TIMEOUT);
            addHeaders(builder, header);
            builder.GET();
            return builder.build();
        };

        ResponseHandler<T> responseHandler = new ResponseHandler<>() {
            @Override
            public HttpResponse.BodyHandler<?> bodyHandler() {
                return HttpResponse.BodyHandlers.ofByteArray();
            }

            @Override
            public T parse(HttpResponse<?> response) {
                byte[] body = (byte[]) response.body();
                return gson.fromJson(new String(body, StandardCharsets.UTF_8), cls);
            }
        };

        return executeWithRetry(requestSupplier, responseHandler);
    }

    /**
     * Function for a POST-Request.
     *
     * @param header Map containing the Header-Data
     * @param path   The Url
     * @param data   The Query-Data
     * @param cls    The Wrapper Class
     * @param <T>    Wrapper Class Type
     * @return A Wrapper Class Object
     */
    public static <T> T post(Map<String, String> header, String path, String data, Class<T> cls) throws IOException, InterruptedException {
        Gson gson = getGson();

        HttpRequestSupplier requestSupplier = () -> {
            HttpRequest.Builder builder = HttpRequest.newBuilder(buildUri(path, null));
            builder.timeout(REQUEST_TIMEOUT);
            addHeaders(builder, header);
            builder.POST(HttpRequest.BodyPublishers.ofString(data));
            return builder.build();
        };

        ResponseHandler<T> responseHandler = new ResponseHandler<>() {
            @Override
            public HttpResponse.BodyHandler<?> bodyHandler() {
                return HttpResponse.BodyHandlers.ofString();
            }

            @Override
            public T parse(HttpResponse<?> response) {
                String body = (String) response.body();
                return gson.fromJson(body, cls);
            }
        };

        return executeWithRetry(requestSupplier, responseHandler);
    }

    /**
     * Function for a DELETE-Request.
     *
     * @param header Map containing the Header-Data
     * @param path   The Url
     * @param q      The Query
     * @param cls    The Wrapper Class
     * @param <T>    Wrapper Class Type
     * @return A Wrapper Class Object
     */
    public static <T> T delete(Map<String, String> header, String path, Query q, Class<T> cls) throws IOException, InterruptedException {
        Gson gson = getGson();

        HttpRequestSupplier requestSupplier = () -> {
            HttpRequest.Builder builder = HttpRequest.newBuilder(buildUri(path, q));
            builder.timeout(REQUEST_TIMEOUT);
            addHeaders(builder, header);
            builder.DELETE();
            return builder.build();
        };

        ResponseHandler<T> responseHandler = new ResponseHandler<>() {
            @Override
            public HttpResponse.BodyHandler<?> bodyHandler() {
                return HttpResponse.BodyHandlers.ofString();
            }

            @Override
            public T parse(HttpResponse<?> response) {
                String body = (String) response.body();
                return gson.fromJson(body, cls);
            }
        };

        return executeWithRetry(requestSupplier, responseHandler);
    }

    @FunctionalInterface
    private interface HttpRequestSupplier {
        HttpRequest get() throws IOException;
    }

    private interface ResponseHandler<T> {
        HttpResponse.BodyHandler<?> bodyHandler();
        T parse(HttpResponse<?> response);
    }
}
