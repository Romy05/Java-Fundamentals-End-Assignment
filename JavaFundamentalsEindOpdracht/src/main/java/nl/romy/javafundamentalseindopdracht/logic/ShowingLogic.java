package nl.romy.javafundamentalseindopdracht.logic;

import javafx.stage.FileChooser;
import nl.romy.javafundamentalseindopdracht.model.Showing;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ShowingLogic {

    private List<Showing> showings;

    public ShowingLogic() {
        showings = new ArrayList<Showing>();
    }
    public void save()  {
        try (FileOutputStream fos = new FileOutputStream(new File("showings.dat"));
             ObjectOutputStream oos = new ObjectOutputStream(fos);){
            for(Showing showing:showings)
                oos.writeObject(showings);
        }
        catch (FileNotFoundException fnfe) {
            System.out.println("The file could not be found");
        }
        catch (IOException ioe) {
            System.out.println("An error occurred while saving the showings");
            ioe.printStackTrace();
        }
    }
    public void load() {
        FileInputStream fis = null;
        try { fis = new FileInputStream(new File("showings.dat"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.showings = (List<Showing>) ois.readObject();
        }
        catch(ClassNotFoundException cnfe){
            System.out.println("Could not find class");
        }
        catch (IOException ioe){
            System.out.println("An error occurred while loading the showings");
            System.out.println(ioe.toString());
            ioe.printStackTrace();
        }
    }
    public List<Showing> getShowings(){
        return showings;
    }
    public void writeCSV(List<Showing> writableShowings) throws Exception {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        Writer writer = null;
        try {
            File file = fileChooser.showSaveDialog(null);
            writer = new BufferedWriter(new FileWriter(file));
            for (Showing showing : writableShowings) {
                int amountOfSeatsTaken = showing.getAmountOfSoldSeats();
                String seatsLeftString = String.valueOf((showing.getAmountOfSeats()-amountOfSeatsTaken));
                String text = (showing.getFormattedStartTime() +","+showing.getFormattedEndTime()+","+showing.getTitle()+","+seatsLeftString+"\n" );

                writer.write(text);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {

            writer.flush();
            writer.close();
        }
    }
}
