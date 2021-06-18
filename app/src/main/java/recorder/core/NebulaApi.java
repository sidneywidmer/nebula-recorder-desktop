package recorder.core;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import okhttp3.*;
import org.json.JSONObject;
import recorder.core.exceptions.RecorderException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Super minimalist nebula api client with a lot of potential improvements :)
 */
public class NebulaApi {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;
    private final Config config;

    @Inject
    public NebulaApi(Config config) {
        this.config = config;
        client = new OkHttpClient()
                .newBuilder()
                .callTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public Response login(JSONObject payload) {
        return post(payload, "/api/auth/login");
    }

    public Response check(String token) {
        var payload = new JSONObject();
        return get("/api/auth/check", token);
    }

    public Response upload(Path recording, String token) throws IOException {
        var file = Files.readAllBytes(recording);

        var payload = new JSONObject()
                .put("name", recording.getFileName())
                .put("description", "")
                .put("type", "GIF")
                .put("recording", Base64.getEncoder().encode(file));

        return post(payload, "/api/recording/upload", token);
    }

    private Response get(String endpoint, String token) {
        var request = new Request.Builder()
                .url(config.getString("api.endpoint") + endpoint)
                .get();

        return request(request, token);
    }

    private Response post(JSONObject payload, String endpoint) {
        return post(payload, endpoint, null);
    }

    private Response post(JSONObject payload, String endpoint, String token) {
        var body = RequestBody.create(payload.toString(), JSON);
        var request = new Request.Builder()
                .url(config.getString("api.endpoint") + endpoint)
                .post(body);

        return request(request, token);
    }

    private Response request(Request.Builder request, String token) {
        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        }

        Response response = null;
        try {
            response = client.newCall(request.build()).execute();
        } catch (IOException e) {
            throw new RecorderException("Could not reach API server.");
        }

        return response;
    }
}
