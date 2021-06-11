package recorder.core.recorders.ffmpeg;

import com.github.kokorin.jaffree.ffmpeg.*;
import com.typesafe.config.Config;
import recorder.core.recorders.CanRecord;

import java.awt.*;
import java.nio.file.Paths;

public class FfmpegRecorder implements CanRecord {
    private final Config config;
    private Rectangle area;
    private FFmpegResultFuture current;

    public FfmpegRecorder(Config config) {
        this.config = config;
    }

    @Override
    public void setArea(Rectangle area) {
        this.area = area;
    }

    @Override
    public void start() {
        var basePath = config.getString("recorder.storage");
        var filename = "recording-" + System.currentTimeMillis() + ".gif";
        var pathToVideo = Paths.get(basePath + "/" + filename);

        var capture = CaptureInput
                .captureDesktop()
                .setCaptureFrameRate(30)
                .setCaptureCursor(true)
                .setCaptureVideoOffset(area.x, area.y)
                .setCaptureVideoSize(area.width, area.height);

        var output = UrlOutput.toPath(pathToVideo);

        current = FFmpeg.atPath()
                .addInput(capture)
                .addOutput(output)
                .setOverwriteOutput(true)
                .executeAsync();
    }

    @Override
    public void stop() {
        // 1. Return file path on stop
        current.graceStop();
    }
}
