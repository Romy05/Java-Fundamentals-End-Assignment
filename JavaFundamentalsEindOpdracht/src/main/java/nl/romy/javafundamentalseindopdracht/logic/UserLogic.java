package nl.romy.javafundamentalseindopdracht.logic;

import nl.romy.javafundamentalseindopdracht.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserLogic {
    private List<User> users;

    public UserLogic() {
        users = new ArrayList<User>();
    }
    public void save()  {
        try (FileOutputStream fos = new FileOutputStream(new File("users.dat"));
             ObjectOutputStream oos = new ObjectOutputStream(fos);){
            for(User user: users)
                oos.writeObject(users);
        }
        catch (FileNotFoundException fnfe) {
            System.out.println("The file could not be found");
        }
        catch (IOException ioe) {
            System.out.println("An error occurred while saving the items");
        }
    }
    public void load() {
        FileInputStream fis = null;
        try { fis = new FileInputStream(new File("users.dat"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (List<User>)ois.readObject();
        }
        catch(ClassNotFoundException cnfe){
            System.out.println("Could not find class");
        }
        catch (IOException ioe){
            System.out.println("An error occurred while loading the items");
        }
    }
    public List<User> getUsers(){
        return users;
    }
}
