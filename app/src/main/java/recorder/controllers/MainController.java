package recorder.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import recorder.core.Stopwatch;
import recorder.events.AreaSelectedEvent;

public class MainController {
    private final SelectAreaController selectArea;
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private Stopwatch stopwatch;

    @FXML
    private Button start;
    @FXML
    private Button stop;
    @FXML
    private Button area;
    @FXML
    private Text info;

    @Inject
    public MainController(SelectAreaController selectArea) {
        this.selectArea = selectArea;
        EventBus.getDefault().register(this);
    }

    public void selectArea(ActionEvent event) {
        selectArea.init();
    }

    /**
     * When starting a recording, the select area and start buttons are disabled.
     */
    public void startRecording(ActionEvent event) {
        selectArea.recorder.start();
        area.setDisable(true);
        start.setDisable(true);
        stop.setDisable(false);

        this.stopwatch = new Stopwatch(info);
        stopwatch.start();

        LOGGER.info("Start Recording");
    }

    /**
     * Once the recording is stopped, we can re-enable the select area and start buttons. The
     * start button is still active because we still know the selected area and a user
     * could record the same area twice.
     */
    public void stopRecording(ActionEvent event) {
        selectArea.recorder.stop();

        area.setDisable(false);
        start.setDisable(false);
        stop.setDisable(true);

        stopwatch.stop();

        LOGGER.info("Stop Recording");
    }

    /**
     * Once an area is selected, we can enable the start recording button.
     */
    @Subscribe
    public void onAreaSelected(AreaSelectedEvent event) {
        var area = event.getArea();

        start.setDisable(false);

        this.info.setVisible(true);
        this.info.setText("Currently selected area: " + area.width + "x" + area.height);
    }
}
