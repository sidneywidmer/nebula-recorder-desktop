package recorder.core;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

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
        client = new OkHttpClient().newBuilder().build();
    }

    public Response login(JSONObject payload) {
        return post(payload, "/api/auth/login");
    }

    private Response post(JSONObject payload, String endpoint) {
        var body = RequestBody.create(payload.toString(), JSON);
        var request = new Request.Builder()
                .url(config.getString("api.endpoint") + endpoint)
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
