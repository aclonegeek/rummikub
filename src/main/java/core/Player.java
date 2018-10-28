package core;

import java.util.ArrayList;

public abstract class Player implements TableObserver, PlayerObserver, PlayerSubject {
    private ArrayList<PlayerObserver> observers;
    private int lowestHandCount = -1;

    protected Hand hand;
    protected PlayBehaviour playBehaviour;

    public Player() {
        this.observers = new ArrayList<>();
        this.hand = new Hand();
    }

    public void add(Tile tile) {
        this.hand.add(tile);
    }

    public int getHandSize() {
        return this.hand.getSize();
    }

    public ArrayList<Meld> getWorkspace() {
        return this.playBehaviour.getWorkspace();
    }

    public ArrayList<Meld> play() {
        ArrayList<Meld> newTableState = this.playBehaviour.determineMove(this.hand);
        if (newTableState != null) {
            this.removeTilesFromHand(newTableState);
        }
        return newTableState;
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
        return;
    }

    public void update(int lowestHandCount) {
        if (this.lowestHandCount == -1 || lowestHandCount < this.lowestHandCount) {
            this.lowestHandCount = lowestHandCount;
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

    public int getLowestHandCount() {
        return this.lowestHandCount;
    }

    public ArrayList<PlayerObserver> getObservers() {
        return this.observers;
    }
}
