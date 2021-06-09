package recorder.providers;


import com.google.inject.Inject;
import com.google.inject.Provider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import recorder.core.recorders.Recorder;
import recorder.core.recorders.ffmpeg.FfmpegRecorder;
import recorder.core.recorders.java.JavaRecorder;

public class RecorderProvider implements Provider<Recorder> {
    private final Config config;

    @Inject
    public RecorderProvider(Config config) {
        this.config = config;
    }

    /**
     * We have different methods of recording the screen. Historically we started with the
     * pure JavaRecorder which turned out to be super complex and then moved on to
     * the ffmpeg recorder.
     */
    @Override
    public Recorder get() {
        return new Recorder(new FfmpegRecorder(config));
    }
}
