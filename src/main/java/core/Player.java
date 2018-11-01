package core;

import java.util.ArrayList;

public abstract class Player implements PlayerSubject {
    protected Hand hand;
    protected PlayBehaviour playBehaviour;
    protected boolean initialMove = true;
    
    private ArrayList<PlayerObserver> observers;

    public Player() {
        this.observers = new ArrayList<>();
        this.hand = new Hand();
    }
    
    abstract ArrayList<Meld> play();

    public void add(Tile tile) {
        this.hand.add(tile);
    }

    public int getHandSize() {
        return this.hand.getSize();
    }

    public ArrayList<Meld> getWorkspace() {
        return this.playBehaviour.getWorkspace();
    }
    
    public int getLowestHandCount() {
        return this.playBehaviour.getLowestHandCount();
    }

    protected PlayBehaviour getPlayBehaviour() {
        return this.playBehaviour;
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

    public void registerObserver(PlayerObserver observer) {
		this.observers.add(observer);
	}

	public void removeObserver(PlayerObserver observer) {
        this.observers.removeIf(o -> observer.equals(o));
	}

	public void notifyObservers() {
        this.observers.forEach(o -> o.update(this.hand.getSize()));
	}

    public ArrayList<PlayerObserver> getObservers() {
        return this.observers;
    }
}