package recorder.core;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import okhttp3.*;
import org.json.JSONObject;
import recorder.core.exceptions.RecorderException;

import java.io.File;
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

    public Response upload(Path recording, String token) {
        var file = RequestBody.create(recording.toFile(), MediaType.parse("multipart/form-data"));

        var body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", recording.getFileName().toString())
                .addFormDataPart("description", "")
                .addFormDataPart("type", "GIF")
                .addFormDataPart("recording", recording.getFileName().toString(), file)
                .build();

        var request = new Request.Builder()
                .url(config.getString("api.endpoint") + "/api/recording/upload")
                .method("POST", body);

        return request(request, token);
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
