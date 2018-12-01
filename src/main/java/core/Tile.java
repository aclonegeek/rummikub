package core;

import java.util.ArrayList;

import core.Globals.Colour;

public class Tile {
    protected Colour colour;
    protected int value;
    protected boolean onTable = false;
    
    // Jokers
    private ArrayList<Tile> alternateState;
    private boolean isJoker = false;
    private boolean isReplaced = false;

    public Tile(Colour colour, int value) {
        this.colour = colour;
        this.value = value;
    }
    
    public Tile(Tile tile) {
        this.colour = tile.colour;
        this.value = tile.value;
        this.onTable = tile.onTable;
        this.isJoker = tile.isJoker;
        this.isReplaced = tile.isReplaced;
        
        if (this.isJoker) {
            alternateState = new ArrayList<>();
            for (Tile alternateTile : tile.alternateState) {
                alternateState.add(new Tile(alternateTile));
            }
        }
    }

    public Tile(String tile) {
        if (tile.equals("J")) {
            this.isJoker = true;
            this.colour = null;
            this.value = 0;
            alternateState = new ArrayList<>();
        } else {
            this.colour = Colour.getEnum(tile.charAt(0));
            this.value = Integer.parseInt(tile.substring(1));
        }
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
            this.isReplaced = true;
            this.colour = null;
            this.value = 0;
        }
    }
    
    public boolean isReplaced() {
        return this.isReplaced;
    }
    
    public void setIsReplaced(boolean isReplaced) {
        this.isReplaced = isReplaced;
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