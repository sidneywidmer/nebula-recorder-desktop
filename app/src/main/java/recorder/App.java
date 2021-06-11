package recorder;

import com.google.inject.Guice;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import recorder.core.Auth;
import recorder.core.Loader;
import recorder.core.exceptions.RecorderException;
import recorder.providers.Bootstrap;

public class App extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Override
    public void start(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler(App::errorHandler);

        // Initialize DI
        var injector = Guice.createInjector(new Bootstrap());
        var loader = injector.getInstance(Loader.class);
        var auth = injector.getInstance(Auth.class);

        // Show the login or main view
        var token = auth.getToken();
        if (auth.isValidToken(token)) {
            stage.setScene(new Scene(loader.get("main.fxml")));
        } else {
            stage.setScene(new Scene(loader.get("login.fxml")));
        }
        stage.show();
    }

    /**
     * Custom error handler for uncaught exceptions. We'll use it to gracefully handle custom
     * RecorderExceptions and display en error to the user.
     */
    private static void errorHandler(Thread t, Throwable e) {
        var actualError = e.getCause().getCause();
        if (actualError.getClass() != RecorderException.class) {
            e.printStackTrace();
        }

        LOGGER.error(actualError.getMessage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
