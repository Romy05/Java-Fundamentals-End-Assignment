package nl.romy.javafundamentalseindopdracht.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.romy.javafundamentalseindopdracht.logic.ShowingLogic;
import nl.romy.javafundamentalseindopdracht.logic.TextFormatLogic;
import nl.romy.javafundamentalseindopdracht.model.Database;
import nl.romy.javafundamentalseindopdracht.model.Room;
import nl.romy.javafundamentalseindopdracht.model.Showing;
import nl.romy.javafundamentalseindopdracht.model.enums.AddOrEditAction;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AddEditShowingDialogController implements Initializable {

    @FXML
    private TextField addEditShowingsTitleTextField;
    @FXML
    private DatePicker addEditShowingsStartDatePicker;
    @FXML
    private TextField addEditShowingsStartTimeTextField;
    @FXML
    private TextField addEditShowingsDurationTextField;
    @FXML
    private Label addEditShowingsEndDateAndTimeLabel;
    @FXML
    private Label addEditTitleText;
    @FXML
    private Button addEditShowingButton;
    @FXML
    private VBox layout;
    @FXML
    private CheckBox movieIsSixteenPlusCheckBox;
    private ObservableList<Showing> showings;
    private TextFormatLogic textFormatLogic = new TextFormatLogic();
    private ShowingLogic showingLogic;

    private Showing showing;
    private Database database;
    private AddOrEditAction action;

    public AddOrEditAction getAction() {
        return action;
    }

    public AddEditShowingDialogController(ObservableList<Showing> showings, Database database) {
        this.showings = showings;
        this.database = database;
        this.action = AddOrEditAction.Add;

    }
    public AddEditShowingDialogController(ObservableList<Showing> showings, Database database, Showing s){
        this.showings = showings;
        this.showing = s;
        this.database = database;
        this.action = AddOrEditAction.Edit;
    }
    public Showing getShowing() {
        return showing;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.showingLogic = database.getShowingLogic();
        initializeFormatFields();
        addEditShowingsDurationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            displayEndTime();
        });
        addEditShowingsStartTimeTextField.textProperty().addListener((observable,oldValue,newValue) -> {
            displayEndTime();
        });
        if(action == AddOrEditAction.Add){
            addEditTitleText.setText("Add showing");
            addEditShowingButton.setText("Add showing");
        }
        else {
            addEditTitleText.setText("Edit showing");
            addEditShowingButton.setText("Edit showing");
        }
        if(action == AddOrEditAction.Add){
            initializeAddShowingsScreen();
        }
        else initializeEditShowingsScreen(showing);
    }
    private void initializeAddShowingsScreen(){
        addEditShowingsStartDatePicker.setValue(LocalDate.now());
        addEditShowingButton.setDisable(true);
    }
    private void initializeFormatFields(){
        addEditShowingsStartTimeTextField.setTextFormatter(new TextFormatter<>(textFormatLogic::onlyAllowTimeFormat));
        addEditShowingsDurationTextField.setTextFormatter(new TextFormatter<>(textFormatLogic::onlyAllowNumberFormat));
        addEditShowingsStartDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0 );
            }
        });
    }
    private void initializeEditShowingsScreen(Showing showing){
        addEditShowingsTitleTextField.setText(showing.getTitle());
        addEditShowingsStartDatePicker.setValue(showing.getStartTime().toLocalDate());
        addEditShowingsStartTimeTextField.setText(showing.getStartTime().format(DateTimeFormatter.ofPattern("H:m")));
        int duration = calculateShowingDurationInMinutes(showing.getStartTime().toLocalTime(),showing.getEndTime().toLocalTime());
        addEditShowingsDurationTextField.setText(String.valueOf(duration));
        displayEndTime();
    }
    private int calculateShowingDurationInMinutes(LocalTime startTime, LocalTime endTime){
        final int showingBreak= 15;
        LocalTime duration = endTime.minusHours(startTime.getHour()).minusMinutes(startTime.getMinute());
        duration = duration.minusMinutes(showingBreak);
        int minutes = duration.getMinute();
        minutes = minutes + (duration.getHour()*60);
        return minutes;
    }
    @FXML
    protected void displayEndTime() {
            try{
                LocalDateTime startDateTime = getStartDateTime(getStartTime());
                LocalTime duration = getDuration();
                LocalDateTime endDateTime = calculateEndDateTime(duration, startDateTime);
                if (startDateTime.isBefore(LocalDateTime.now())) {
                addEditShowingsEndDateAndTimeLabel.setText("Please enter a time in the future!");
                addEditShowingButton.setDisable(true);
                }
                else {
                    addEditShowingsEndDateAndTimeLabel.setText("The end time including break is:\n" + endDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                    addEditShowingButton.setDisable(false);
                }
            }catch(DateTimeParseException | NullPointerException e){
                addEditShowingsEndDateAndTimeLabel.setText("Please enter a valid time and duration.");
                addEditShowingButton.setDisable(true);
            }
    }
    private LocalTime getStartTime(){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m");
            return LocalTime.parse(addEditShowingsStartTimeTextField.getText(),formatter);
        }catch(DateTimeParseException e){
            return null;
        }
    }
    private LocalTime getDuration(){
        try {
            return convertIntToDuration(Integer.parseInt(addEditShowingsDurationTextField.getText()));
        }catch(Exception e){
            return null;
        }
    }
    private LocalDateTime getStartDateTime(LocalTime startTime){
        try {
            LocalDateTime startDateTime = addEditShowingsStartDatePicker.getValue().atTime(startTime);
            return startDateTime;
        }catch(Exception e){
           return null;
        }
    }
    @FXML
    protected void onAddEditShowingButtonClick(ActionEvent event) {

        String title = addEditShowingsTitleTextField.getText();
        LocalTime duration = getDuration();
        LocalDateTime startDateTime = getStartDateTime(getStartTime());
        LocalDateTime endDateTime = calculateEndDateTime(duration, startDateTime);
        boolean movieIsSixteenPlus =false;
        if(movieIsSixteenPlusCheckBox.isSelected())
            movieIsSixteenPlus=true;

        if (action == AddOrEditAction.Add) {
                database.addShowing(new Showing(startDateTime, endDateTime, title, new Room(6,12),movieIsSixteenPlus));
        }
        else if(action ==AddOrEditAction.Edit){
           showing.updateShowing(startDateTime, endDateTime, title);
        }
            showingLogic.save();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
    }
    private LocalTime convertIntToDuration(int minutes){
        return LocalTime.of(minutes / 60, minutes % 60);
    }
    private LocalDateTime calculateEndDateTime(LocalTime duration, LocalDateTime startDateTime){
        if(duration==null)
            return null;
        final int showingBreak= 15;
        LocalDateTime endTime = startDateTime.plusHours(duration.getHour());
        endTime = endTime.plusMinutes(duration.getMinute());
        endTime = endTime.plusMinutes(showingBreak);
        return endTime ;
    }
    @FXML
    protected void onCancelEditOrAddShowingButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
