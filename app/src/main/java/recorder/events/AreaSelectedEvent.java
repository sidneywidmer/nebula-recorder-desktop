package recorder.events;

import java.awt.*;

public class AreaSelectedEvent {
    private final Rectangle area;

    public AreaSelectedEvent(Rectangle area) {
        this.area = area;
    }

    public Rectangle getArea() {
        return area;
    }
}
