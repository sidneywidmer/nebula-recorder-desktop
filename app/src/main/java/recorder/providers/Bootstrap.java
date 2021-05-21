package recorder.providers;


import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import javafx.fxml.FXMLLoader;

public class Bootstrap extends AbstractModule {
    /**
     * Register all our providers.
     */
    protected void configure() {
        bind(Config.class).toProvider(ConfigProvider.class);
        bind(FXMLLoader.class).toProvider(FXMLLoaderProvider.class).asEagerSingleton();
    }
}

