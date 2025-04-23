package nl.romy.javafundamentalseindopdracht.logic;

import javafx.scene.control.TextFormatter;

public class TextFormatLogic {
    public TextFormatter.Change onlyAllowTimeFormat(TextFormatter.Change change){
        String textFieldInput = change.getControlNewText();

        if (textFieldInput.length() > 5) {
            return null;
        }
        if(textFieldInput.matches("\\d{0,2}(:\\d{0,2})?")) {
            if (textFieldInput.length() == 2 && !textFieldInput.contains(":") && !change.isDeleted()) {
                change.setText(textFieldInput + ":");
                change.setCaretPosition(textFieldInput.length()+1);
                change.setRange(0,1);
                return change;
            }

            return change;
        }
        return null;
    }
    public TextFormatter.Change onlyAllowNumberFormat(TextFormatter.Change change){
        String textFieldInput = change.getControlNewText();
        if (textFieldInput.matches("\\d*")){
            return change;
        }
        return null;
    }
    public TextFormatter.Change onlyAllowLetterFormat(TextFormatter.Change change){
        String textFieldInput = change.getControlNewText();
        if (textFieldInput.matches("[a-zA-Z\\s]+") || change.isDeleted()) {
            return change;
        }
        return null;
    }
}
