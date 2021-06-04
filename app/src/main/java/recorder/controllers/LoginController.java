package recorder.controllers;


import com.google.inject.Inject;
import com.typesafe.config.Config;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.json.JSONObject;
import recorder.core.Auth;
import recorder.core.Loader;
import recorder.core.NebulaApi;

import java.io.IOException;

public class LoginController {
    private final Config config;
    private final Loader loader;
    private final NebulaApi nebulaApi;
    private final Auth auth;

    @FXML
    private AnchorPane login;
    @FXML
    private Text error;
    @FXML
    private TextField email;
    @FXML
    private TextField password;

    @Inject
    public LoginController(Config config, Loader loader, NebulaApi nebulaApi, Auth auth) {
        this.config = config;
        this.loader = loader;
        this.nebulaApi = nebulaApi;
        this.auth = auth;
    }

    /**
     * Login to our service, save the jwt token and show the main view on success and an error message
     * if anything else than a 200 code is returned. Persist the returned token.
     */
    public void login(ActionEvent event) throws IOException {
        var payload = new JSONObject().put("email", email.getText()).put("password", password.getText());
        var response = nebulaApi.login(payload);

        if (response.code() != 200) {
            error.setVisible(true);
            email.setStyle("-fx-text-box-border: #dd0404; -fx-focus-color: #dd0404");
            password.setStyle("-fx-text-box-border: #dd0404; -fx-focus-color: #dd0404;");
            error.setText("Invalid credentials.");
            return;
        }

        auth.saveToken(response.body().string());
        login.getScene().setRoot(loader.get("main.fxml"));
    }

}
