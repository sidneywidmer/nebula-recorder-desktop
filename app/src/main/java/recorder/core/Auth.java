package recorder.core;

import com.google.inject.Inject;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Auth {
    private final String path;
    private final NebulaApi nebulaApi;

    @Inject
    public Auth(NebulaApi nebulaApi) {
        this.nebulaApi = nebulaApi;
        path = System.getProperty("user.home") + "/.nebula";
    }

    public String getToken() {
        try {
            var parsedConfig = new JSONObject(Files.readString(Path.of(path)));
            return parsedConfig.getString("token");
        } catch (IOException e) {
            return null;
        }
    }

    public void saveToken(String token) throws IOException {
        Files.writeString(Path.of(path), token);
    }

    public boolean isValidToken(String token) {
        return nebulaApi.check(token).isSuccessful();
    }
}
