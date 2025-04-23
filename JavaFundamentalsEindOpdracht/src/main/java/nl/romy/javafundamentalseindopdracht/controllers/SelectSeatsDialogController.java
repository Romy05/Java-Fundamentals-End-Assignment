package nl.romy.javafundamentalseindopdracht.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.romy.javafundamentalseindopdracht.Application;
import nl.romy.javafundamentalseindopdracht.logic.SaleLogic;
import nl.romy.javafundamentalseindopdracht.logic.ShowingLogic;
import nl.romy.javafundamentalseindopdracht.logic.TextFormatLogic;
import nl.romy.javafundamentalseindopdracht.model.Database;
import nl.romy.javafundamentalseindopdracht.model.Sale;
import nl.romy.javafundamentalseindopdracht.model.Seat;
import nl.romy.javafundamentalseindopdracht.model.Showing;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class SelectSeatsDialogController implements Initializable {
    private Showing showing;
    @FXML
    private Button selectSeatsSellButton;
    @FXML
    private Button selectSeatsCancelButton;
    @FXML
    private Label selectedShowingLabel;
    @FXML
    private GridPane selectSeatsGridPane;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private ListView<Seat> selectedSeatsListView;
    private ObservableList<Seat> seats;
    private Database database;
    private TextFormatLogic textFormatLogic;
    private ShowingLogic showingLogic;
    private SaleLogic saleLogic;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    editSellButton();
                });
        customerNameTextField.setTextFormatter(new TextFormatter<>(textFormatLogic::onlyAllowLetterFormat));
        seats = FXCollections.observableList(new ArrayList<Seat>());
        selectedSeatsListView.setItems(seats);
        selectedShowingLabel.setText(selectedShowingLabel.getText() + showing.toString());
        generateSeatingChart();
    }
    private void generateSeatingChart(){
        int[][] seatingChart = showing.getRoom().getSeatingChart();
        for (int i=0;i<seatingChart[1].length+1;i++)
        {
            for (int j=1;j<seatingChart.length+1;j++)
            {
                if (i==0){
                    Label rowLabel = new Label();
                    rowLabel.setText("Row "+ j);
                    selectSeatsGridPane.add(rowLabel,i,j);
                    selectSeatsGridPane.setMargin(rowLabel,new Insets(4));

                }else {
                    Button seatButton = new Button();

                    if (seatingChart[j-1][i-1]==0 ){
                        seatButton.setStyle("-fx-background-color: #808080FF;");
                        int row = j-1;
                        int column = i-1;
                        seatButton.setOnAction(event -> selectOrDeselectSeat(event, new Seat(row,column)));
                    }
                    else seatButton.setStyle("-fx-background-color: #ff0000;");
                    seatButton.setText(String.valueOf(i));
                    selectSeatsGridPane.add(seatButton, i, j);
                    selectSeatsGridPane.setMargin(seatButton, new Insets(1));
                }
            }
        }
    }
    private void selectOrDeselectSeat(ActionEvent actionEvent, Seat seat) {
        Button button = (Button) actionEvent.getSource();
        if (Objects.equals(button.getStyle(), "-fx-background-color: #808080FF;")){
            button.setStyle("-fx-background-color: #00ff00;");
            seats.add(seat);
        } else if (Objects.equals(button.getStyle(), "-fx-background-color: #00ff00;")){
            button.setStyle("-fx-background-color: #808080FF;");
            Seat removableSeat = new Seat();
            for (Seat s : seats){
                if (s.toString().equals(seat.toString())){
                    removableSeat=s; //seats.remove(seat) werkte niet.
                }
            }
            if(removableSeat!=null)
            seats.remove(removableSeat);

            selectedSeatsListView.refresh();
        }
        editSellButton();
    }
    private void editSellButton(){
        if(!customerNameTextField.getText().isEmpty())
        selectSeatsSellButton.setDisable(false);
        else selectSeatsSellButton.setDisable(true);

        switch(seats.size()){
            case 0: selectSeatsSellButton.setDisable(true); selectSeatsSellButton.setText("Sell tickets"); break;
            case 1: selectSeatsSellButton.setText("Sell 1 ticket");break;
            default: selectSeatsSellButton.setText("Sell " + seats.size()+ " tickets");break;
        }
    }

    public SelectSeatsDialogController(Showing showing, Database database) {
        this.showing = showing;
        this.database = database;
        this.textFormatLogic = new TextFormatLogic();
        this.saleLogic = database.getSaleLogic();
        this.showingLogic = database.getShowingLogic();
    }
    @FXML
    protected void onSellButtonClick(ActionEvent event){
        Sale sale =new Sale(customerNameTextField.getText(), LocalDateTime.now(),seats.stream().toList(),showing);
        if(showing.getIsSixteenPlus()){
            try {
                AgeCheckDialogController controller = new AgeCheckDialogController(sale,database, seats,showing);
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("age-check-view.fxml"));
                fxmlLoader.setController(controller);
                Scene scene = new Scene(fxmlLoader.load());
                Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.setScene(scene);
                dialog.setTitle("Age check");
                dialog.showAndWait();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            database.addSale(sale);
            for (Seat s : seats){
                showing.getRoom().addToSeatingChart(s.getRow(),s.getColumn());
            }
        }

        showingLogic.save();
        saleLogic.save();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void onCancelButtonClick(ActionEvent event){
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

}
