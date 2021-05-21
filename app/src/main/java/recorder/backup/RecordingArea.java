package recorder.backup;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import javafx.application.Application;

import java.awt.*;

class RecordingArea {
    private final Application parent;

    private Stage window;
    private StackPane root;
    private Rectangle rect;
    private Scale scale;

    private Point currentDrag;
    private Point currentDragScreen;

    public RecordingArea(Application parent) {
        this.parent = parent;
        setupGui();
    }

    /**
     * Create a transparent window that overlay the whole screen. The user can then draw a rectangle
     * on that Stage to select the area he want's to record.
     */
    protected void setupGui() {
        Rectangle2D screen = Screen.getPrimary().getBounds();

        root = new StackPane();
        root.setAlignment(Pos.TOP_LEFT);
        root.setStyle("-fx-background-color: transparent;");

        // Wee need something to click on to
/*
        Rectangle rectangle = new Rectangle(screen.getWidth(), screen.getHeight());
        rectangle.setFill(new Color(1, 1, 1, 0.5));
        root.getChildren().add(rectangle);
*/

        Scene scene = new Scene(root, screen.getWidth(), screen.getHeight());
        //scene.setFill(Color.TRANSPARENT);
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
        window.hide();

        var point = new Point((int) event.getScreenX(), (int) event.getScreenY());
        var area = new java.awt.Rectangle();
        area.setFrameFromDiagonal(currentDragScreen, point);

        var recorder = new Recorder(area);
        recorder.start(2);
    }
}
