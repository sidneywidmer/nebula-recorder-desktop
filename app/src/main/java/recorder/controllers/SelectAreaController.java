package recorder.controllers;

import com.github.kokorin.jaffree.ffmpeg.FFmpegStopper;
import com.google.inject.Inject;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import recorder.core.recorders.Recorder;

import java.awt.*;

class SelectAreaController {
    private Stage window;
    private StackPane root;
    private Rectangle rect;

    private Point currentDrag;
    private Point currentDragScreen;

    public Recorder recorder;

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectAreaController.class);

    @Inject
    public SelectAreaController(Recorder recorder) {
        this.recorder = recorder;
    }

    /**
     * Create a transparent window that overlay the whole screen. The user can then draw a rectangle
     * on that Stage to select the area he want's to record. We don't use a fxml file for this
     * view since it doesn't have many elements and consists mostly of logic.
     */
    protected void init() {
        Rectangle2D screen = Screen.getPrimary().getBounds();

        root = new StackPane();
        root.setAlignment(Pos.TOP_LEFT);
        root.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(root, screen.getWidth(), screen.getHeight());
        scene.setCursor(Cursor.CROSSHAIR);
        scene.setOnMousePressed(this::dragStart);
        scene.setOnMouseReleased(this::dragEnd);
        scene.setOnMouseDragged(this::midDrag);

        window = new Stage();
        window.initStyle(StageStyle.TRANSPARENT);
        window.setOpacity(0.1);
        window.setTitle("Select Recording Area");
        window.setScene(scene);

        window.show();
    }

    private void dragStart(MouseEvent event) {
        currentDragScreen = new Point((int) event.getScreenX(), (int) event.getScreenY());
        currentDrag = new Point((int) event.getX(), (int) event.getY());

        rect = new Rectangle(0, 0);
        root.getChildren().add(rect);
        rect.setFill(Color.CYAN);

        var position = new Translate(event.getX(), event.getY());
        rect.getTransforms().add(position);
    }

    /**
     * Resize the selection area while dragging.
     */
    private void midDrag(MouseEvent event) {
        rect.setWidth(event.getX() - currentDrag.getX());
        rect.setHeight(event.getY() - currentDrag.getY());
    }

    private void dragEnd(MouseEvent event) {
        window.close();

        var point = new Point((int) event.getScreenX(), (int) event.getScreenY());
        var area = new java.awt.Rectangle();
        area.setFrameFromDiagonal(currentDragScreen, point);

        recorder.setArea(area);
        LOGGER.info("Area selected");
    }
}
