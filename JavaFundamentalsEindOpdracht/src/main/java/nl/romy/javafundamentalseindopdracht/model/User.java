package nl.romy.javafundamentalseindopdracht.model;

import nl.romy.javafundamentalseindopdracht.model.enums.UserRole;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private UserRole userRole;
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    public User(String firstName, String lastName, String password, String username, UserRole userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getUserRole() {
        return userRole;
    }
}
