package recorder.core.recorders.java;


import com.typesafe.config.Config;
import recorder.core.recorders.CanRecord;
import recorder.core.recorders.java.futures.Capture;
import recorder.core.recorders.java.futures.ConvertToGif;

import java.awt.*;
import java.util.concurrent.*;

public class JavaRecorder implements CanRecord {
    private final Config config;
    private Capture currentCapture;
    private Rectangle area;

    public JavaRecorder(Config config) {
        this.config = config;
    }

    @Override
    public void setArea(Rectangle area) {
        this.area = area;
    }

    @Override
    public void start() {
        currentCapture = new Capture(area, config);
        CompletableFuture.runAsync(currentCapture);
    }

    @Override
    public void stop() {
        currentCapture.stop();

        var toGif = new ConvertToGif(config);
        CompletableFuture.runAsync(toGif);
    }
}
