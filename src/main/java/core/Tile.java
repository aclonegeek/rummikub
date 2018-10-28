package core;

import core.Globals.Colour;

public class Tile {
    private Colour colour;
    private int value;
    private boolean onTable;
    
    public Tile (Colour colour, int value) {
        this.colour = colour;
        this.value = value;
        this.onTable = false;
    }
    
    public Tile (String tile) {
        this.colour = Colour.getEnum(tile.charAt(0));
        this.value = Character.getNumericValue(tile.charAt(1));
        this.onTable = false;
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
    
    @Override
    public String toString() {
        return String.valueOf(this.colour.getSymbol()) + String.valueOf(this.value);
    }
}