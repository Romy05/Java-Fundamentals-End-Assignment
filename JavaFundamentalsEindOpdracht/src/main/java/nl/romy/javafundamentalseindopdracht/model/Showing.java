package nl.romy.javafundamentalseindopdracht.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Showing implements Serializable {
    private static final long serialVersionUID = 1L;
    private int amountOfSeats;
    private Room room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String formattedStartTime;
    private String formattedEndTime;
    private String title;
    private boolean isSixteenPlus;

    public boolean getIsSixteenPlus() {
        return isSixteenPlus;
    }

    public void setIsSixteenPlus(boolean sixteenPlus) {
        isSixteenPlus = sixteenPlus;
    }

    public Showing(LocalDateTime startTime, LocalDateTime endTime, String title , Room room, boolean isSixteenPlus)
    {
        this.isSixteenPlus = isSixteenPlus;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.startTime = startTime;
        this.endTime = endTime;
        this.formattedEndTime = endTime.format(timeFormatter);
        this.formattedStartTime = startTime.format(timeFormatter);
        this.title=title;
        this.room = room;
        this.amountOfSeats = room.getSeatingChart().length * room.getSeatingChart()[0].length;
    }

    public String getFormattedEndTime() {
        return formattedEndTime;
    }

    public String getFormattedStartTime() {
        return formattedStartTime;
    }

    public int getAmountOfSeats() {
        return amountOfSeats;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    public int getAmountOfSoldSeats(){
        int amountOfSeats= 0;
        for(int i=0; i<this.room.getSeatingChart().length; i++){
            for(int j=0; j<this.room.getSeatingChart()[i].length; j++){
                if (this.room.getSeatingChart()[i][j] == 1)
                    amountOfSeats++;
            }
        }
        return amountOfSeats;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public String getTitle() {
        return title;
    }

    public Showing updateShowing(LocalDateTime startDateTime, LocalDateTime endDateTime, String title) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.startTime = startDateTime;
        this.endTime = endDateTime;
        this.title = title;
        this.formattedEndTime = this.endTime.format(timeFormatter);
        this.formattedStartTime = this.startTime.format(timeFormatter);

        return this;
    }
    @Override
    public String toString(){
        return (formattedStartTime + " " + title);
    }
}
