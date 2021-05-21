package recorder;

import com.google.inject.Guice;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import recorder.providers.Bootstrap;

import java.io.IOException;
import java.io.InputStream;


public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize DI
        var injector = Guice.createInjector(new Bootstrap());
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);

        // To start, show the login screen
        try (InputStream fxmlInputStream = ClassLoader.getSystemResourceAsStream("recorder/login.fxml")) {
            Parent parent = loader.load(fxmlInputStream);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 3..2..1.. LIFTOFF!
        launch(args);
    }
}
