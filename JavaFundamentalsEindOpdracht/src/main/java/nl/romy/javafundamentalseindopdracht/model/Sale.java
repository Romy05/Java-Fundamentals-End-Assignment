package nl.romy.javafundamentalseindopdracht.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Sale implements Comparable, Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime dateTime;
    private int numberOfTickets;
    private String customerName;
    private String formattedStartTime;
    private Showing showing;

    public Sale(String customerName, LocalDateTime dateTime, List<Seat> seats, Showing showing) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        this.customerName = customerName;
        this.dateTime = dateTime;
        this.numberOfTickets = seats.size();
        this.showing = showing;
        formattedStartTime= dateTime.format(timeFormatter);
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getFormattedStartTime() {
        return formattedStartTime;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }

    public Showing getShowing() {
        return showing;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public int compareTo(Object o) {

        if(o instanceof Sale){
           Sale s = (Sale)o;
           return s.getDateTime().compareTo(this.getDateTime());
        }
        return 0;
    }
}

