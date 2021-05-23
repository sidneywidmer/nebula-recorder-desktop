package recorder.providers;


import com.google.inject.AbstractModule;
import com.typesafe.config.Config;

public class Bootstrap extends AbstractModule {
    /**
     * Register all our providers.
     */
    protected void configure() {
        bind(Config.class).toProvider(ConfigProvider.class);
    }
}

