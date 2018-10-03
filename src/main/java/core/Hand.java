package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hand {
    ArrayList<Tile> hand;
    
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
        
        // Map colour to its order
        Map<Character, Integer> order = new HashMap<>();
        order.put('R', 1); order.put('G', 2); order.put('B', 3); order.put('O', 4);
        
        // Sort by colour
        int n = this.hand.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int colourVal = order.get(this.hand.get(j).getColour());
                
                if (colourVal > order.get(this.hand.get(j + 1).getColour())) {
                    Tile temp = this.hand.get(j);
                    this.hand.set(j, this.hand.get(j + 1));
                    this.hand.set(j + 1, temp);
                }
            }
        }
        
        // Sort by value within colour
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int colourVal = order.get(this.hand.get(j).getColour());
                int value = this.hand.get(j).getValue();
                
                if (value > this.hand.get(j + 1).getValue() && colourVal == order.get(this.hand.get(j + 1).getColour())) {
                    Tile temp = this.hand.get(j);
                    this.hand.set(j, this.hand.get(j + 1));
                    this.hand.set(j + 1, temp);
                }
            }
        }
    }
}
