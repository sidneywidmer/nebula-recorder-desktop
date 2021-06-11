package recorder;

import com.google.inject.Guice;
import javafx.application.Application;
import javafx.application.Platform;
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
        Thread.setDefaultUncaughtExceptionHandler(App::showError);

        // Initialize DI
        var injector = Guice.createInjector(new Bootstrap());
        var loader = injector.getInstance(Loader.class);
        var auth = injector.getInstance(Auth.class);

        // Show the login or main view
        if (auth.getToken() != null) {
            stage.setScene(new Scene(loader.get("main.fxml")));
        } else {
            stage.setScene(new Scene(loader.get("login.fxml")));
        }
        stage.show();
    }

    private static void showError(Thread t, Throwable e) {
        LOGGER.error(e.getMessage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
