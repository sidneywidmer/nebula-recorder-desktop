package recorder.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import recorder.core.Auth;
import recorder.core.NebulaApi;
import recorder.core.Stopwatch;
import recorder.core.exceptions.RecorderException;
import recorder.events.AreaSelectedEvent;
import recorder.events.OpenDocumentEvent;

import java.io.IOException;
import java.util.HashMap;

public class MainController {
    private final SelectAreaController selectArea;
    private final NebulaApi nebula;
    private final Auth auth;
    private final Config config;
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
    @FXML
    private Hyperlink link;

    @Inject
    public MainController(SelectAreaController selectArea, NebulaApi nebula, Auth auth, Config config) {
        this.selectArea = selectArea;
        this.nebula = nebula;
        this.auth = auth;
        this.config = config;

        EventBus.getDefault().register(this);
    }

    protected void registerLinkHandler(Hyperlink link) {
        link.setOnAction((ActionEvent event) -> {
            EventBus.getDefault().post(new OpenDocumentEvent(link.getTooltip().getText()));
            event.consume();
        });
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
    public void stopRecording(ActionEvent event) throws IOException {
        var newRecording = selectArea.recorder.stop();
        stopwatch.stop();

        // Fmpeg needs a sec on the filesystem to finish the output stream...
        try {
            info.setText("Finalizing...");
            Thread.sleep(1_000);
        } catch (InterruptedException ignored) {
        }

        area.setDisable(false);
        start.setDisable(false);
        stop.setDisable(true);

        info.setText("Uploading...");

        try {
            var response = nebula.upload(newRecording, auth.getToken());
            var parsedResponse = new ObjectMapper().readValue(response.body().string(), HashMap.class);
            info.setVisible(false);

            link.setVisible(true);
            link.setTooltip(new Tooltip(config.getString("api.endpoint") + parsedResponse.get("url")));
            link.setText("Recording anzeigen");
            registerLinkHandler(link);
        } catch (RecorderException e) {
            info.setText("Upload failed.");
        }
        LOGGER.info("Stop Recording");
    }

    /**
     * Once an area is selected, we can enable the start recording button.
     */
    @Subscribe
    public void onAreaSelected(AreaSelectedEvent event) {
        var area = event.getArea();

        start.setDisable(false);

        this.link.setVisible(false);
        this.info.setVisible(true);
        this.info.setText("Currently selected area: " + area.width + "x" + area.height);
    }
}
