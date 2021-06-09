package recorder.core.recorders.java.futures;

import com.google.inject.Inject;
import com.typesafe.config.Config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Capture implements Runnable {
    protected Rectangle area;
    protected int interval = 25;
    private volatile boolean running = true;

    private Config config;

    public Capture(Rectangle area, Config config) {
        this.area = area;
        this.config = config;
    }

    @Override
    public void run() {
        var store = config.getString("recorder.storage");

        try {
            var rt = new Robot();
            while (running) {
                var img = rt.createScreenCapture(area);
                ImageIO.write(
                        img,
                        "jpeg",
                        new File(store + "/" + System.currentTimeMillis() + ".jpeg")
                );
                Thread.sleep(interval);
            }
        } catch (IOException | InterruptedException | AWTException ignored) {
        }
    }

    public void stop() {
        running = false;
    }
}
