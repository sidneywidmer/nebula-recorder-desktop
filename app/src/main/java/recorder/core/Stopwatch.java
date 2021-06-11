package recorder.core;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Stopwatch {
    private final Text label;
    private Timeline timeline;
    private LocalTime time = LocalTime.parse("00:00");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");

    public Stopwatch(Text label) {
        this.label = label;
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> incrementTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void start() {
        label.setVisible(true);
        timeline.play();
    }

    public void stop() {
        timeline.stop();
        time = LocalTime.parse("00:00");
        label.setText("Finished.");
    }

    private void incrementTime() {
        time = time.plusSeconds(1);
        label.setText(time.format(dtf));
    }
}
