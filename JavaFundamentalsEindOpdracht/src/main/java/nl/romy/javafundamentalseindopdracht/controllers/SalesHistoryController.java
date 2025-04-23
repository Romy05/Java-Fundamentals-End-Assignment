package nl.romy.javafundamentalseindopdracht.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import nl.romy.javafundamentalseindopdracht.model.Database;
import nl.romy.javafundamentalseindopdracht.model.Sale;
import nl.romy.javafundamentalseindopdracht.model.Showing;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;


public class SalesHistoryController implements Initializable {

    private ObservableList<Sale> sales;
    private Database database;
    @FXML
    private TableView<Sale> salesHistoryTableView;
    public SalesHistoryController(Database database)
    {
        this.database = database;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.sales = FXCollections.observableList(this.database.getSales());
        FXCollections.sort(sales);
        salesHistoryTableView.setItems(sales);
        salesHistoryTableView.refresh();
    }

}
