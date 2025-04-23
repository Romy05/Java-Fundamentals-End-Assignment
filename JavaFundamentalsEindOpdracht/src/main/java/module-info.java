module nl.romy.javafundamentalseindopdracht {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.management;
    requires java.desktop;


    opens nl.romy.javafundamentalseindopdracht.model to javafx.base;
    opens nl.romy.javafundamentalseindopdracht to javafx.fxml;
    exports nl.romy.javafundamentalseindopdracht;
    exports nl.romy.javafundamentalseindopdracht.controllers;
    opens nl.romy.javafundamentalseindopdracht.controllers to javafx.fxml;
}