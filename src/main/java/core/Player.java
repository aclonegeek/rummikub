package core;

import java.util.ArrayList;

// TODO: make Player a Subject
public abstract class Player {
    protected Hand hand;
    //protected PlayBehaviour playBehaviour;
    
    public void add(Tile tile) {
        this.hand.add(tile);
    }
    
    public int getHandSize() {
        return this.hand.getSize();
    }

    public ArrayList<Meld> play(){
        // ArrayList<Meld> newTableState = this.playBehaviour.determineMove();
        ArrayList<Meld> newTableState = new ArrayList<>(); // Remove this line when playBehaviour is implemented
        this.removeTilesFromHand(newTableState);
        return newTableState;
    }
    
    // Removes tiles from Player's hand that appear in the given ArrayList for which isOnTable is false (ie. they were just added)
    public void removeTilesFromHand(ArrayList<Meld> newTableState) {
        for (int i = 0; i < newTableState.size(); i++) {
            Meld meld = newTableState.get(i);
            for(int j = 0; j < meld.getSize(); j++) {
                Tile tile = meld.getTile(j);
                if(!tile.isOnTable()) {
                    this.hand.remove(tile);
                }
            }
        }     
    }
}