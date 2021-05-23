package recorder;

import com.google.inject.Guice;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import recorder.core.Loader;
import recorder.providers.Bootstrap;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        // Initialize DI
        var injector = Guice.createInjector(new Bootstrap());
        var loader = injector.getInstance(Loader.class);

        // Show the login view
        stage.setScene(new Scene(loader.get("login.fxml")));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
