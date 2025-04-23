package nl.romy.javafundamentalseindopdracht.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import nl.romy.javafundamentalseindopdracht.Application;
import nl.romy.javafundamentalseindopdracht.logic.LogInLogic;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorText;
    private Stage stage;

    public LogInController(Stage stage){
        this.stage = stage;
    }

    @FXML
    protected void onLogInButtonClick(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        LogInLogic logInLogic = new LogInLogic();
        boolean logInSuccesful = logInLogic.Authenticate(username, password);
        if(logInSuccesful) {
            //log in
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("dashboard-view.fxml"));
            DashboardController dashboardController = new DashboardController(logInLogic.GetUserByUsername(username));
            fxmlLoader.setController(dashboardController);
            Scene scene = new Scene(fxmlLoader.load(), 700, 400);
            stage.setTitle("Dashboard");
            stage.setScene(scene);
            stage.show();

        }else {
            errorText.setVisible(true);
            passwordField.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
