package nl.romy.javafundamentalseindopdracht.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import nl.romy.javafundamentalseindopdracht.Application;
import nl.romy.javafundamentalseindopdracht.model.*;
import nl.romy.javafundamentalseindopdracht.model.enums.UserRole;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private Database db = new Database();
    private ObservableList<Showing> showings = FXCollections.observableList(db.getShowings());
    @FXML
    private VBox layout;
    @FXML
    private Label welcomeText;
    @FXML
    private Label LoggedInText;
    @FXML
    private Label CurrentDateTimeText;
    @FXML
    private Button manageShowingsButton;
    @FXML
    private  Button viewSalesHistoryButton;
    @FXML
    private Button sellTicketsButton;
    @FXML
    private VBox welcomeScreen;
    private User currentUser;

    public DashboardController(User user){
        this.currentUser = user;

    }
    @FXML
    protected void onManageShowingsButtonClick(ActionEvent event) {
        loadScene("manage-showings-view.fxml",new ManageShowingsController(showings,db));
        setMenuButtonsDefaultColour();
        manageShowingsButton.setStyle("-fx-background-color: #b8b0b0;");
    }
    private void initializeWelcomeScreen(){
        welcomeScreen.setVisible(true);
        welcomeText.setText("Welcome " + currentUser.getFirstName() + " " + currentUser.getLastName());
        LoggedInText.setText("You are logged in as "+ currentUser.getUserRole().toString());
        updateTimeLabel();
        doSomethingEveryMinute().play();
        if (currentUser.getUserRole()== UserRole.Management){
            sellTicketsButton.setVisible(false);
        }else if(currentUser.getUserRole()== UserRole.Sales){
            viewSalesHistoryButton.setVisible(false);
            manageShowingsButton.setVisible(false);
        }
    }
    private Timeline doSomethingEveryMinute(){
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(60), event -> updateTimeLabel()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        return timeline;
    }
    private void updateTimeLabel(){
        LocalDateTime time = LocalDateTime.now();
        CurrentDateTimeText.setText("The current date and time is "+ time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
    }
    public void loadScene(String name, Object controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(name));
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load());
            if (layout.getChildren().size() > 1)
                layout.getChildren().remove(1);
            layout.getChildren().add(scene.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onSellTicketsButtonClick(ActionEvent event) {
        loadScene("sell-tickets-view.fxml", new SellTicketsController(showings, db));
        setMenuButtonsDefaultColour();
        this.sellTicketsButton.setStyle("-fx-background-color: #b8b0b0;");
    }
    private void setMenuButtonsDefaultColour(){
        String style = "-fx-background-color: #5454aa;";
        manageShowingsButton.setStyle(style);
        viewSalesHistoryButton.setStyle(style);
        sellTicketsButton.setStyle(style);
    }
    @FXML
    protected void onViewSalesButtonClick(ActionEvent event){
        loadScene("sales-history-view.fxml",new SalesHistoryController(db));
        setMenuButtonsDefaultColour();
        viewSalesHistoryButton.setStyle("-fx-background-color: #b8b0b0;");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeWelcomeScreen();
        this.db = new Database();
    }
}
