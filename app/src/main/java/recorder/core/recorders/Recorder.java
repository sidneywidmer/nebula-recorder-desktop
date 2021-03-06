package recorder.core.recorders;

import org.greenrobot.eventbus.EventBus;
import recorder.events.AreaSelectedEvent;

import java.awt.*;
import java.nio.file.Path;

public class Recorder implements CanRecord {
    private final CanRecord implementation;
    private Rectangle area;

    public Recorder(CanRecord implementation) {
        this.implementation = implementation;
    }


    @Override
    public void setArea(Rectangle area) {
        implementation.setArea(area);
        EventBus.getDefault().post(new AreaSelectedEvent(area));
    }

    @Override
    public void start() {
        implementation.start();
    }

    @Override
    public Path stop() {
        return implementation.stop();
    }
}
