package nl.romy.javafundamentalseindopdracht.logic;

import nl.romy.javafundamentalseindopdracht.model.Sale;
import nl.romy.javafundamentalseindopdracht.model.Showing;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaleLogic {
    private List<Sale> sales;

    public SaleLogic() {
        sales = new ArrayList<Sale>();
    }
    public void save()  {
        try (FileOutputStream fos = new FileOutputStream(new File("sales.dat"));
             ObjectOutputStream oos = new ObjectOutputStream(fos);){
            for(Sale sale: sales)
                oos.writeObject(sales);
        }
        catch (FileNotFoundException fnfe) {
            System.out.println("The file could not be found");
        }
        catch (IOException ioe) {
            System.out.println("An error occurred while saving the sales");
            ioe.printStackTrace();
        }
    }
    public void load() {
        FileInputStream fis = null;
        try { fis = new FileInputStream(new File("sales.dat"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.sales = (List<Sale>) ois.readObject();
        }
        catch(ClassNotFoundException cnfe){
            System.out.println("Could not find class");
        }
        catch (IOException ioe){
            System.out.println("An error occurred while loading the sales");
            System.out.println(ioe.toString());
            ioe.printStackTrace();
        }
    }
    public List<Sale> getSales(){
        return sales;
    }
}
