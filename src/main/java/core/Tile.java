package core;

import java.util.ArrayList;

import core.Globals.Colour;

public class Tile {
    protected Colour colour;
    protected int value;
    protected ArrayList<Tile> alternateState;
    protected boolean isJoker = false;
    protected boolean onTable = false;

    public Tile(Colour colour, int value) {
        this.colour = colour;
        this.value = value;
    }

    public Tile(String tile) {
        if (tile.equals("J")) {
            this.isJoker = true;
            this.colour = null;
            this.value = 0;
            alternateState = new ArrayList<>();
        }
        this.colour = Colour.getEnum(tile.charAt(0));
        this.value = Integer.parseInt(tile.substring(1));
    }

    public Colour getColour() {
        return this.colour;
    }

    public int getValue() {
        return this.value;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isOnTable() {
        return this.onTable;
    }

    public void setOnTable(boolean onTable) {
        this.onTable = onTable;
    }

    public boolean isJoker() {
        return this.isJoker;
    }

    public void releaseJoker() {
        if (this.isJoker) {
            this.colour = null;
            this.value = 0;
        }
    }

    public void addAlternateState(Tile alternateState) {
        this.alternateState.add(alternateState);
    }
    
    public ArrayList<Tile> getAlternateState() {
        return this.alternateState;
    }

    public boolean equals(Tile tile) {
        return this.colour == tile.getColour() && this.value == tile.getValue();
    }

    public boolean jokerEquals(Tile tile) {
        return (this.colour == tile.getColour() && this.value == tile.getValue())
                || this.alternateState.stream().anyMatch(t -> t.equals(tile));
    }

    @Override
    public String toString() {
        if (this.isJoker) {
            return "J";
        }
        return String.valueOf(this.colour.getSymbol()) + String.valueOf(this.value);
    }
}