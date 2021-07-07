package recorder.events;

public class OpenDocumentEvent {

    private final String path;

    public OpenDocumentEvent(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
