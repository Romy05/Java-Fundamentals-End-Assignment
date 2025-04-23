package nl.romy.javafundamentalseindopdracht.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.romy.javafundamentalseindopdracht.Application;
import nl.romy.javafundamentalseindopdracht.logic.ShowingLogic;
import nl.romy.javafundamentalseindopdracht.model.Database;
import nl.romy.javafundamentalseindopdracht.model.Sale;
import nl.romy.javafundamentalseindopdracht.model.Showing;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class ManageShowingsController implements Initializable {
    @FXML
    private TableView<Showing> showingsTableView;
    @FXML
    private VBox manageShowingsScreen;
    @FXML
    private Label manageShowingsErrorLabel;
    @FXML
    private Button editShowingButton;
    @FXML
    private Button deleteShowingButton;
    @FXML
    private TableColumn<Showing, LocalDateTime> startTimeColumn;
    @FXML
    private TableColumn<Showing,String> soldSeatsColumn;
    private Database database;
    private ObservableList<Showing> showings;
    private ShowingLogic showingLogic;

    public ManageShowingsController(ObservableList<Showing> showings, Database database) {
        this.database = database;
        this.showings = showings;
        this.showingLogic=database.getShowingLogic();
    }

    private void initializeManageShowingsScreen() {
        showingsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            editShowingButton.setDisable(false);
            deleteShowingButton.setDisable(false);
        });
        this.showings = FXCollections.observableList(database.getShowings());
        manageShowingsErrorLabel.setText(null);
        manageShowingsScreen.setVisible(true);
        showingsTableView.setItems(showings);
        soldSeatsColumn.setCellValueFactory(cellData ->{
            Showing s = cellData.getValue();
            int amountOfSeatsTaken = s.getAmountOfSoldSeats();
            String seatsLeftString = (s.getAmountOfSeats()-amountOfSeatsTaken+"/"+s.getAmountOfSeats());
            return new SimpleStringProperty(seatsLeftString);
        });
        showingsTableView.getSortOrder().add(startTimeColumn);
        startTimeColumn.setSortType(TableColumn.SortType.ASCENDING);
        showingsTableView.refresh();
    }

    @FXML
    protected void onAddShowingsButtonClick(ActionEvent event) {
        makeEditOrAddDialog(new AddEditShowingDialogController(showings, database));
    }
    @FXML
    protected void onEditShowingsButtonClick(ActionEvent event) {
            makeEditOrAddDialog(new AddEditShowingDialogController(showings, database, showingsTableView.getSelectionModel().getSelectedItem()) );
    }

    private void makeEditOrAddDialog(AddEditShowingDialogController controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("add-edit-showing-view.fxml"));
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load());
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(scene);
            dialog.setTitle(controller.getAction().toString()+" showing");
            dialog.showAndWait();
            initializeManageShowingsScreen();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onDeleteShowingsButtonClick(ActionEvent event) {
        Showing showing = showingsTableView.getSelectionModel().getSelectedItem();
            if (showing.getAmountOfSoldSeats()==0) {
                showingsTableView.getItems().remove(showing);
                database.deleteShowing(showing);
                initializeManageShowingsScreen();

                manageShowingsErrorLabel.setText(null);
                showMessage("The showing has been deleted succesfully");
            }
            else {
                manageShowingsErrorLabel.setText("This showing cannot be deleted,\ntickets are already sold for showing.");
            }
    }
    private void showMessage(String message){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Message");
        Button button = new Button("Close");
        button.setPadding(new Insets(5, 30, 5, 30));
        button.setOnAction(e -> stage.close());
        VBox dialogVBox = new VBox(new Label(message), button);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setSpacing(10);
        Scene dialogScene = new Scene(dialogVBox, 250, 100);
        stage.setScene(dialogScene);
        stage.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeManageShowingsScreen();
    }
    @FXML
    protected void onExportShowingsButtonClick(ActionEvent event) throws Exception {
        List<Showing> showingList=showingsTableView.getItems();
        showingLogic.writeCSV(showingList);
    }

}
