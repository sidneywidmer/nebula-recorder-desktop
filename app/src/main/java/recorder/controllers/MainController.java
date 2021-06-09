package recorder.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {
    private final SelectAreaController selectArea;

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
        System.out.println("Start Recording");
    }

    public void stopRecording(ActionEvent event) {
        selectArea.recorder.stop();
        System.out.println("Stop Recording");
    }
}
