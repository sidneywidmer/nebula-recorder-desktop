package recorder;

import com.typesafe.config.Config;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recorder.core.NebulaApi;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiTest {
    private MockWebServer server;
    private NebulaApi api;

    /**
     * Before each test we create a fresh server instance which our api class then can query.
     * We can then inspect the made requests to check if they're complete.
     */
    @BeforeEach
    public void init() throws IOException {
        server = new MockWebServer();

        // We'll just return an empty 200 message
        server.enqueue(new MockResponse().setBody(""));
        server.start();

        var config = mock(Config.class);
        api = new NebulaApi(config);

        // Get the path of our server, unfortunately it has a trailing slash we don't want.
        var path = server.url("/").toString();
        var cleanPath = path.substring(0, path.length() - 1);

        // Inject our fake endpoint
        when(config.getString("api.endpoint")).thenReturn(cleanPath);
    }

    @Test
    void login() throws InterruptedException {
        var payload = new JSONObject().put("email", "foo@bar.ch").put("password", "hunter1234");
        api.login(payload);

        var request = server.takeRequest();
        assertEquals("/api/auth/login", request.getPath());
        assertEquals(payload.toString(), request.getBody().readUtf8());
        assertEquals("POST", request.getMethod());
        assertNull(request.getHeader("Authorization"));
    }

    @Test
    void check() throws InterruptedException {
        var token = "Foo";
        api.check(token);

        var request = server.takeRequest();
        assertEquals("/api/auth/check", request.getPath());
        assertEquals("", request.getBody().readUtf8());
        assertEquals("GET", request.getMethod());
        assertEquals("Bearer Foo", request.getHeader("Authorization"));
    }

    @Test
    void upload() throws InterruptedException {
        var token = "Foo";
        var path = Paths.get("src/test/java/recorder/data/dummy.txt");
        api.upload(path, token);


        var request = server.takeRequest();
        var body = request.getBody().readUtf8();
        assertEquals("/api/recording/upload", request.getPath());
        assertTrue(body.contains("GIF"));
        assertTrue(body.contains("dummy.txt"));
        assertEquals("POST", request.getMethod());
        assertEquals("Bearer Foo", request.getHeader("Authorization"));
    }


}
