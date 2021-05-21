package recorder.controllers;


import com.google.inject.Inject;
import com.typesafe.config.Config;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.io.IOException;

public class LoginController {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final Config config;

    @FXML
    private AnchorPane login;
    @FXML
    private Text error;
    @FXML
    private TextField email;
    @FXML
    private TextField password;

    @Inject
    public LoginController(Config config) {
        this.config = config;
    }

    /**
     * Login to our service, save the jwt token and show the main view.
     */
    public void login(ActionEvent event) throws IOException {
        var client = new OkHttpClient().newBuilder().build();

        var payload = new JSONObject()
                .put("email", email.getText())
                .put("password", password.getText());

        var body = RequestBody.create(payload.toString(), JSON);
        var request = new Request.Builder()
                .url("http://localhost:8000/api/auth/login")
                .post(body)
                .build();
        var response = client.newCall(request).execute();

        if (response.code() != 200) {
            error.setVisible(true);
            email.setStyle("-fx-text-box-border: #dd0404; -fx-focus-color: #dd0404");
            password.setStyle("-fx-text-box-border: #dd0404; -fx-focus-color: #dd0404;");
            error.setText("Invalid credentials.");
            return;
        }

        var stage = login.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../main.fxml"));
        stage.setRoot(loader.load());
    }
}
