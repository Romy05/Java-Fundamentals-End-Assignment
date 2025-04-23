package nl.romy.javafundamentalseindopdracht.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.romy.javafundamentalseindopdracht.Application;
import nl.romy.javafundamentalseindopdracht.model.Database;
import nl.romy.javafundamentalseindopdracht.model.Showing;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class SellTicketsController implements Initializable {
    private ObservableList<Showing> showings;
    private Showing selectedShowing;
    private Database database;
    @FXML
    private TableView<Showing> salesTableView;
    @FXML
    private Label sellTicketsSelectedLabel;
    @FXML
    private Label sellTicketsErrorLabel;
    @FXML
    private Button selectSeatsButton;
    @FXML
    private TableColumn<Showing, String> seatsLeftColumn;
    @FXML
    private TextField searchBarTextField;
    public SellTicketsController(ObservableList<Showing> showings, Database database) {
        this.showings = showings;
        this.database = database;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FilteredList<Showing> filteredShowings = new FilteredList<>(showings,showing -> true);
        searchBarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredShowings.setPredicate(showing->{
                if (newValue.length()<3){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return showing.getTitle().toLowerCase().contains(lowerCaseFilter);
            });
            salesTableView.setItems(filteredShowings);
        });

        seatsLeftColumn.setCellValueFactory(cellData ->{
            Showing s = cellData.getValue();
            int amountOfSeatsTaken = s.getAmountOfSoldSeats();
            String seatsLeftString = (s.getAmountOfSeats()-amountOfSeatsTaken+"/"+s.getAmountOfSeats());
            return new SimpleStringProperty(seatsLeftString);
        });
        seatsLeftColumn.setCellFactory(column -> new TableCell<Showing, String>(){
            @Override
            protected void updateItem(String seatsLeft, boolean empty) {
                super.updateItem(seatsLeft, empty);
                if(empty || seatsLeft == null) {
                    setText(null);
                }
                else {
                    setText(seatsLeft);
                }
            }
        });
        initializeSellTicketsScreen();
    }
    private void initializeSellTicketsScreen(){
        salesTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showSelectedShowing();
        });
        initializeSalesTableView();
    }
    private void initializeSalesTableView(){
        this.showings = FXCollections.observableList(database.getShowings());
        FilteredList<Showing> filteredShowings = new FilteredList<>(showings,showing -> showing.getStartTime().isAfter(LocalDateTime.now()));
        salesTableView.setItems(filteredShowings);

        salesTableView.sort();
        salesTableView.refresh();
    }
    private void showSelectedShowing() {

        sellTicketsErrorLabel.setText(null);
        selectedShowing = salesTableView.getSelectionModel().getSelectedItem();
        selectSeatsButton.setDisable(false);
        sellTicketsSelectedLabel.setText("Selected: " + selectedShowing.toString());

    }
    @FXML
    protected void onSelectSeatsButtonClicked(ActionEvent event) {

        if (selectedShowing != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("select-seats-view.fxml"));
                fxmlLoader.setController(new SelectSeatsDialogController(selectedShowing, database));
                Scene scene = new Scene(fxmlLoader.load());
                Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.setScene(scene);
                dialog.setTitle("Select seats");
                dialog.showAndWait();
                salesTableView.refresh();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else sellTicketsErrorLabel.setText("Select a showing first.");
    }
}
