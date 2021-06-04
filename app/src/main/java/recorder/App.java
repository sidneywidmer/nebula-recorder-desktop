package recorder;

import com.google.inject.Guice;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import recorder.core.Auth;
import recorder.core.Loader;
import recorder.providers.Bootstrap;

public class App extends Application {
    @Override
    public void start(Stage stage) {
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

    public static void main(String[] args) {
        launch(args);
    }
}
