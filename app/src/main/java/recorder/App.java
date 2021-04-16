package recorder;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        setupGui(stage);
    }


    /**
     * Setup the main Recorder GUI window with all the basic controls.
     */
    protected void setupGui(Stage stage) {
        Button button = new Button("select recording area");
        button.setOnAction(event -> new RecordingArea(App.this));

        VBox layout = new VBox(10);
        layout.getChildren().setAll(button);
        layout.setPadding(new Insets(10));

        stage.setScene(new Scene(layout, 160, 100));
        stage.show();
    }


}
