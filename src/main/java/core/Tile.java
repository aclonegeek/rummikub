package core;

public class Tile {
    private char colour;
    private int value;
    
    public Tile (char colour, int value) {
        this.colour = colour;
        this.value = value;
    }
    
    public char getColour() {
        return Character.toUpperCase(this.colour);
    }
    
    public int getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.getColour()) + String.valueOf(this.getValue());
    }
}
