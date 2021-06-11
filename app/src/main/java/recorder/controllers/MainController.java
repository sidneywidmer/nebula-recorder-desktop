package recorder.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {
    private final SelectAreaController selectArea;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @FXML
    private Button start;
    @FXML
    private Button stop;
    @FXML
    private Button area;

    @Inject
    public MainController(SelectAreaController selectArea) {
        this.selectArea = selectArea;
    }

    public void selectArea(ActionEvent event) {
        selectArea.init();
    }

    public void startRecording(ActionEvent event) {
        selectArea.recorder.start();
        LOGGER.info("Start Recording");
    }

    public void stopRecording(ActionEvent event) {
        selectArea.recorder.stop();
        LOGGER.info("Stop Recording");
    }
}
