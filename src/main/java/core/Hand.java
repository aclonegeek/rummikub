package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hand {
    ArrayList<Tile> hand;
    
    public Hand(){
        hand = new ArrayList<Tile>();
    }
    
    public void add(Tile tile) {
        hand.add(tile);
        sort();
    } 
    
    public Tile remove(int index) {
        if(hand.size() == 0 || index < 0 || index > hand.size()) {
            return null;
        }
        
        return hand.remove(index);
    }

    public int getSize() {
        return hand.size();
    }
    
    public String toString() {
        
        String returnString = "[";
        for(int i = 0; i < hand.size(); i++) {
            returnString += hand.get(i).toString();
            if(i < hand.size() - 1) {
                returnString += ", ";
            }
        }
        
        returnString += "]";
        return returnString;
    }
    
    private void sort() {
        
        //map colour to its order
        Map<Character, Integer> order = new HashMap<Character, Integer>();
        order.put('R', 1); order.put('G', 2); order.put('B', 3); order.put('O', 4);
        
        //sort by colour
        int n = hand.size();
        for(int i = 0; i < n-1; i++) {
            for(int j = 0; j < n-i-1; j++) {
                int colourVal = order.get(hand.get(j).getColour());
                
                if(colourVal > order.get(hand.get(j+1).getColour())){
                    Tile temp = hand.get(j);
                    hand.set(j, hand.get(j+1));
                    hand.set(j+1, temp);
                }
            }
        }
        
        //sort by value within colour
        for(int i = 0; i < n-1; i++) {
            for(int j = 0; j < n-i-1; j++) {
                int colourVal = order.get(hand.get(j).getColour());
                int value = hand.get(j).getValue();
                
                if(value > order.get(hand.get(j+1).getColour()) && colourVal == order.get(hand.get(j+1).getColour())){
                    Tile temp = hand.get(j);
                    hand.set(j, hand.get(j+1));
                    hand.set(j+1, temp);
                }
            }
        }
    }
}
