package nl.romy.javafundamentalseindopdracht.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import nl.romy.javafundamentalseindopdracht.model.Database;
import nl.romy.javafundamentalseindopdracht.model.Sale;
import nl.romy.javafundamentalseindopdracht.model.Seat;
import nl.romy.javafundamentalseindopdracht.model.Showing;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class AgeCheckDialogController implements Initializable {
    @FXML
    private Label movieLabel;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private Label numberOfTicketsLabel;
    @FXML
    private Label customerNameLabel;
    @FXML
    private CheckBox confirmIdCheckBox;
    @FXML
    private Button confirmPurchaseButton;
    private Sale sale;
    private Database database;
    private List<Seat> seats;
    private Showing showing;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieLabel.setText(movieLabel.getText()+sale.getShowing().toString());
        dateTimeLabel.setText(dateTimeLabel.getText()+sale.getFormattedStartTime());
        numberOfTicketsLabel.setText(numberOfTicketsLabel.getText()+sale.getNumberOfTickets());
        customerNameLabel.setText(customerNameLabel.getText()+sale.getCustomerName());
    }
    public AgeCheckDialogController(Sale sale, Database database, List<Seat> seats, Showing showing) {
        this.sale=sale;
        this.database=database;
        this.seats=seats;
        this.showing=showing;
    }
    @FXML
    protected void checkBoxClicked(ActionEvent event) {
        if (confirmIdCheckBox.isSelected()) {
            confirmPurchaseButton.setDisable(false);
        }else{
        confirmPurchaseButton.setDisable(true);}
    }
    @FXML
    protected void onConfirmPurchaseButtonClick(ActionEvent event) {
        database.addSale(sale);
        for (Seat s : seats){
            showing.getRoom().addToSeatingChart(s.getRow(),s.getColumn());
        }
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void onCancelButtonClick(ActionEvent event){
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
