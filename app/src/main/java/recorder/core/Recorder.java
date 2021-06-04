package recorder.core;


import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.*;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

public class Recorder {
    protected Rectangle area;

    public Recorder(Rectangle area) {
        this.area = area;
    }

    public void start(int seconds) throws IOException, ExecutionException, InterruptedException {
        var capture = new Capture(area);

        var future = CompletableFuture.runAsync(capture)
                .orTimeout(seconds, TimeUnit.SECONDS)
                .exceptionally(throwable -> {
                    capture.stop();
                    return null;
                });

        var result = future.get();
        var extensionFilter = new FileNameExtensionFilter("N/A", "jpeg");
        var output = new FileImageOutputStream(new File("/home/lab/Desktop/tmp/final.gif"));
        var writer = new GifSequenceWriter(output, TYPE_3BYTE_BGR, 50, false);
        var file = new File("/home/lab/Desktop/tmp");
        var files = file.listFiles();
        Arrays.sort(files);

        for (var child : files) {
            if (!extensionFilter.accept(child)) {
                continue;
            }
            writer.writeToSequence(ImageIO.read(child));
            child.delete();
        }

        writer.close();
        output.close();
    }
}
