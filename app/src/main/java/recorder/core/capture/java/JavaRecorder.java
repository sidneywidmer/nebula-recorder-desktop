package recorder.core.capture.java;


import recorder.core.capture.Recorder;
import recorder.core.capture.java.futures.Capture;
import recorder.core.capture.java.futures.ConvertToGif;

import java.awt.*;
import java.util.concurrent.*;

public class JavaRecorder implements Recorder {
    protected Rectangle area;
    private Capture currentCapture;


    public JavaRecorder(Rectangle area) {
        this.area = area;
    }

    @Override
    public void start() {
        currentCapture = new Capture(area);
        CompletableFuture.runAsync(currentCapture);
    }

    @Override
    public void stop() {
        currentCapture.stop();

        var toGif = new ConvertToGif();
        CompletableFuture.runAsync(toGif);
    }
}
