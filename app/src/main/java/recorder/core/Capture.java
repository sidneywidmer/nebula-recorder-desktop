package recorder.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Capture implements Runnable {
    protected Rectangle area;
    protected String store = "/home/lab/Desktop/tmp";
    protected int interval = 25;
    private volatile boolean running = true;

    public Capture(Rectangle area) {
        this.area = area;
    }

    @Override
    public void run() {
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
