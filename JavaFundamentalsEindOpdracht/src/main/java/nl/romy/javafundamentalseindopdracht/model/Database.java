package nl.romy.javafundamentalseindopdracht.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.romy.javafundamentalseindopdracht.logic.SaleLogic;
import nl.romy.javafundamentalseindopdracht.logic.ShowingLogic;
import nl.romy.javafundamentalseindopdracht.logic.UserLogic;

import java.util.List;

public class Database {
    private List<User> users;
    private ObservableList<Showing> showings;
    private ObservableList<Sale> sales;
    private ShowingLogic showingLogic;
    private UserLogic userLogic;
    private SaleLogic saleLogic;

    public Database() {
        this.showingLogic = new ShowingLogic();
        this.userLogic = new UserLogic();
        this.saleLogic = new SaleLogic();
        userLogic.load();
        showingLogic.load();
        this.users = userLogic.getUsers();
        this.showings = FXCollections.observableList(showingLogic.getShowings());
        saleLogic.load();
        this.sales = FXCollections.observableList(saleLogic.getSales());
    }

    public ShowingLogic getShowingLogic() {
        return showingLogic;
    }

    public UserLogic getUserLogic() {
        return userLogic;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Showing> getShowings() {
        return showings;
    }
    public List<Sale> getSales() {
        return sales;
    }

    public SaleLogic getSaleLogic() {

        return saleLogic;
    }

    public void addSale(Sale sale)
    {
        this.sales.add(sale);
    }
    public void deleteShowing(Showing showing) {
        showings.remove(showing);
    }
    public void addShowing(Showing showing) {
        showings.add(showing);
    }

}
