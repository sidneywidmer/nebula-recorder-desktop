package recorder.core.recorders.java;


import com.typesafe.config.Config;
import recorder.core.exceptions.RecorderException;
import recorder.core.recorders.CanRecord;
import recorder.core.recorders.java.futures.Capture;
import recorder.core.recorders.java.futures.ConvertToGif;

import java.awt.*;
import java.nio.file.Path;
import java.util.concurrent.*;

/**
 * Legacy CanRecord implementation that got replaced by the ffmpeg recorder. Because we had
 * to change the interface along the way, the stop method is not really clean and was just
 * "made to somehow work" and would need a refactor.
 */
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
    public Path stop() {
        currentCapture.stop();

        var toGif = new ConvertToGif(config);
        var feature = CompletableFuture.runAsync(toGif);

        try {
            feature.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RecorderException("Could not convert stills to gif.");
        }

        return Path.of(config.getString("recorder.storage") + "/recording.gif");
    }
}
