package core;

import core.Globals.Colour;

public class Tile implements Comparable<Tile>{
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
        this.value = Integer.parseInt(tile.substring(1));
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
    
    public boolean equals(Tile tile) {
        return this.colour == tile.getColour() && this.value == tile.getValue();
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.colour.getSymbol()) + String.valueOf(this.value);
    }
    
    @Override
    // If values equal, compare colour values instead (O > G > B > R)
    public int compareTo(Tile tile) {
      if (this.value != tile.getValue()) {
          return Integer.compare(this.value, tile.getValue());
      }
      return Integer.compare(this.colour.getValue(), tile.colour.getValue());
    }   
}