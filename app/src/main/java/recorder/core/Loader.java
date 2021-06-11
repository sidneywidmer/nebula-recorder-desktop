package recorder.core;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import recorder.App;
import recorder.core.exceptions.RecorderException;

import java.io.IOException;

/**
 * Helper class to load new views and resolving the classes via our DI.
 */
public class Loader {
    private final FXMLLoader loader;

    @Inject
    public Loader(Injector injector) {
        loader = new FXMLLoader();
        loader.setControllerFactory(injector::getInstance);
    }

    public Parent get(String fxml) {
        var resource = App.class.getResourceAsStream(fxml);
        try {
            return loader.load(resource);
        } catch (IOException e) {
            throw new RecorderException("IOException while loading fxml at " + fxml);
        }
    }
}
