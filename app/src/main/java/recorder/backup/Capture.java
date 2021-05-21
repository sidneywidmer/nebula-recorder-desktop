package recorder.backup;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Capture implements Runnable {
    protected Rectangle area;
    protected String store = "/home/lab/Desktop/tmp";
    protected int interval = 100;
    private volatile boolean running = true;

    public Capture(Rectangle area) {
        this.area = area;
    }

    @Override
    public void run() {
        try {
            var rt = new Robot();
            while (running) {
                BufferedImage img = rt.createScreenCapture(area);
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
