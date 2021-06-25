package recorder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import recorder.core.Auth;
import recorder.core.NebulaApi;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;
import uk.org.webcompere.systemstubs.properties.SystemProperties;

import java.io.File;

@ExtendWith(SystemStubsExtension.class)
class AuthTest {

    @SystemStub
    private SystemProperties systemProperties;

    @BeforeEach
    void before() {
        var path = new File("src/test/java/recorder/data/");
        systemProperties.set("user.home", path.getAbsolutePath());
    }

    @Test
    void givenValidFile_getToken() {
        var path = new File("src/test/java/recorder/data/");
        systemProperties.set("user.home", path.getAbsolutePath());

        var nebulaApi = mock(NebulaApi.class);
        var auth = new Auth(nebulaApi);

        var token = auth.getToken();
        assertEquals(token, "foo-bar");
    }

    @Test
    void givenInvalidFile_getToken() {
        // Non existent path
        var path = new File("fail/");
        systemProperties.set("user.home", path.getAbsolutePath());

        var nebulaApi = mock(NebulaApi.class);
        var auth = new Auth(nebulaApi);

        var token = auth.getToken();
        assertNull(token);
    }
}

