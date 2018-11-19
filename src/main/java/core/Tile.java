package core;

import core.Globals.Colour;

public class Tile {
    private Colour colour;
    private int value;
    protected boolean isJoker = false;
    protected boolean onTable = false;
    
    public Tile (Colour colour, int value) {
        this.colour = colour;
        this.value = value;
    }
    
    public Tile (String tile) {
        this.colour = Colour.getEnum(tile.charAt(0));
        this.value = Integer.parseInt(tile.substring(1));
    }
    
    public Colour getColour() {
        return this.colour;
    }
    
    public int getValue() {
        return this.value;
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
    
    public boolean equals(Tile tile) {
        return this.colour == tile.getColour() && this.value == tile.getValue();
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.colour.getSymbol()) + String.valueOf(this.value);
    }
}