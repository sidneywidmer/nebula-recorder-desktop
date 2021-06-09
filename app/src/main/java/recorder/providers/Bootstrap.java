package recorder.providers;


import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import recorder.core.recorders.Recorder;

public class Bootstrap extends AbstractModule {
    /**
     * Register all our providers.
     */
    protected void configure() {
        bind(Config.class).toProvider(ConfigProvider.class);
        bind(Recorder.class).toProvider(RecorderProvider.class);
    }
}

