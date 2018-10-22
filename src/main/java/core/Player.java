package core;

import java.util.ArrayList;

public abstract class Player implements TableObserver {
    protected ArrayList<Meld> workspace;
    protected Hand hand;
    protected PlayBehaviour playBehaviour;
    protected boolean initialMove;
    
    public Player() {
        this.workspace = new ArrayList<>();
        this.hand = new Hand();
        this.initialMove = true;
    }
    
    public void add(Tile tile) {
        this.hand.add(tile);
    }
    
    public int getHandSize() {
        return this.hand.getSize();
    }
    
    public ArrayList<Meld> getWorkspace() {
        return this.workspace;
    }

    public ArrayList<Meld> play() {
        ArrayList<Meld> newTableState = this.playBehaviour.determineMove(this.workspace, this.hand, this.initialMove);
        if (newTableState != null) {
            this.initialMove = false;
            this.removeTilesFromHand(newTableState);
            return newTableState;
        }
        else {
            return null;
        }
    }
    
    // Removes tiles from Player's hand that appear in the given ArrayList for which isOnTable is false (ie. they were just added)
    public void removeTilesFromHand(ArrayList<Meld> newTableState) {
        for (Meld meld : newTableState) {
            for (int j = 0; j < meld.getSize(); j++) {
                if (!meld.getTile(j).isOnTable()) {
                    this.hand.remove(meld.getTile(j));
                }
            }
        }
    }
    
    public void update(ArrayList<Meld> melds) {
        this.workspace = melds;
    }
}