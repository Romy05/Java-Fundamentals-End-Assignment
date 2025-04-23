package nl.romy.javafundamentalseindopdracht;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.romy.javafundamentalseindopdracht.controllers.DashboardController;
import nl.romy.javafundamentalseindopdracht.controllers.LogInController;
import nl.romy.javafundamentalseindopdracht.model.Database;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("log-in-view.fxml"));
       fxmlLoader.setController(new LogInController(stage));
       Scene scene = new Scene(fxmlLoader.load(), 320, 140);
       stage.setTitle("Log in");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}