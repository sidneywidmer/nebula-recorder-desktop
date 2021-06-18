package recorder.core.recorders;

import java.awt.*;
import java.nio.file.Path;

public interface CanRecord {
    public void setArea(Rectangle area);

    public void start();

    public Path stop();
}
