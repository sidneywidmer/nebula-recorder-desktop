package recorder.core.capture.java.futures;

import recorder.core.GifSequenceWriter;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

public class ConvertToGif implements Runnable {
    @Override
    public void run() {
        try {
            var extensionFilter = new FileNameExtensionFilter("N/A", "jpeg");
            var output = new FileImageOutputStream(new File("/home/lab/Desktop/tmp/final_" + System.currentTimeMillis() + ".gif"));
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
        } catch (IOException ignored) {
        }
    }
}
