package core;

import core.Globals.Colour;

public class Tile {
    private Colour colour;
    private int value;
    
    public Tile (Colour colour, int value) {
        this.colour = colour;
        this.value = value;
    }
    
    public char getColour() {
        return this.colour.getSymbol();
    }
    
    public int getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.colour.getSymbol()) + String.valueOf(this.value);
    }
}