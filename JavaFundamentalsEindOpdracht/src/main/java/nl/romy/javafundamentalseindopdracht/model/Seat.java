package nl.romy.javafundamentalseindopdracht.model;

import java.io.Serializable;

public class Seat implements Serializable {
    private static final long serialVersionUID = 1L;
    private int row;
    private int column;

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
    }
    public Seat(){}
    @Override
    public String toString() {
        return ("Row "+ (row+1) + " / Seat "+(column+1));
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
