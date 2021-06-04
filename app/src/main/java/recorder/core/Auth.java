package recorder.core;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Auth {
    private final String configFile = ".nebula";
    private final String path;

    public Auth() {
        path = System.getProperty("user.home") + "/" + configFile;
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
}
