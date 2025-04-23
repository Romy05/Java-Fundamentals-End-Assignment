package nl.romy.javafundamentalseindopdracht.model;

import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private int[][] seatingChart;

    public Room(int rows, int columns) {
        this.seatingChart = new int[rows][columns];
    }

    public int[][] getSeatingChart() {
        return seatingChart;
    }
    public void addToSeatingChart(int row, int column) {
        seatingChart[row][column]++;
    }
}
