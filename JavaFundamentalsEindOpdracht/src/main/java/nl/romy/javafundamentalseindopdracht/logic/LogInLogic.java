package nl.romy.javafundamentalseindopdracht.logic;

import nl.romy.javafundamentalseindopdracht.model.Database;
import nl.romy.javafundamentalseindopdracht.model.User;

import java.util.List;

public class LogInLogic {
    Database db;

    public LogInLogic() {
        this.db = new Database();
    }

    public boolean Authenticate(String username, String password){
        User currentUser = GetUserByUsername(username);

        if ((currentUser==null)||(!currentUser.getPassword().equals(password)))
            return false;
        return true;
    }
    public User GetUserByUsername(String username){
        List<User> users = db.getUsers();
        User currentUser = null;
        for(User user : users){
            if (user.getUsername().equals(username)){
                currentUser = user;
                break;
            }
        }
        return currentUser;
    }
}
