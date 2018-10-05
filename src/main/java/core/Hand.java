package core;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Tile> hand;
    
    public Hand(){
        this.hand = new ArrayList<>();
    }
    
    public void add(Tile tile) {
        this.hand.add(tile);
        this.sort();
    } 
    
    public Tile remove(int index) {
        if (this.hand.size() == 0 || index < 0 || index > this.hand.size()) {
            return null;
        }
        
        return this.hand.remove(index);
    }

    public int getSize() {
        return this.hand.size();
    }
    
    public String toString() {
        String returnString = "[";
        for (int i = 0; i < this.hand.size(); i++) {
            returnString += this.hand.get(i).toString();
            if (i < this.hand.size() - 1) {
                returnString += ", ";
            }
        }

        returnString += "]";
        return returnString;
    }
    
    private void sort() { 
        // Sort by colour
        int n = this.hand.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int colourVal = this.hand.get(j).getColour().getValue();
                
                if (colourVal > this.hand.get(j + 1).getColour().getValue()) {
                    Tile temp = this.hand.get(j);
                    this.hand.set(j, this.hand.get(j + 1));
                    this.hand.set(j + 1, temp);
                }
            }
        }
        
        // Sort by value within colour
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int colourVal = this.hand.get(j).getColour().getValue();
                int value = this.hand.get(j).getValue();
                
                if (value > this.hand.get(j + 1).getValue() && colourVal == this.hand.get(j + 1).getColour().getValue()) {
                    Tile temp = this.hand.get(j);
                    this.hand.set(j, this.hand.get(j + 1));
                    this.hand.set(j + 1, temp);
                }
            }
        }
    }
}
