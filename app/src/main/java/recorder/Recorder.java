package recorder;


import java.awt.*;
import java.util.concurrent.*;

public class Recorder {
    protected Rectangle area;

    public Recorder(Rectangle area) {
        this.area = area;
    }

    public void start(int seconds) {
        var capture = new Capture(area);

        CompletableFuture.runAsync(capture)
                .orTimeout(seconds, TimeUnit.SECONDS)
                .exceptionally(throwable -> {
                    System.out.println("Done");
                    capture.stop();
                    return null;
                });
    }
}
