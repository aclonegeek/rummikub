package core;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Hand {
    private ArrayList<Tile> hand;

    public Hand(){
        this.hand = new ArrayList<>();
    }

    public Hand(String hand) {
        this.hand = new ArrayList<>();

        if (hand.length() != 0) {
            for (String tile : hand.split(",")) {
                this.hand.add(new Tile(tile));
                this.sort();
            }
        }
    }
    
    public Hand(Hand hand) {
        this.hand = new ArrayList<>();
        
        for (int i = 0; i < hand.getSize(); i++) {
            this.hand.add(new Tile(hand.getTile(i).toString()));
            this.sort();
        }
    }

    public void add(Tile tile) {
        this.hand.add(tile);
        this.sort();
    }

    // Removes tile at given index
    public Tile remove(int index) {
        if (this.hand.size() == 0 || index < 0 || index > this.hand.size()) {
            return null;
        }

        return this.hand.remove(index);
    }

    // Removes first tile in hand that matches input tile
    public Tile remove(Tile tile) {
        for (int i = 0; i < this.hand.size(); i++) {
            if (tile.toString().equals(hand.get(i).toString())) {
                return this.hand.remove(i);
            }
        }

        return null;
    }

    // Removes each tile in meld from hand
    public void remove(Meld meld) {
        for (int i = 0; i < meld.getSize(); i++) {
            this.hand.remove(meld.getTile(i));
        }
    }

    public boolean containsTile(Tile tile) {
        for (Tile tempTile : this.hand) {
            if (tempTile.toString().equals(tile.toString())) {
                return true;
            }
        }
        return false;
    }

    public int getSize() {
        return this.hand.size();
    }

    public Tile getTile(int index) {
        return this.hand.get(index);
    }

    public String toString() {
        return this.hand.stream()
            .map(Object::toString)
            .collect(Collectors.joining(" ", "[", "]"));
    }

    private void sort() {
        // Remove jokers
        ArrayList<Tile> jokers = new ArrayList<>();
        for (int i = 0; i < this.hand.size(); i++) {
            if (this.hand.get(i).isJoker()) {
                jokers.add(this.hand.remove(i));
            }
        }
        
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
        
        // Add jokers to front of hand
        for (Tile joker : jokers) {
            this.hand.add(0, joker);
        }
    }
}
