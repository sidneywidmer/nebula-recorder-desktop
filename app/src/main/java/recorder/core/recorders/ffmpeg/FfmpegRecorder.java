package recorder.core.recorders.ffmpeg;

import com.github.kokorin.jaffree.ffmpeg.CaptureInput;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import com.github.kokorin.jaffree.ffprobe.FFprobe;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;
import com.typesafe.config.Config;
import recorder.core.recorders.CanRecord;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

public class FfmpegRecorder implements CanRecord {
    private final Config config;
    private Rectangle area;

    public FfmpegRecorder(Config config) {
        this.config = config;
    }

    @Override
    public void setArea(Rectangle area) {
        this.area = area;
    }

    @Override
    public void start() {
        Path pathToVideo = Paths.get(config.getString("recorder.storage"));

/*
        FFmpeg.atPath()
                .addInput(CaptureInput
                        .captureDesktop()
                        .setCaptureFrameRate(30)
                        .setCaptureCursor(true)
                )
                .addOutput(UrlOutput
                        .toPath(pathToVideo)
                        // Record with ultrafast to lower CPU usage
                        .addArguments("-preset", "ultrafast")
                        .setDuration(3, TimeUnit.SECONDS)
                )
                .setOverwriteOutput(true)
                .execute();

        //Re-encode when record is completed to optimize file size
        Path pathToOptimized = pathToVideo.resolveSibling("optimized-" + pathToVideo.getFileName());
        FFmpeg.atPath()
                .addInput(UrlInput.fromPath(pathToVideo))
                .addOutput(UrlOutput.toPath(pathToOptimized))
                .execute();
*/
        var foo = FFmpeg.atPath().addArgument("-version").execute();

        //Files.move(pathToOptimized, pathToVideo, StandardCopyOption.REPLACE_EXISTING);
        // How to record with ffmpeg?
        //  - https://github.com/artclarke/humble-video
        //  - ffmpeg -video_size 1024x768 -framerate 25 -f x11grab -i :0.0 output.mpg
        // - event system to handle state (recording, areaSelected, stop)
        // - error handling
        var bar = "bar";
    }

    @Override
    public void stop() {
        // 1. Return file path on stop

    }
}
