package recorder.core.capture.java.futures;

import com.google.inject.Inject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Capture implements Runnable {
    protected Rectangle area;
    protected int interval = 25;
    private volatile boolean running = true;

    @Inject
    public Capture(Rectangle area) {
        this.area = area;
    }

    @Override
    public void run() {
        // How to inject config with a parameter?
        // How to bind JavaRecorder to Recorder so we can just reference Recorder?
        // How to record with ffmpeg?
        //  - https://github.com/artclarke/humble-video
        //  - ffmpeg -video_size 1024x768 -framerate 25 -f x11grab -i :0.0 output.mpg

        var store = config.getString("paths.storage");

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
